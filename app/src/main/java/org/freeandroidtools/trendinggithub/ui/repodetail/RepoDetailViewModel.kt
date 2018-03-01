package org.freeandroidtools.trendinggithub.ui.repodetail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.model.GithubRepo
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
}