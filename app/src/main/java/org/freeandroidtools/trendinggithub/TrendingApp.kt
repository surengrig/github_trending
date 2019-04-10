package org.freeandroidtools.trendinggithub

import android.app.Application
import org.freeandroidtools.trendinggithub.di.AppComponent
import org.freeandroidtools.trendinggithub.di.AppModule
import org.freeandroidtools.trendinggithub.di.DaggerAppComponent
import org.freeandroidtools.trendinggithub.model.User
import timber.log.Timber


class TrendingApp : Application() {

    var token: String? = null
    var user: User? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()

        Timber.plant(Timber.DebugTree())
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}