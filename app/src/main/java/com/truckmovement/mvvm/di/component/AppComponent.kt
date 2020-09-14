package com.truckmovement.mvvm.di.component

import com.truckmovement.mvvm.AppController
import com.truckmovement.mvvm.di.builder.ActivityBuilder
import com.truckmovement.mvvm.di.module.AppModule
import com.truckmovement.mvvm.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(AppModule::class, AndroidInjectionModule::class,
    NetworkModule::class, ActivityBuilder::class))

interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: AppController): Builder
        fun build(): AppComponent
    }

    fun inject(app: AppController)

    //fun inject(postListViewModel: LoginViewModel)
    //fun inject(aboutMeViewModel: AboutMeViewModel)

}