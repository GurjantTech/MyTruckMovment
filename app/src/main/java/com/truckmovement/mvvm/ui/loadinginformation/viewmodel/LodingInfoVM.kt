package com.truckmovement.mvvm.ui.loadinginformation.viewmodel

import android.R
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truckmovement.mvvm.ui.loadinginformation.LoadingInformationFragment
import com.truckmovement.mvvm.ui.truckdetail.model.SendMailResponceModel
import com.truckmovement.mvvm.utils.*
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_loading_information.*
import kotlinx.android.synthetic.main.fragment_loading_information.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


class LodingInfoVM(private var fragment: LoadingInformationFragment) : ViewModel() {
    var mailApiResponce: MutableLiveData<SendMailResponceModel> = MutableLiveData()
    var idnumber = ""

    fun sendMailApi(body: String, formName: String) {
        fragment.showProgress()

        val body = RequestBody.create(MediaType.parse("text/plain"), body)
        var email = RequestBody.create(
            MediaType.parse("text/plain"),
            fragment.sessionManager.getValue(fragment.sessionManager.usermail, "")
        )
        var formname = RequestBody.create(MediaType.parse("text/plain"), formName)
        var fromfileList: ArrayList<MultipartBody.Part> = ArrayList()

        fragment.requestInterface.sendMailOnServer(email, formname, body, fromfileList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onResponse, this::onError)
    }

    private fun onResponse(mailresponce: SendMailResponceModel) {
        fragment.hideProgress()
        Log.e("Success", Gson().toJson(mailresponce))
        mailApiResponce.value = mailresponce
    }

    private fun onError(error: Throwable) {
        fragment.hideProgress()
        showToast(fragment.requireContext(), error.message.toString())
        Log.e("error", error.message.toString())
        error.printStackTrace()

    }

    fun setFromSppinerData(view: View) {
        fragment.from_citylist.clear()
        for (i in from_spList.indices) {
            fragment.from_citylist.add(from_spList[i].city)
        }
        fragment.from_citylist.sort()
        fragment.from_citylist.add(0, "Loading Station")
        val arrayAdapter =
            ArrayAdapter(view.context, R.layout.simple_spinner_item, fragment.from_citylist)
        view!!.from_sp.setAdapter(arrayAdapter)
    }

    fun termsAndCondition(): Boolean {
        if (fragment.termsConditionBtn.isChecked) {

            return true
        } else {
            showToast(
                fragment.requireContext(),
                fragment.getString(com.truckmovement.mvvm.R.string.tc_notselect)
            )
            return false
        }
    }

    fun sendMail() {
        if (fragment.shift_start.isChecked) {
            fragment.Register = "Yes"
        } else {
            fragment.Register = "No"
        }
        if (fragment.shift_end.isChecked) {
            fragment.Cancellation = "Yes"
        } else {
            fragment.Cancellation = "No"
        }
        if (fragment.braekstart.isChecked) {
            fragment.Braekstart = "Yes"
        } else {
            fragment.Braekstart = "No"
        }
        if (fragment.break_end.isChecked) {
            fragment.Braekend = "Yes"
        } else {
            fragment.Braekend = "No"
        }
        if (fragment.comp_cargo.isChecked) {
            fragment.Compcargo = "Yes"
        } else {
            fragment.Compcargo = "No"
        }
        if (fragment.departure.isChecked) {
            fragment.Departure = "Yes"
        } else {
            fragment.Departure = "No"
        }
        var formName = "LOADING INFORMATION"
        var data =
            "Loading Station : " + fragment.from + " & Loading Place : " + fragment.to + " & Register : " + fragment.Register + " & Cancellation : " + fragment.Cancellation + " & Start of loading : " + fragment.Braekstart + " & End of loading : " + fragment.Braekend + " & Complete cargo : " + fragment.Compcargo + " & Departure : " + fragment.Departure + " & Lat : " + userCurrentLat + " & Lng : " + userCurrentLang
        if (fragment.from != "Loading Station" && fragment.to != "Loading Place") {
            sendMailApi(data, formName)
        } else {
            showToast(fragment.context!!, "Please Fill All Fields.")
        }


    }

}