package com.kanika.airqualitmonitoring.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kanika.airqualitmonitoring.SOCKET_URL
import com.kanika.airqualitmonitoring.data.db.AirQaulityDataDatabase
import com.kanika.airqualitmonitoring.model.AirQaulityDataModel
import com.kanika.airqualitmonitoring.repository.AirQaulityDataSocketImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesOkhttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().pingInterval(30, TimeUnit.SECONDS).build()
    }

    @Provides
    @Singleton
    fun providesOkHttpRequest(): Request {
        return Request.Builder().url(SOCKET_URL).build()
    }

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext appContext: Context) =
        AirQaulityDataDatabase.createDb(appContext)

    @Singleton
    @Provides
    fun providesAppDao(db: AirQaulityDataDatabase) = db.cityAqiDao()

    @Singleton
    @Provides
    fun providesGson() = Gson()

    @Singleton
    @Provides
    fun providesGsonTypeToken(): Type {
        return object : TypeToken<List<AirQaulityDataModel?>?>() {}.type
    }

    @Singleton
    @Provides
    fun providesWebSocketImpl(
        okHttpClient: OkHttpClient, request: Request, gson: Gson, type: Type
    ): AirQaulityDataSocketImpl {
        return AirQaulityDataSocketImpl(okHttpClient, request, gson, type)
    }
}