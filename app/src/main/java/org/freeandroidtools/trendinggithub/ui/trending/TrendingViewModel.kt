package org.freeandroidtools.trendinggithub.ui.trending

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
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

    fun getTrending(): LiveData<List<GithubRepo>> {
        return LiveDataReactiveStreams.fromPublisher(repository.getTrending("Android", 30))
    }

    fun refreshTrending() = repository.refreshTrending("Android", 30)

}