package com.truckmovement.mvvm.ui.Base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.truckmovement.mvvm.data.local.SessionManager
import com.truckmovement.mvvm.data.remote.RequestInterface
import com.truckmovement.mvvm.utils.showLoadingDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


open class BaseFragment : Fragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var requestInterface: RequestInterface


    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onAttach(activity: Activity) {
        AndroidSupportInjection.inject(this)
        super.onAttach(activity)
    }



    fun hideProgress() {
        progressDialog?.let {
            if (it.isShowing) it.cancel()
        }
    }

    fun showProgress() {
        progressDialog = showLoadingDialog(activity)
    }
}