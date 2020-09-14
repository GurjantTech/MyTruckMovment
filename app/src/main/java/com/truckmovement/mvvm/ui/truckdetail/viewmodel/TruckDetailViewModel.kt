package com.truckmovement.mvvm.ui.truckdetail.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truckmovement.mvvm.R
import com.truckmovement.mvvm.ui.truckdetail.TruckDetailFragment
import com.truckmovement.mvvm.ui.truckdetail.model.SendMailResponceModel
import com.truckmovement.mvvm.utils.isValidEmail
import com.truckmovement.mvvm.utils.showToast
import com.truckmovement.mvvm.utils.userCurrentLang
import com.truckmovement.mvvm.utils.userCurrentLat
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_truck_detail.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class TruckDetailViewModel(private var fragment: TruckDetailFragment) : ViewModel() {
    var sendMailApiResponse: MutableLiveData<SendMailResponceModel> = MutableLiveData()

    fun sendMail(body: String, formName: String) {
        fragment.showProgress()
        val body = RequestBody.create(MediaType.parse("text/plain"), body)
        var email = RequestBody.create(
            MediaType.parse("text/plain"),
            fragment.sessionManager.getValue(fragment.sessionManager.usermail, "")
        )
        var formname = RequestBody.create(MediaType.parse("text/plain"), formName)
        var file = RequestBody.create(MediaType.parse("text/plain"), "")
        var fromfileList: ArrayList<MultipartBody.Part> = ArrayList()
        fragment.requestInterface.sendMailOnServer(email, formname, body, fromfileList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onResponse, this::onError)
    }

    private fun onResponse(loginModel: SendMailResponceModel) {
        fragment.hideProgress()
        Log.e("Success", Gson().toJson(loginModel))
        sendMailApiResponse.value = loginModel
    }

    private fun onError(error: Throwable) {
        fragment.hideProgress()
        Log.e("error", error.message.toString())
        error.printStackTrace()

    }


    fun sendEmail() {
        var truckNo = fragment.et_truck_number.text.toString()
        var orderNumber = fragment.et_ordernumber.text.toString()
        var dispo_email = fragment.dispo_email.text.toString()
        var customer_email = fragment.customer_email.text.toString()
        if (fragment.shift_start.isChecked) {
            fragment.Schichtbeginn = "Yes"
        } else {
            fragment.Schichtbeginn = "No"
        }
        if (fragment.shift_end.isChecked) {
            fragment.Schichtende = "Yes"
        } else {
            fragment.Schichtende = "No"
        }
        if (fragment.braekstart.isChecked) {
            fragment.Pause = "Yes"
        } else {
            fragment.Pause = "No"
        }
        if (fragment.break_end.isChecked) {
            fragment.Pausenende = "Yes"
        } else {
            fragment.Pausenende = "No"
        }

        println("fragment.departure== " + fragment.departure)
        println("fragment.arrival== " + fragment.arrival)
        var body =
            "Truck Number : " + truckNo + " & Order Number : " + orderNumber + " & Dispo-Email : " +
                    dispo_email + " & Customer-Email : " + customer_email +
                    " & Departure : " + fragment.arrival + " & Arrival : " + fragment.departure +
                    " & Start of shift : " + fragment.Schichtbeginn + " & End of shift : " + fragment.Schichtende + " & Break : " + fragment.Pause + " & End of break : " + fragment.Pausenende /*+ " & Loading Station : " + fragment.departure*/ + " & Lat : " + userCurrentLat + " & Lng : " + userCurrentLang

        var formName = "TRUCK DETAIL"
        sendMail(body, formName)

    }

    fun termsAndCondtions(): Boolean {
        if (fragment.termsConditionBtn.isChecked) {
            return true
        } else {
            showToast(fragment.requireContext(), fragment.getString(R.string.tc_notselect))
            return false
        }
    }

    fun validation(): Boolean {
        if (fragment.et_truck_number.text.toString() != "" &&
            fragment.et_ordernumber.text.toString() != ""
            && fragment.dispo_email.text.toString() != ""
            && fragment.customer_email.text.toString() != ""
            && !fragment.departure.equals(fragment.context!!.getString(R.string.arrival))
            && !fragment.arrival.equals(fragment.context!!.getString(R.string.departure))) {
            if (isValidEmail(fragment.dispo_email.text.toString())) {
                if (isValidEmail(fragment.customer_email.text.toString())) {
                    return true
                } else {
                    showToast(fragment.context!!, fragment.getString(R.string.invalid_customerEmail))
                    return false
                }

            } else {
                showToast(fragment.context!!, fragment.getString(R.string.invalid_dispo_email))
                return false
            }

        } else {
            showToast(fragment.context!!, fragment.getString(R.string.emptyfilled))
            return false
        }
    }

}