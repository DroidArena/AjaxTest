package com.ajax.ajaxtestassignment.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ua.medstar.idis.DebugInitializer

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class AppInitializersModule {
    @Binds
    @IntoSet
    abstract fun providesDebugInitializer(initializer: DebugInitializer): AppInitializer
}