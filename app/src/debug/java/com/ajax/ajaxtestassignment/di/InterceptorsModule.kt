package com.ajax.ajaxtestassignment.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor

@Module
@InstallIn(SingletonComponent::class)
object InterceptorsModule {
    @Provides
    @IntoSet
    fun providesStethoInterceptor(): Interceptor = StethoInterceptor()
}