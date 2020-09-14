package com.truckmovement.mvvm.di.module

import android.content.Context
import android.os.Bundle
import com.truckmovement.mvvm.AppController
import com.truckmovement.mvvm.data.local.SessionManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideApplication(app:AppController):Context=app

    @Provides
    @Singleton
    fun provideSessionManagerModule(context: Context):SessionManager{
        return SessionManager(context.getSharedPreferences("SharedPref", Context.MODE_PRIVATE))
    }

    @Provides
    @Singleton
    fun provideBundleModule(): Bundle {
        return Bundle()
    }
}