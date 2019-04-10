package org.freeandroidtools.trendinggithub.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.freeandroidtools.trendinggithub.db.RepoDatabase
import org.freeandroidtools.trendinggithub.helpers.Constants
import org.freeandroidtools.trendinggithub.helpers.enqueue
import org.freeandroidtools.trendinggithub.model.GithubRepo
import org.freeandroidtools.trendinggithub.model.SearchResult
import org.freeandroidtools.trendinggithub.model.StarredRepo
import org.freeandroidtools.trendinggithub.service.GithubApiService
import org.joda.time.LocalDate
import retrofit2.Call
import java.util.Date
import java.util.concurrent.Executor
import javax.inject.Inject


class GithubApiRepository @Inject constructor(
    private var service: GithubApiService,
    private var repoDatabase: RepoDatabase,
    private var sharedPreferences: SharedPreferences
) {

    @Inject
    lateinit var executor: Executor

    val data: MediatorLiveData<List<GithubRepo>> by lazy { MediatorLiveData<List<GithubRepo>>() }

    /**
     * Get trending repos for the last specified days. It tries to fetch from local db first. if
     * if the data is not available or the data was fetched more tna 24h ago, it fetches from the
     * server and caches the result to db.
     *
     * @param topic a topic to search for
     * @param days the last number of days to search for
     * @return a LiveData with trending repos
     */
    fun getTrending(topic: String, days: Int): Flowable<List<GithubRepo>> {
        val source = repoDatabase.repoDao().getAll()
        source
            .take(1)
            .subscribe {
                executor.execute {
                    val response = trendingReposEndpoint(topic, days)
                        .execute()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            refreshData(it.repos)
                        }
                    }
                }
            }

        return source
    }


    /**
     * Get all repos that the user has starred. Fetches from the
     * server and caches the result to db.
     *
     * @return a LiveData with starred repos
     */
    fun getStarred(user: String): Flowable<List<StarredRepo>> {
        val source = repoDatabase.starredReposDao().getAll()
        source
            .take(1)
            .subscribe {
                service.starredRepos(user)
                    .subscribe(
                        {
                            if (it != null) {
                                refreshStarredData(it)
                            } else {
                                source.doOnError { throw Throwable("No items") }
                            }
                        },
                        {
                            source.doOnError { throw it }
                        }
                    )
            }

        return source
    }

    private fun refreshStarredData(it: List<StarredRepo>) {
        repoDatabase.starredReposDao().deleteAll()
        repoDatabase.starredReposDao().insertAll(it)
    }

    /**
     * Returns repo with the given id
     *
     * @param id of the repo to get
     */
    fun getRepo(id: String): LiveData<List<GithubRepo>> {
        return repoDatabase.repoDao().getById(id)
    }

    /**
     * Syncs the local db with the server and updates the LiveData
     * @param topic a topic to search for
     * @param days the last number of days to search for
     * @return a LiveData with trending repos
     */
    fun refreshTrending(topic: String, days: Int) {
        trendingReposEndpoint(topic, days)
            .enqueue(
                {
                    if (it.isSuccessful) {
                        it.body()?.let {
                            executor.execute {
                                refreshData(it.repos)
                            }
                        }
                    }
                },
                {
                    //                            data.value = data.value
                }
            )
    }

    private fun trendingReposEndpoint(topic: String, days: Int): Call<SearchResult> {
        val fromDate = LocalDate().minusDays(days)
        val query = "topic:$topic+created:>$fromDate+stars:>1"
        return service.searchRepos(query, "stars", "desc")
    }


    /**
     * Deletes all items in db and inserts repos
     *
     * @param repos repository list to insert
     */
    private fun refreshData(repos: List<GithubRepo>) {
        repoDatabase.repoDao().deleteAll()
        repoDatabase.repoDao().insertAll(repos)
        sharedPreferences.edit()
            .putLong(Constants.REFRESH_TIMESTAMP_KEY, Date().time)
            .apply()
    }

    fun logout() {
        repoDatabase.authDao()
            .getSelected()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    it?.userId?.let {
                        repoDatabase.userDao().deleteById(it)
                    }
                    repoDatabase.authDao().deleteAll()
                },
                {

                }
            )
    }

    fun getStarredRepoById(id: String): LiveData<List<StarredRepo>> {
        return repoDatabase.starredReposDao().getById(id)
    }
}