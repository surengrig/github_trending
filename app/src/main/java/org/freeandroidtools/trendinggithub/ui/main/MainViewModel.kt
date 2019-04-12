package org.freeandroidtools.trendinggithub.ui.main

import androidx.lifecycle.ViewModel
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.repository.GithubApiRepository
import javax.inject.Inject


class MainViewModel : ViewModel() {
    init {
        TrendingApp.appComponent.inject(this)
    }

    @Inject
    lateinit var repository: GithubApiRepository

    fun logout() {
        repository.logout()
    }

    override fun onCleared() {
        repository.dispose()
        super.onCleared()
    }
}