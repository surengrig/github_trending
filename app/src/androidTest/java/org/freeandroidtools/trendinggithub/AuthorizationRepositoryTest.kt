package org.freeandroidtools.trendinggithub

import android.database.sqlite.SQLiteConstraintException
import io.reactivex.Observable
import org.freeandroidtools.trendinggithub.db.AuthDao
import org.freeandroidtools.trendinggithub.db.RepoDatabase
import org.freeandroidtools.trendinggithub.db.UserDao
import org.freeandroidtools.trendinggithub.model.Token
import org.freeandroidtools.trendinggithub.model.User
import org.freeandroidtools.trendinggithub.repository.AuthorizationRepository
import org.freeandroidtools.trendinggithub.service.GithubApiService
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class AuthorizationRepositoryTest {
    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var service: GithubApiService
    @Mock
    private lateinit var repoDatabase: RepoDatabase
    @Mock
    private lateinit var authDao: AuthDao
    @Mock
    private lateinit var userDao: UserDao

    @InjectMocks
    private lateinit var authRepository: AuthorizationRepository

    @Test
    fun login_success_test() {
        val fakeToken = "token123"
        Mockito.`when`(
            service.authorizations(any())
        ).thenReturn(
            Observable.just(Token().apply { token = fakeToken })
        )
        Mockito.`when`(service.getLoggedInUser())
            .thenReturn(Observable.just(User().apply { token = fakeToken }))

        Mockito.`when`(repoDatabase.authDao())
            .thenReturn(authDao)
        Mockito.`when`(repoDatabase.userDao())
            .thenReturn(userDao)

        Mockito.`when`(repoDatabase.authDao().insert(any()))
            .thenReturn(1)
        Mockito.`when`(repoDatabase.userDao().insert(any()))
            .thenReturn(1)

        authRepository.signIn("user", "password")
            .test()
            .assertValue {
                it.token == fakeToken
            }
    }

    @Test
    fun login_error_test() {
        val fakeToken = "token123"
        Mockito.`when`(
            service.authorizations(any())
        )
            .thenReturn(
                Observable.just(Token().apply { token = fakeToken })
            )
        Mockito.`when`(service.getLoggedInUser())
            .thenReturn(Observable.just(User().apply { token = fakeToken }))
        Mockito.`when`(repoDatabase.authDao())
            .thenReturn(authDao)
        Mockito.`when`(repoDatabase.userDao())
            .thenReturn(userDao)

        val fakeError = SQLiteConstraintException("Error occurred")
        Mockito.`when`(repoDatabase.authDao().insert(any()))
            .thenThrow(fakeError)
        Mockito.`when`(repoDatabase.userDao().insert(any()))
            .thenReturn(1)

        authRepository.signIn("user", "password")
            .test()
            .assertError(fakeError)
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}
