package org.freeandroidtools.trendinggithub.service

import com.google.gson.JsonArray
import io.reactivex.Observable
import okhttp3.ResponseBody
import org.freeandroidtools.trendinggithub.model.AuthRequest
import org.freeandroidtools.trendinggithub.model.SearchResult
import org.freeandroidtools.trendinggithub.model.StarredRepo
import org.freeandroidtools.trendinggithub.model.Token
import org.freeandroidtools.trendinggithub.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface GithubApiService {

    @GET("search/repositories")
    fun searchRepos(
        @Query("q", encoded = true) searchKey: String,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): Call<SearchResult>


    @GET("users/{user}/repos")
    fun listUserRepo(@Path("user") user: String): Call<JsonArray>


    @POST("authorizations")
    @Headers("Accept: application/json")
    fun authorizations(
        @Body authRequest: AuthRequest
    ): Observable<Token>


    @GET("user")
    fun getLoggedInUser(): Observable<User>

    @GET("users/{user}/starred")
    fun starredRepos(@Path("user") user: String): Observable<List<StarredRepo>>

    @PUT("user/starred/{owner}/{repo}")
    fun starRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Observable<ResponseBody>
}
