package com.ajax.ajaxtestassignment.di

import android.content.Context
import androidx.work.WorkManager
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import ua.medstar.idis.data.*
import ua.medstar.idis.data.api.ApiService
import ua.medstar.idis.notification.DeviceTokenProvider
import ua.medstar.idis.notification.DeviceTokenProviderImpl
import ua.medstar.idis.util.DeviceGenerator
import ua.medstar.idis.util.DeviceGeneratorImpl
import java.time.Clock
import javax.inject.Singleton
import ua.medstar.idis.data.FileStorage
import ua.medstar.idis.data.FileStorageImpl

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideFileStorage(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): FileStorage {
        return FileStorageImpl(context, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideAppUpdateManager(
        @ApplicationContext context: Context,
        apiService: ApiService,
        fileStorage: FileStorage
    ): AppUpdateManager {
        return AppUpdateManagerImpl(context, apiService, fileStorage)
    }

    @Provides
    @Singleton
    fun provideLocalStorage(@ApplicationContext context: Context, moshi: Moshi): LocalStorage {
        return DefaultLocalStorage(context, moshi)
    }

    @Provides
    @Singleton
    fun provideDeviceTokenProvider(): DeviceTokenProvider {
        return DeviceTokenProviderImpl()
    }

    @Provides
    @Singleton
    fun provideDeviceGenerator(): DeviceGenerator {
        return DeviceGeneratorImpl
    }


    @Provides
    @Singleton
    @DefaultClock
    fun provideDefaultClock(): Clock {
        return Clock.systemDefaultZone()
    }
}