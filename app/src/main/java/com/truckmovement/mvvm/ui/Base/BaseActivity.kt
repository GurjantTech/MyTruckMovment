package com.truckmovement.mvvm.ui.Base

import android.app.ProgressDialog
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.truckmovement.mvvm.data.local.SessionManager
import com.truckmovement.mvvm.data.remote.RequestInterface
import com.truckmovement.mvvm.utils.showLoadingDialog
import dagger.android.AndroidInjection
import java.util.*
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var requestInterface: RequestInterface

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        var laanguage=sessionManager.getValue(sessionManager.laguagePef,"")
        val locale = Locale(laanguage)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    fun hideProgress() {
        progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    fun showProgress() {
        progressDialog = showLoadingDialog(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
        }*/
        hideKeyboard(this, findViewById<View>(android.R.id.content).windowToken)
        return super.dispatchTouchEvent(ev)
    }

    fun hideKeyboard(context: Context, windowToken: IBinder) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

}
