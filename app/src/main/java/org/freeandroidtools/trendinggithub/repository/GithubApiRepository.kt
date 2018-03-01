package org.freeandroidtools.trendinggithub.repository

import android.arch.lifecycle.MediatorLiveData
import android.content.SharedPreferences
import org.freeandroidtools.trendinggithub.db.RepoDatabase
import org.freeandroidtools.trendinggithub.helpers.Constants
import org.freeandroidtools.trendinggithub.helpers.enqueue
import org.freeandroidtools.trendinggithub.model.GithubRepo
import org.freeandroidtools.trendinggithub.model.SearchResult
import org.freeandroidtools.trendinggithub.service.GithubApiService
import org.joda.time.LocalDate
import retrofit2.Call
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject


class GithubApiRepository @Inject constructor(private var service: GithubApiService,
                                              private var repoDatabase: RepoDatabase,
                                              private var sharedPreferences: SharedPreferences) {

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
    fun getTrending(topic: String, days: Int): MediatorLiveData<List<GithubRepo>> {
        val source = repoDatabase.repoDao().getAll()
        data.addSource(source) {
            val lastModified = sharedPreferences.getLong(Constants.REFRESH_TIMESTAMP_KEY, Constants.REFRESH_TIMEOUT)

            if (it?.isNotEmpty() == false || Date().time - lastModified >= Constants.REFRESH_TIMEOUT) {
                executor.execute {
                    val response = trendingReposEndpoint(topic, days)
                            .execute()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            refreshData(it.repos)
                        }
                    }
                }
            } else {
                data.value = it
            }
        }
        return data
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
                            } else {
                                data.value = data.value
                            }
                        },
                        {
                            data.value = data.value
                        })
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

    companion object {
        val TAG: String = GithubApiRepository::class.java.simpleName
    }
}