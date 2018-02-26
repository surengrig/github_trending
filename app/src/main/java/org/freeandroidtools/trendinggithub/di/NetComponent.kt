package org.freeandroidtools.trendinggithub.di

import dagger.Component
import org.freeandroidtools.trendinggithub.repository.GithubApiRepository
import org.freeandroidtools.trendinggithub.viewmodel.TrendingViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetModule::class])
interface NetComponent {
    fun inject(repository: GithubApiRepository)
    fun inject(viewModel: TrendingViewModel)
}