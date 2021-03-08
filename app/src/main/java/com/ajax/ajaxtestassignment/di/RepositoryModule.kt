package com.ajax.ajaxtestassignment.di

import com.ajax.ajaxtestassignment.data.ContactsRepository
import com.ajax.ajaxtestassignment.data.ContactsRepositoryImpl
import com.ajax.ajaxtestassignment.data.api.Api
import com.ajax.ajaxtestassignment.data.database.ContactsDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideContactRepository(api: Api,
                                 contactsDao: ContactsDao,
                                 @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher): ContactsRepository {
        return ContactsRepositoryImpl(api, contactsDao, coroutineDispatcher)
    }
}