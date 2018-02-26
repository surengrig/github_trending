package org.freeandroidtools.trendinggithub.service

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.google.gson.JsonArray
import org.freeandroidtools.trendinggithub.model.SearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GithubApiService {

    @GET("search/repositories")
    fun searchRepos(@Query("q", encoded = true) searchKey: String,
                    @Query("sort") sort: String,
                    @Query("order") order: String): Call<SearchResult>


    @GET("users/{user}/repos")
    fun listUserRepo(@Path("user") user: String): Call<JsonArray>
}
