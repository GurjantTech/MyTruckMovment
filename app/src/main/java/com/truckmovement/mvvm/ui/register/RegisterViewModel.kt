package com.truckmovement.mvvm.ui.register

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truckmovement.mvvm.R
import com.truckmovement.mvvm.ui.register.model.RegisterApiResponceModel
import com.truckmovement.mvvm.utils.isValidEmail
import com.truckmovement.mvvm.utils.showToast
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject


class RegisterViewModel(private var activity: RegisterActivity) : ViewModel() {
    var userCountNumber = ArrayList<String>()
    var numberOFUser = ""

    var jsonobject = JSONObject()
    var registerResponce: MutableLiveData<RegisterApiResponceModel> = MutableLiveData()


    fun userRegisterApi(body: JSONObject) {
        activity.showProgress()
        activity.requestInterface.userRegisterApi(body.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onResponse, this::onError)
    }


    private fun onResponse(loginModel: RegisterApiResponceModel) {
        activity.hideProgress()
        showToast(activity, loginModel.companyName)
        Log.e("Success", Gson().toJson(loginModel))
        registerResponce.value = loginModel
    }

    private fun onError(error: Throwable) {
        activity.hideProgress()
        Log.e("error", error.message.toString())
        error.printStackTrace()
        showToast(activity, error.message.toString())
//        activity.showFailedLogin()
    }


    fun intialize() {
        userCountNumber = ArrayList()
        userCountNumber.clear()
        userCountNumber.add(activity.getString(R.string.numberOfUser))
        for (i in 1..100) {
            userCountNumber.add(i.toString())
        }
        val arrayAdapter =
            ArrayAdapter(activity, android.R.layout.simple_spinner_item, userCountNumber)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activity.usersCount_sp.setAdapter(arrayAdapter)
        activity.usersCount_sp.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                numberOFUser = parentView!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        })
    }

    fun tcValidation(): Boolean {
        if (activity.termsConditionBtn.isChecked) {
            return true
        } else {
            showToast(activity, activity.getString(R.string.tc_notselect))
            return false
        }
    }

    fun saveUserDetail() {

        var user_name = activity.et_username.text.toString()
        var user_pass = activity.et_password.text.toString()
        var user_con_pass = activity.et_confirmpassword.text.toString()
        var et_mail = activity.et_mail.text.toString()
        var et_company_name = activity.et_company_name.text.toString()
        var et_phnnumber = activity.et_phnnumber.text.toString()

        if (et_mail != "" && et_company_name != "" && ((numberOFUser != "Anzahl der Benutzer") && (numberOFUser != "Number Of User"))) {
            if (isValidEmail(et_mail)) {
                jsonobject.put("CompanyName", et_company_name)
                jsonobject.put("Email", et_mail)
                jsonobject.put("Telephone", et_phnnumber)
                jsonobject.put("UserCount", numberOFUser.toInt())
                Log.e("jsonobject::", jsonobject.toString())
                userRegisterApi(jsonobject)
            } else {
                showToast(activity, activity.getString(R.string.wrong_email))
            }
        } else {
            showToast(activity, activity.getString(R.string.emptyfilled))

        }


    }

}