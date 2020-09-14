package com.truckmovement.mvvm.ui.awb.viewmodel

import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darsh.multipleimageselect.activities.AlbumSelectActivity
import com.darsh.multipleimageselect.helpers.Constants
import com.truckmovement.mvvm.R
import com.truckmovement.mvvm.ui.awb.AWBActivity
import com.truckmovement.mvvm.ui.truckdetail.model.SendMailResponceModel
import com.truckmovement.mvvm.utils.showToast
import com.truckmovement.mvvm.utils.userCurrentLang
import com.truckmovement.mvvm.utils.userCurrentLat
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_a_w_b.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AWBviewmodel(var activity: AWBActivity) : ViewModel() {
    var mailApiResponce: MutableLiveData<SendMailResponceModel> = MutableLiveData()

    fun sendMailApi(body: String, formName: String) {
        activity.showProgress()
        var fromfileList: ArrayList<MultipartBody.Part> = ArrayList()
        val body = RequestBody.create(MediaType.parse("text/plain"), body)
        var email = RequestBody.create(MediaType.parse("text/plain"), activity.sessionManager.getValue(activity.sessionManager.usermail,""))
//        var email = RequestBody.create(MediaType.parse("text/plain"), clientEmailID)
        var formname = RequestBody.create(MediaType.parse("text/plain"), formName)
        if (activity.galleryImagesList.size > 0) {
            fromfileList.clear()
            for (i in activity.galleryImagesList.indices) {
               var imageReques:MultipartBody.Part=prepareFilePart("formFiles",
                   activity.galleryImagesList[i].path.toString()
               )
             fromfileList.add(imageReques)
            }

        }
        activity.requestInterface.sendMailOnServer(email, formname, body, fromfileList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onResponse, this::onError)
    }

    private fun onResponse(mailresponce: SendMailResponceModel) {
        activity.hideProgress()
        Log.e("Success", Gson().toJson(mailresponce))
        mailApiResponce.value = mailresponce
    }

    private fun onError(error: Throwable) {
        activity.hideProgress()
        showToast(activity, error.message.toString())
        Log.e("error", error.message!!)
        error.printStackTrace()

    }

    private fun prepareFilePart(partName: String, path: String): MultipartBody.Part {
        val file = File(path)
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody)
    }

    fun getImagesFromGallery() {
        val intent = Intent(activity, AlbumSelectActivity::class.java)
//set limit on number of images that can be selected, default is 10
//set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 4)
        activity.startActivityForResult(intent, Constants.REQUEST_CODE)
    }
fun termsConditionValidation():Boolean{
    if(activity.termsConditionBtn.isChecked){
        return true
    }else{
        showToast(activity,activity.getString(R.string.tc_notselect))
       return false

    }
}

    fun sendmailData() {
        var awb_uld = activity.awb_uld_et.text.toString()
        var damage = "No"
        var notfound = "No"
        var nospace = "No"
        var damagereport = "No"
        var pod = "No"
        if (activity.damageCheckbox.isChecked) {
            damage = "Yes"
        } else {
            damage = "No"
        }
        if (activity.notfoundCheckbox.isChecked) {
            notfound = "Yes"
        } else {
            notfound = "No"
        }
        if (activity.nospaceCheckbox.isChecked) {
            nospace = "Yes"
        } else {
            nospace = "No"
        }
        if (activity.damagereportCheckbox.isChecked) {
            damagereport = "Yes"
        } else {
            damagereport = "No"
        }
        if (activity.podCheckbox.isChecked) {
            pod = "Yes"
        } else {
            pod = "No"
        }
        var body = "AWB/ULD Number : " + awb_uld + "& Damage : " + damage + " & Damage Piece : " + activity.damagePiece_et.text + " & Not Found : " + notfound + " & Not Found Piece : " +
                    activity.notfoundPiece_et.text + " & No Space : " + nospace + " & Damage Report : " + damagereport + " & POD : " + pod + "& QR Code : "+ activity.qrcodetext + " & Attachment Count : " + activity.attachmentcount+" & Lat : " + userCurrentLat + " & Lng : "+ userCurrentLang
        var formName = "AWB/ULD"
        sendMailApi(body, formName)

    }
    fun validation():Boolean{
       if( activity.awb_uld_et.text.toString()!="" /*|| activity.damagePiece_et.text.toString()!="" || activity.notfoundPiece_et.text.toString()!=""*/){
         return true
       }else{
           val alertDialog = AlertDialog.Builder(this.activity).create()
           alertDialog.setTitle(this.activity.getString(R.string.app_name))
           alertDialog.setMessage(this.activity.getString(R.string.fill_atlest_awd))
           alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
               object: DialogInterface.OnClickListener{
                  override fun onClick(dialog:DialogInterface, which:Int) {
                       dialog.dismiss()
                   }
               })
           alertDialog.show()
           return false
       }
    }
}