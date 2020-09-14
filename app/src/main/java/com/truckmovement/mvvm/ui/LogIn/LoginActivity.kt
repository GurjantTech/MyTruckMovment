package com.truckmovement.mvvm.ui.LogIn

import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.truckmovement.mvvm.R
import com.truckmovement.mvvm.ui.Base.BaseActivity
import com.truckmovement.mvvm.ui.LogIn.Model.LoginModel
import com.truckmovement.mvvm.ui.home.HomeActivity
import com.truckmovement.mvvm.ui.register.RegisterActivity

import com.truckmovement.mvvm.utils.showToast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)
        observer()
        loginBtn.setOnClickListener(this)
        registerBtn.setOnClickListener(this)

    }

    private fun observer() {
        viewModel.loginApiResponse.observeForever {

        }
    }
 fun loginApiResponce(it:LoginModel){
     if (it.userName != null) {
         showToast(this, getString(R.string.login_successfully))
         sessionManager.setValue(sessionManager.username, it.userName)
         sessionManager.setValue(sessionManager.userId, it.userID)
         sessionManager.setValue(sessionManager.password, it.userName)
         sessionManager.setValue(sessionManager.usermail,it.email)
         var intent = Intent(this, HomeActivity::class.java)
         startActivity(intent)
         overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation)
         finish()
     } else {
         showToast(this, it.message)
     }
 }
    @Override
    override fun onClick(v: View) {
        when (v.id) {
            loginBtn.id -> userLogin()
            registerBtn.id -> goToRegisterActivity()
        }
    }

    private fun userLogin() {
        if (et_username.text.toString() != "" && et_password.text.toString() != "") {
            viewModel.loginApi(et_username.text.toString(), et_password.text.toString())
//            withoutApi()
        } else {
            showToast(this, getString(R.string.invalidCredential))
        }

    }

    private fun withoutApi() {
        showToast(this, getString(R.string.login_successfully))
        sessionManager.setValue(sessionManager.username, "test")
        sessionManager.setValue(sessionManager.userId, "25")
        sessionManager.setValue(sessionManager.password,"123456")
        sessionManager.setValue(sessionManager.usermail,"test@gmail.com")
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation)
        finish()
    }

    private fun goToRegisterActivity() {
        var intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)

    }


}
