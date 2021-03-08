package com.ajax.ajaxtestassignment.di

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}