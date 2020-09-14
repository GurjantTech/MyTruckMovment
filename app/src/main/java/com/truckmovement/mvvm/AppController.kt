package com.truckmovement.mvvm


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.truckmovement.mvvm.data.local.SessionManager
import com.truckmovement.mvvm.di.component.DaggerAppComponent

import dagger.android.*
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.mailpopup_layout.*

import javax.inject.Inject

class AppController : MultiDexApplication(), HasActivityInjector, HasSupportFragmentInjector,
    HasServiceInjector {

    @Inject
    lateinit var fragment: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>

    override fun serviceInjector(): AndroidInjector<Service> {
        return serviceInjector
    }

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }


    override fun onCreate() {
        DaggerAppComponent.builder().application(this).build().inject(this)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(p0: Activity) {

            }

            override fun onActivityStarted(p0: Activity) {

            }

            override fun onActivityDestroyed(p0: Activity) {

            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

            }

            override fun onActivityStopped(p0: Activity) {

            }

            @SuppressLint("SourceLockedOrientationActivity")
            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle?
            ) {
                // new activity created; force its orientation to portrait
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }

            override fun onActivityResumed(p0: Activity) {

            }


        })

        super.onCreate()


        //From Here you can set app language for english empty and for german put gr
        var selctedLanguage = sessionManager.getValue(sessionManager.laguagePef, "")
        if (selctedLanguage!!.equals("pl")) {
            sessionManager.setValue(sessionManager.laguagePef,"pl")
        } else if (selctedLanguage!!.equals("gr")) {
            sessionManager.setValue(sessionManager.laguagePef,"gr")
        } else if (selctedLanguage!!.equals("cs")) {
            sessionManager.setValue(sessionManager.laguagePef,"cs")
        } else if (selctedLanguage!!.equals("tr")) {
            sessionManager.setValue(sessionManager.laguagePef,"tr")
        } else if(selctedLanguage!!.equals("ro")){
            sessionManager.setValue(sessionManager.laguagePef,"ro")
        }else {
            sessionManager.setValue(sessionManager.laguagePef,"")
        }

    }

    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment> {
        return fragment
    }


}