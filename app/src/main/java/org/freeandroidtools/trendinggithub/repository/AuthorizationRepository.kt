package org.freeandroidtools.trendinggithub.repository

import io.reactivex.Maybe
import io.reactivex.Observable
import okhttp3.Credentials
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.db.RepoDatabase
import org.freeandroidtools.trendinggithub.model.AuthRequest
import org.freeandroidtools.trendinggithub.model.AuthorizedUser
import org.freeandroidtools.trendinggithub.model.Token
import org.freeandroidtools.trendinggithub.model.User
import org.freeandroidtools.trendinggithub.service.GithubApiService
import timber.log.Timber
import javax.inject.Inject


class AuthorizationRepository @Inject constructor(
    private var service: GithubApiService,
    private var repoDatabase: RepoDatabase
) {

    /**
     * Sets new authorization token and gets the info about authorised uses
     *
     * @return Observable with user info
     */
    private fun getUserInfo(token: String?): Observable<User> {
        val application = TrendingApp.appComponent.getApp()
        application.token = token
        return service.getLoggedInUser()
    }


    private fun authorize(user: String, password: String): Observable<Token> {
        val authRequest = AuthRequest.generate()
        val token = Credentials.basic(user, password)
        val application = TrendingApp.appComponent.getApp()
        application.token = token
        return service.authorizations(authRequest)
    }

    fun signIn(user: String, password: String): Observable<User> {
        var token = ""
        return authorize(user, password)
            .flatMap {
                Timber.d("getUserInfo")
                token = it.token!!
                getUserInfo(it.token)
            }.doOnNext {
                Timber.d("insert to db")
                it.token = token
                val id = repoDatabase.userDao().insert(it)
                repoDatabase.authDao().insert(AuthorizedUser(id))

                val application = TrendingApp.appComponent.getApp()
                application.user = it
            }
    }

    /**
     * Get trending repos for the last specified days. It tries to fetch from local db first. if
     * if the data is not available or the data was fetched more tna 24h ago, it fetches from the
     * server and caches the result to db.
     *
     * @return a LiveData with trending repos
     */
    fun getAuthorisedUser(): Maybe<List<User>> {
        return repoDatabase.authDao()
            .getSelected()
            .flatMapMaybe {
                getUserById(it.userId)
            }.doAfterSuccess {
                if (it.isNotEmpty()) {
                    val application = TrendingApp.appComponent.getApp()
                    application.token = it[0].token
                    application.user = it[0]
                }
            }
    }

    private fun getUserById(userId: Long): Maybe<List<User>> {
        return repoDatabase.userDao().getById(userId)
    }
}