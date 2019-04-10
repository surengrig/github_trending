package org.freeandroidtools.trendinggithub.ui.starred

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.model.StarredRepo
import org.freeandroidtools.trendinggithub.repository.GithubApiRepository
import javax.inject.Inject

class StarredViewModel : ViewModel() {
    init {
        TrendingApp.appComponent.inject(this)
    }

    @Inject
    lateinit var repository: GithubApiRepository

    fun getStarred(user: String): LiveData<List<StarredRepo>> {
        return LiveDataReactiveStreams.fromPublisher(repository.getStarred(user))
    }

}