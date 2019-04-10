package org.freeandroidtools.trendinggithub.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.helpers.Error
import org.freeandroidtools.trendinggithub.helpers.Result
import org.freeandroidtools.trendinggithub.helpers.Success
import org.freeandroidtools.trendinggithub.model.User
import org.freeandroidtools.trendinggithub.repository.AuthorizationRepository
import timber.log.Timber
import javax.inject.Inject


class LoginViewModel : ViewModel() {

    init {
        TrendingApp.appComponent.inject(this)
    }

    @Inject
    lateinit var repository: AuthorizationRepository


    private val disposables = CompositeDisposable()
    private val schedulersFacade: org.freeandroidtools.trendinggithub.helpers.SchedulersFacade =
        org.freeandroidtools.trendinggithub.helpers.SchedulersFacade()

    val singinData by lazy { MutableLiveData<Result<User>>() }

    fun signin(user: String, password: String): LiveData<Result<User>> {

        val ob = repository.signIn(user, password)

        disposables.add(
            ob.subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(
                    {
                        singinData.value = Success(it)
                    },
                    {
                        singinData.value = Error(it)
                        Timber.d("error ${it.message}")
                    }
                )
        )

        return singinData
    }
}