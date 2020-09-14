package com.truckmovement.mvvm.di.module

import android.content.Context
import com.truckmovement.mvvm.BuildConfig
import com.truckmovement.mvvm.data.remote.RequestInterface
import com.truckmovement.mvvm.utils.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl(): String {
        return BASE_URL
    }

    @Provides
    @Singleton
    fun provideRetrofit(@Named("baseUrl") baseUrl: String, gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: okhttp3.Cache): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val level = getInterceptorLevel()
        httpLoggingInterceptor.level = level

        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(httpLoggingInterceptor).connectTimeout(2,TimeUnit.MINUTES)
//                .addInterceptor(ApiKeyInterceptor())
            .build()

    }

    private fun getInterceptorLevel(): HttpLoggingInterceptor.Level {
        return if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(context: Context): okhttp3.Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        return okhttp3.Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideRequestInterface(retrofit: Retrofit): RequestInterface {
        return retrofit.create(RequestInterface::class.java)
    }
}