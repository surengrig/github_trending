package org.freeandroidtools.trendinggithub.repository

import android.arch.lifecycle.MediatorLiveData
import android.content.SharedPreferences
import org.freeandroidtools.trendinggithub.helpers.Constants
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.db.RepoDatabase
import org.freeandroidtools.trendinggithub.helpers.enqueue
import org.freeandroidtools.trendinggithub.model.GithubRepo
import org.freeandroidtools.trendinggithub.service.GithubApiService
import org.joda.time.LocalDate
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject


class GithubApiRepository {

    init {
        TrendingApp.appComponent.inject(this)
    }

    @Inject
    lateinit var service: GithubApiService

    @Inject
    lateinit var repoDatabase: RepoDatabase

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val executor: Executor = Executors.newFixedThreadPool(2)


    val data: MediatorLiveData<List<GithubRepo>> by lazy { MediatorLiveData<List<GithubRepo>>() }

    /**
     *  Get trending repos for the last specified days
     * @param topic a topic to search for
     * @param days the last number of days to search for
     */
    fun getTrending(topic: String, days: Int): MediatorLiveData<List<GithubRepo>> {
        val source = repoDatabase.repoDao().getAll()

        val fromDate = LocalDate().minusDays(days)
        val query = "topic:$topic+created:>$fromDate+stars:>1"

        data.addSource(source) {
            val lastModified = sharedPreferences.getLong(Constants.REFRESH_TIMESTAMP_KEY, Constants.REFRESH_TIMEOUT)

            if (it?.isNotEmpty() == false || Date().time - lastModified >= Constants.REFRESH_TIMEOUT) {
                executor.execute {
                    val response = service.searchRepos(query, "stars", "desc")
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

    fun refreshTrending(topic: String, days: Int) {
        val fromDate = LocalDate().minusDays(days)
        val query = "topic:$topic+created:>$fromDate+stars:>1"

        service.searchRepos(query, "stars", "desc")
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