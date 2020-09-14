package com.truckmovement.mvvm.di.builder

import com.truckmovement.mvvm.ui.LogIn.LoginActivity
import com.truckmovement.mvvm.ui.SplashActivity
import com.truckmovement.mvvm.ui.awb.AWBActivity
import com.truckmovement.mvvm.ui.home.ContactFormFragment
import com.truckmovement.mvvm.ui.home.HomeActivity
import com.truckmovement.mvvm.ui.loadinginformation.LoadingInformationFragment

import com.truckmovement.mvvm.ui.privacy.PrivacyPolicyActivity1
import com.truckmovement.mvvm.ui.register.RegisterActivity
import com.truckmovement.mvvm.ui.truckdetail.TruckDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract fun splashActivity(): SplashActivity
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): LoginActivity
    @ContributesAndroidInjector
    abstract fun registerActivity(): RegisterActivity
    @ContributesAndroidInjector
    abstract fun homeActivity(): HomeActivity
    @ContributesAndroidInjector
    abstract fun loadingInformationFragment(): LoadingInformationFragment
    @ContributesAndroidInjector
    abstract fun truckDetailFragment(): TruckDetailFragment

    @ContributesAndroidInjector
    abstract fun AWBActivity(): AWBActivity
    @ContributesAndroidInjector
    abstract fun contactFormFragment(): ContactFormFragment

    @ContributesAndroidInjector
    abstract fun privacyPolicyActivity(): PrivacyPolicyActivity1








}