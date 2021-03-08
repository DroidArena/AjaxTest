package com.ajax.ajaxtestassignment.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.medstar.idis.data.db.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): IdisDatabase {
        return Room.databaseBuilder(context, IdisDatabase::class.java, "db")
                .build()
    }

    @Singleton
    @Provides
    fun providesAnalyseDao(database: IdisDatabase): AnalyseDao {
        return database.analyseDao()
    }

    @Singleton
    @Provides
    fun providesExaminationDao(database: IdisDatabase): ExaminationDao {
        return database.examinationDao()
    }

    @Singleton
    @Provides
    fun providesFreeNumbersDAO(database: IdisDatabase): FreeNumbersDao {
        return database.freeNumbersDao()
    }

    @Singleton
    @Provides
    fun providesKitDAO(database: IdisDatabase): KitDao {
        return database.kitDao()
    }

    @Singleton
    @Provides
    fun providesMediaDAO(database: IdisDatabase): MediaDao {
        return database.mediaDao()
    }

    @Singleton
    @Provides
    fun providesResultsDao(database: IdisDatabase): ResultsDao {
        return database.resultsDao()
    }
}