package org.freeandroidtools.trendinggithub.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.model.GithubRepo
import org.freeandroidtools.trendinggithub.repository.GithubApiRepository
import javax.inject.Inject

class TrendingViewModel : ViewModel() {
    init {
        TrendingApp.appComponent.inject(this)
    }

    @Inject
    lateinit var repository: GithubApiRepository

    val trendingData: MutableLiveData<List<GithubRepo>> by lazy {
        getTrending()
    }

    fun getTrending(): MutableLiveData<List<GithubRepo>> =
        repository.getTrending("Android", 30)

    fun refreshTrending() =
        repository.refreshTrending("Android", 30)

}