package com.truckmovement.mvvm.ui.awb

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darsh.multipleimageselect.helpers.Constants
import com.darsh.multipleimageselect.models.Image
import com.truckmovement.mvvm.R
import com.truckmovement.mvvm.ui.Base.BaseActivity
import com.truckmovement.mvvm.ui.awb.adapter.AttachmentAdapter
import com.truckmovement.mvvm.ui.awb.viewmodel.AWBviewmodel
import com.truckmovement.mvvm.ui.awb.viewmodelfactory.AWBViewModelFactory
import com.truckmovement.mvvm.utils.showToast
import com.google.zxing.integration.android.IntentIntegrator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_a_w_b.*


class AWBActivity : BaseActivity(), View.OnClickListener {
    lateinit var qrscan: IntentIntegrator  ////qr code scanner object
    lateinit var viewmodel: AWBviewmodel
    lateinit var galleryImagesList: ArrayList<Uri>

    var qrcodetext=""
    var attachmentcount=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a_w_b)
        qrscan = IntentIntegrator(this)
        qrscan.setOrientationLocked(false)
        // By Default Orientation Lock is true For Landscape Mode
        viewmodel =
            ViewModelProviders.of(this, AWBViewModelFactory(this)).get(AWBviewmodel::class.java)
        observer()
        galleryImagesList = ArrayList()
        scannerBtn.setOnClickListener(this)
        backBtn.setOnClickListener(this)
        selectImages.setOnClickListener(this)
        sendBtn.setOnClickListener(this)





    }

    private fun observer() {
        viewmodel.mailApiResponce.observeForever {
            if (it.status == "1") {
                showToast(this, this.getString(R.string.datasentsuccessfully))
                finish()
            }
        }
    }

    private fun getPermission() {
        Dexter.withActivity(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        viewmodel.getImagesFromGallery()
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permenantly, navigate user to app settings
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                qrcodetext=result.contents
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == Constants.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val images: ArrayList<Image> =
                data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES)!!
            //The array list has the image paths of the selected images
            galleryImagesList.clear()
            for (i in images.indices) {
                galleryImagesList.add(Uri.parse(images[i].path))
            }
            setAdapter()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setAdapter() {
        attachmentcount=galleryImagesList.size.toString()
        var attachmentAdapter = AttachmentAdapter(this, galleryImagesList)
        attachment_rv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        attachment_rv.adapter = attachmentAdapter
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.backBtn -> finish()
            R.id.scannerBtn -> qrscan.initiateScan()
            R.id.selectImages -> getPermission()
            R.id.sendBtn -> if(viewmodel.validation())if(viewmodel.termsConditionValidation()) viewmodel.sendmailData()
        }
    }


}
