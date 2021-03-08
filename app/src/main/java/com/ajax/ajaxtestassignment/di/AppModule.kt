package com.ajax.ajaxtestassignment.di

import android.content.Context
import com.ajax.ajaxtestassignment.data.preferences.UserPreferences
import com.ajax.ajaxtestassignment.data.preferences.UserPreferencesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLocalStorage(@ApplicationContext context: Context): UserPreferences {
        return UserPreferencesImpl(context)
    }
}