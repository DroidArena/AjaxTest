package com.ajax.ajaxtestassignment

import android.app.Application
import com.ajax.ajaxtestassignment.di.AppInitializer
import com.facebook.stetho.Stetho
import timber.log.Timber
import javax.inject.Inject

class DebugInitializer @Inject constructor(): AppInitializer {
    override fun init(application: Application) {
        Timber.plant(Timber.DebugTree())

        Stetho.initializeWithDefaults(application)
    }
}