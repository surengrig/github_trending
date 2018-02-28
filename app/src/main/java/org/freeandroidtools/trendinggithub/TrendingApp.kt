package org.freeandroidtools.trendinggithub

import android.app.Application
import org.freeandroidtools.trendinggithub.di.*


class TrendingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}