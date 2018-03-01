package org.freeandroidtools.trendinggithub.di

import dagger.Component
import org.freeandroidtools.trendinggithub.ui.main.TrendingViewModel
import org.freeandroidtools.trendinggithub.ui.repodetail.RepoDetailViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(viewModel: TrendingViewModel)
    fun inject(viewModel: RepoDetailViewModel)
}