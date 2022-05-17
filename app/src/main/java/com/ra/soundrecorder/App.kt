package com.ra.soundrecorder

import android.app.Application
import com.ra.soundrecorder.di.DaggerMainComponent
import com.ra.soundrecorder.di.MainComponent

open class App: Application() {
    val mainComponent: MainComponent by lazy {
        DaggerMainComponent.factory().create(applicationContext)
    }
}