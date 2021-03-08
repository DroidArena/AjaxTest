package ua.medstar.idis.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.medstar.idis.data.*
import ua.medstar.idis.data.api.AuthApi
import ua.medstar.idis.notification.DeviceTokenProvider
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideExamRepository(examRepository: ExamRepositoryImpl): ExamRepository

    @Binds
    @Singleton
    abstract fun provideKitRepository(kitRepository: KitRepositoryImpl): KitRepository

    @Binds
    @Singleton
    abstract fun provideAnalyseRepository(analyseRepository: AnalyseRepositoryImpl): AnalyseRepository

    @Binds
    @Singleton
    abstract fun provideResultsRepository(resultsRepository: ResultsRepositoryImpl): ResultsRepository

    @Binds
    @Singleton
    abstract fun provideMediaRepository(mediaRepository: MediaRepositoryImpl): MediaRepository

    @Binds
    @Singleton
    abstract fun provideAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun provideObservationSynchronizer(observationSynchronizer: ObservationSynchronizerImpl): ObservationSynchronizer
}