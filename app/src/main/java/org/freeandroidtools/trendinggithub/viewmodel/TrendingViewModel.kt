package org.freeandroidtools.trendinggithub.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.model.GithubRepo
import org.freeandroidtools.trendinggithub.repository.GithubApiRepository
import javax.inject.Inject

class TrendingViewModel : ViewModel() {
    init {
        TrendingApp.netComponent.inject(this)
    }

    @Inject
    lateinit var repository: GithubApiRepository

    val trendingData: MutableLiveData<List<GithubRepo>> = MutableLiveData()

    fun getTrending() {
        repository.getTrending(trendingData, "Android", 30)
    }

}