package com.ajax.ajaxtestassignment.di

import android.content.Context
import androidx.room.Room
import com.ajax.ajaxtestassignment.data.database.AjaxDatabase
import com.ajax.ajaxtestassignment.data.database.ContactsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AjaxDatabase {
        return Room.databaseBuilder(context, AjaxDatabase::class.java, "db")
            .build()
    }

    @Singleton
    @Provides
    fun providesContactsDao(database: AjaxDatabase): ContactsDao {
        return database.contactsDao()
    }
}