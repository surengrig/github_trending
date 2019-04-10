package org.freeandroidtools.trendinggithub.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.helpers.Error
import org.freeandroidtools.trendinggithub.helpers.Result
import org.freeandroidtools.trendinggithub.helpers.Success
import org.freeandroidtools.trendinggithub.model.User
import org.freeandroidtools.trendinggithub.repository.AuthorizationRepository
import timber.log.Timber
import javax.inject.Inject

class SplashViewModel : ViewModel() {

    @Inject
    lateinit var repository: AuthorizationRepository

    init {
        TrendingApp.appComponent.inject(this)
    }

    private val signInData by lazy { MutableLiveData<Result<User>>() }

    fun autoLogin(): MutableLiveData<Result<User>> {
        repository.getAuthorisedUser()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    signInData.postValue(
                        if (it.isEmpty()) {
                            Error(Throwable("no user"))
                        } else {
                            Success(it[0])
                        }
                    )
                },
                {
                    signInData.postValue(Error(it))
                    Timber.d("error ${it.message}")
                }

            )

        return signInData
    }
}