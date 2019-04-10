package org.freeandroidtools.trendinggithub.di

import dagger.Component
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.ui.login.LoginViewModel
import org.freeandroidtools.trendinggithub.ui.main.MainViewModel
import org.freeandroidtools.trendinggithub.ui.repodetail.RepoDetailViewModel
import org.freeandroidtools.trendinggithub.ui.splash.SplashViewModel
import org.freeandroidtools.trendinggithub.ui.starred.StarredViewModel
import org.freeandroidtools.trendinggithub.ui.trending.TrendingViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun getApp(): TrendingApp

    fun inject(viewModel: TrendingViewModel)
    fun inject(viewModel: RepoDetailViewModel)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(splashViewModel: SplashViewModel)
    fun inject(starredViewModel: StarredViewModel)
    fun inject(mainViewModel: MainViewModel)
}