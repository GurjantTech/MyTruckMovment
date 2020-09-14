package com.truckmovement.mvvm.ui.LogIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import com.google.gson.Gson
import com.truckmovement.mvvm.ui.LogIn.Model.LoginModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private var loginActivity: LoginActivity): ViewModel() {
    var loginApiResponse: MutableLiveData<LoginModel> = MutableLiveData()

    fun loginApi(username: String, password: String) {

        loginActivity.showProgress()
        loginActivity.requestInterface.userLoginApi(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onResponse, this::onError)
    }
    private fun onResponse(loginModel: LoginModel) {
        loginActivity.hideProgress()
        Log.e("Success", Gson().toJson(loginModel))
        loginApiResponse.value = loginModel
        loginActivity.loginApiResponce(loginModel)
    }

    private fun onError(error: Throwable) {
        loginActivity.hideProgress()
        Log.e("error", error.message.toString())
        error.printStackTrace()

    }
}