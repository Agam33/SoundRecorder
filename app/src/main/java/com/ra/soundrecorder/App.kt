package com.ra.soundrecorder

import android.app.Application
import com.ra.soundrecorder.di.DaggerMainComponent
import com.ra.soundrecorder.di.MainComponent
import timber.log.Timber


open class App: Application() {
    val mainComponent: MainComponent by lazy {
        DaggerMainComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

    }
}