package org.freeandroidtools.trendinggithub

import android.app.Application
import android.util.Log
import org.freeandroidtools.trendinggithub.di.*


class TrendingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        netComponent = DaggerNetComponent.builder().netModule(NetModule()).build()
    }

    companion object {
        lateinit var netComponent: NetComponent
    }
}