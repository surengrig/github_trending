package org.freeandroidtools.trendinggithub.ui.repodetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.model.GithubRepo
import org.freeandroidtools.trendinggithub.model.StarredRepo
import org.freeandroidtools.trendinggithub.repository.GithubApiRepository
import javax.inject.Inject

class RepoDetailViewModel : ViewModel() {
    init {
        TrendingApp.appComponent.inject(this)
    }

    @Inject
    lateinit var repository: GithubApiRepository

    fun getRepo(id: String): LiveData<List<GithubRepo>> =
            repository.getRepo(id)

    fun geStarredRepo(id: String): LiveData<List<StarredRepo>> =
            repository.getStarredRepoById(id)

    override fun onCleared() {
        repository.dispose()
        super.onCleared()
    }
}