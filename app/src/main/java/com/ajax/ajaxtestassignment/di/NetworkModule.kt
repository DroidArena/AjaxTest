package ua.medstar.idis.di

import android.content.Context
import androidx.core.content.getSystemService
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ua.medstar.idis.BuildConfig
import ua.medstar.idis.RealAppProvider
import ua.medstar.idis.data.*
import ua.medstar.idis.data.api.ApiService
import ua.medstar.idis.data.api.AuthApi
import ua.medstar.idis.data.api.TokenAuthenticator
import ua.medstar.idis.data.api.adapters.FileJsonAdapter
import ua.medstar.idis.data.api.adapters.UUIDJsonAdapter
import ua.medstar.idis.data.api.interceptors.SessionInterceptor
import ua.medstar.idis.data.api.model.ObservationModel
import ua.medstar.idis.data.api.state.NetworkStateManager
import ua.medstar.idis.data.api.state.NetworkStateManagerImpl
import ua.medstar.idis.data.db.model.Kit
import ua.medstar.idis.data.db.model.SupportedDevice
import ua.medstar.idis.data.model.*
import ua.medstar.idis.exceptions.isNetworkException
import ua.medstar.idis.util.Spo2
import ua.medstar.idis.util.Thermometer
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Suppress("unused")
@Module(includes = [InterceptorsModule::class])
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideHttpUrl(storage: LocalStorage): HttpUrl = storage.baseUtl.toHttpUrl()

    @Named("okhttpAuth")
    @Singleton
    @Provides
    fun provideOkHttpClientAuth(okHttpBuilder: OkHttpClient.Builder): OkHttpClient {
        return okHttpBuilder.build()
    }

    @Named("retrofitAuth")
    @Singleton
    @Provides
    fun provideRetrofitAuth(
        httpUrl: HttpUrl,
        @Named("okhttpAuth") client: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(httpUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggerInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        return logger
    }

    @Named("okhttp")
    @Singleton
    @Provides
    fun provideOkHttpClient(
        okHttpBuilder: OkHttpClient.Builder,
        authRepository: AuthRepository
    ): OkHttpClient {
        return okHttpBuilder.authenticator(TokenAuthenticator(authRepository))
            .build()
    }

    @Named("retrofit")
    @Singleton
    @Provides
    fun provideRetrofit(
        httpUrl: HttpUrl,
        @Named("okhttp") client: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(httpUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    /**
     * FIXME replace with retrofit.create(AuthApi::class.java)
     */
    @Singleton
    @Provides
    fun provideAuthApi(@Named("retrofitAuth") retrofit: Retrofit): AuthApi {
        return object : AuthApi {
            override suspend fun activate(deviceInfo: DeviceInfo): Activation {
                return Activation(code = "1234")
            }

            override suspend fun authorize(deviceInfo: DeviceInfo): Activation {
                return Activation(key = "abcd")
            }

            override suspend fun checkUserAuth(authData: UserAuthModel): UserInfo {
                TODO("Not yet implemented")
            }

            override suspend fun checkKit() {
            }
        }
    }

    /**
     * FIXME remove
     */
    private var kit = Kit(
        1, KitStatus.New.code, "123456789",
        listOf(
//            SupportedDevice(
//                id = 1,
//                deviceModelId = 1,
//                deviceModelName = Spo2.MI_SMART_BAND_5.prefix,//Spo2.CONTEC_SPO208.prefix,
//                toolTypeSlug = "spo2",
//                deviceType = "bluetooth",
//                macAddress = null
//            ),
            SupportedDevice(
                id = 2,
                deviceModelId = 2,
                deviceModelName = Thermometer.MANUAL.prefix,
                toolTypeSlug = "temp",
                deviceType = "manual",
                macAddress = null
            )
        )
    )

    /**
     * FIXME replace with retrofit.create(ApiService::class.java)
     */
    @Singleton
    @Provides
    fun provideApiInterface(@Named("retrofit") retrofit: Retrofit): ApiService {
        return object : ApiService {
            override suspend fun getNumbers(quantity: Int): List<String> {
                return listOf("001", "002", "003", "004", "005", "006", "007")
            }

            override suspend fun getKit(): Kit {
                return kit
            }

            override suspend fun postKit(kit: Kit): Kit {
                return if (kit.devices?.all { !it.macAddress.isNullOrEmpty() } == true) {
                    this@NetworkModule.kit = kit.copy(status = KitStatus.Configured.code)
                    this@NetworkModule.kit
                } else {
                    kit
                }
            }

            override suspend fun postExamination(observationModel: ObservationModel): retrofit2.Response<Unit> {
                TODO("Not yet implemented")
            }

            override suspend fun uploadMedia(file: MultipartBody.Part): MediaModel {
                TODO("Not yet implemented")
            }

            override suspend fun postConsumablesRequest(consumablesRequest: ConsumablesRequest) {
                TODO("Not yet implemented")
            }

            override suspend fun getNewVersion(
                versionCode: Int,
                deviceType: String
            ): UpdateResponse {
                return UpdateResponse(retrofit.baseUrl().toString() + "idis_2_0.apk",2, "2.0")
            }
        }
    }

    @Singleton
    @Provides
    fun provideNetworkStateManager(@ApplicationContext context: Context): NetworkStateManager {
        return NetworkStateManagerImpl(
            context.getSystemService()!!,
            context.applicationContext as? RealAppProvider
        )
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .add(FileJsonAdapter())
            .add(UUIDJsonAdapter())
            .build()
    }

    @Provides
    fun provideOkHttpBuilder(
        loggerInterceptor: HttpLoggingInterceptor,
        storage: LocalStorage,
        networkStateManager: NetworkStateManager,
        networkInterceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient.Builder {

        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(SessionInterceptor(storage))
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    return try {
                        chain.proceed(chain.request())
                    } catch (e: Exception) {
                        //TODO check if isNetworkException() is accurate enough and if not, uncomment
                        if (e.isNetworkException()/* && !networkStateManager.isNetworkAvailable()*/) {
                            networkStateManager.setOffline()
                        }
                        throw e
                    }
                }
            })
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggerInterceptor)
        }
        for (networkInterceptor in networkInterceptors) {
            builder.addNetworkInterceptor(networkInterceptor)
        }
        return builder
    }
}