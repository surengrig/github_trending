package org.freeandroidtools.trendinggithub.repository

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import org.freeandroidtools.trendinggithub.enqueue
import org.freeandroidtools.trendinggithub.model.GithubRepo
import org.freeandroidtools.trendinggithub.model.SearchResult
import org.freeandroidtools.trendinggithub.service.GithubApiService
import org.joda.time.LocalDate
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class GithubApiRepository(private val service: GithubApiService) {

    /**
     *  Get trending repos for the last specified days
     * @param data LiveData updated with fetched repos
     * @param topic a topic to search for
     * @param days the last number of days to search for
     */
    fun getTrending(data: MutableLiveData<List<GithubRepo>>, topic: String, days: Int) {
        val fromDate = LocalDate().minusDays(days)
        val query = "topic:$topic+created:>$fromDate+stars:>1"

        service.searchRepos(
                query,
                "stars",
                "desc"
        ).enqueue(
                {
                    it.body()?.repos?.forEach { Log.d(TAG, "stars:${it.stargazersCount} score:${it.score}, forks${it.forksCount}") }
                    data.value = it.body()?.repos
                },
                {
                    data.value = null
                })
        service.searchRepos(
                query,
                "stars",
                "desc"
        ).enqueue(object : retrofit2.Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>?, response: Response<SearchResult>?) {}
            override fun onFailure(call: Call<SearchResult>?, t: Throwable?) {}
        })
    }

    companion object {
        val TAG: String = GithubApiRepository::class.java.simpleName
    }
}