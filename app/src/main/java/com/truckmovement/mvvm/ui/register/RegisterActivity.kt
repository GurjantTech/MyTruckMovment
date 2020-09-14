package com.truckmovement.mvvm.ui.register

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.truckmovement.mvvm.R
import com.truckmovement.mvvm.ui.Base.BaseActivity
import com.truckmovement.mvvm.utils.showToast
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : BaseActivity(), View.OnClickListener {

    lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewModel = ViewModelProviders.of(this, RegisterViewModelFactory(this))
            .get(RegisterViewModel::class.java)
        viewModel.intialize()

        doneBtn.setOnClickListener(this)
        observer()
    }

    private fun observer() {
        viewModel.registerResponce.observeForever {
            if (it.companyName != "") {
                showToast(this, this.getString(R.string.registerationDone))
                finish()
            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            doneBtn.id ->if(viewModel.tcValidation()) viewModel.saveUserDetail()

        }
    }


}
