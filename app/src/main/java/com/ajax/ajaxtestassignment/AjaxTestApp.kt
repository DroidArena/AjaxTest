package com.ajax.ajaxtestassignment

import android.app.Application
import com.ajax.ajaxtestassignment.di.AppInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AjaxTestApp: Application() {
    @Inject
    lateinit var initializers: Set<@JvmSuppressWildcards AppInitializer>

    override fun onCreate() {
        super.onCreate()

        initializers.forEach {
            it.init(this)
        }
    }
}