package com.truckmovement.mvvm.ui.home.viewmodel

import android.app.Dialog
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truckmovement.mvvm.ui.home.HomeActivity
import com.truckmovement.mvvm.ui.home.model.LogoutModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.mailpopup_layout.*

class HomeActivityViewModel (private var homeActivity: HomeActivity): ViewModel() {
    var logoutApiResponse: MutableLiveData<LogoutModel> = MutableLiveData()

    fun logoutApi() {
        homeActivity.showProgress()
        homeActivity.requestInterface.logoutApi(homeActivity.sessionManager.getValue(homeActivity.sessionManager.userId,"")!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onResponse, this::onError)
    }
    private fun onResponse(responce: LogoutModel) {
        homeActivity.hideProgress()
        Log.e("Success", Gson().toJson(responce))

        logoutApiResponse.value = responce
    }

    private fun onError(error: Throwable) {
        homeActivity.hideProgress()
        Log.e("error", error.message.toString())
        error.printStackTrace()

    }



     fun selectedLanguage(view: Dialog) {
        var selctedLanguage = homeActivity.sessionManager.getValue(homeActivity.sessionManager.laguagePef, "")

        if (selctedLanguage!!.equals("pl")) {
            view.germanBtn.isChecked = false
            view.englishSelcted.isChecked = false
            view.polnischSelected.isChecked = true
            view.turkishSelected.isChecked = false
            view.tschischSelected.isChecked = false
            view.romanianSelected.isChecked = false

        } else if (selctedLanguage!!.equals("gr")) {
            view.germanBtn.isChecked = true
            view.englishSelcted.isChecked = false
            view.polnischSelected.isChecked = false
            view.turkishSelected.isChecked = false
            view.tschischSelected.isChecked = false
            view.romanianSelected.isChecked = false

        } else if (selctedLanguage!!.equals("cs")) {
            view.germanBtn.isChecked = false
            view.englishSelcted.isChecked = false
            view.polnischSelected.isChecked = false
            view.turkishSelected.isChecked = false
            view.tschischSelected.isChecked = true
            view.romanianSelected.isChecked = false

        } else if (selctedLanguage!!.equals("tr")) {
            view.germanBtn.isChecked = false
            view.englishSelcted.isChecked = false
            view.polnischSelected.isChecked = false
            view.turkishSelected.isChecked = true
            view.tschischSelected.isChecked = false
            view.romanianSelected.isChecked = false

        } else if(selctedLanguage!!.equals("ro")) {
            view.germanBtn.isChecked = false
            view.englishSelcted.isChecked = false
            view.polnischSelected.isChecked = false
            view.turkishSelected.isChecked = false
            view.tschischSelected.isChecked = false
            view.romanianSelected.isChecked = true
        }else {
             view.germanBtn.isChecked = false
             view.englishSelcted.isChecked = true
             view.polnischSelected.isChecked = false
             view.turkishSelected.isChecked = false
             view.tschischSelected.isChecked = false
            view.romanianSelected.isChecked = false
         }

    }
}