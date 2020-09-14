package com.truckmovement.mvvm.ui.home

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.truckmovement.mvvm.R
import com.truckmovement.mvvm.ui.Base.BaseActivity
import com.truckmovement.mvvm.ui.LogIn.LoginActivity
import com.truckmovement.mvvm.ui.awb.AWBActivity
import com.truckmovement.mvvm.ui.home.adapter.HomeAdapter
import com.truckmovement.mvvm.ui.home.viewmodel.HomeActivityViewModel
import com.truckmovement.mvvm.ui.home.viewmodelfactory.HomeViewModelFactory
import com.truckmovement.mvvm.ui.loadinginformation.LoadingInformationFragment
import com.truckmovement.mvvm.ui.truckdetail.TruckDetailFragment
import com.truckmovement.mvvm.utils.GpsTracker
import com.truckmovement.mvvm.utils.userCurrentLang
import com.truckmovement.mvvm.utils.userCurrentLat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.truckmovement.mvvm.ui.privacy.PrivacyPolicyActivity1
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.mailpopup_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : BaseActivity() {
    var fieldNameList = ArrayList<String>()
    var iconList = ArrayList<Int>()
    lateinit var gpstracker: GpsTracker

    lateinit var viewModel: HomeActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        viewModel = ViewModelProviders.of(this, HomeViewModelFactory(this)).get(HomeActivityViewModel::class.java)
        observer()
        fieldNameList = ArrayList()
        fieldNameList.add(getString(R.string.truckDetail))
        fieldNameList.add(getString(R.string.loading_information))
        fieldNameList.add("AWB/ULD")
        iconList = ArrayList()
        iconList.add(R.drawable.truck_detail_icon)
        iconList.add(R.drawable.loading_icon)
        iconList.add(R.drawable.awd_uld_icon)
        gpstracker = GpsTracker(this)
        locationPermission(this, gpstracker)

        var adapter = HomeAdapter(this, fieldNameList, iconList)
        home_option_rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        home_option_rv.adapter = adapter
        backBtn.setOnClickListener {
            onBackPressed()
        }
        logoutBtn.setOnClickListener {
            viewModel.logoutApi()
//            logoutWithoutApi()

        }
        contactBtn.setOnClickListener {
            loadContactForm()
        }
        privacyBtn.setOnClickListener {
            var intent=Intent(this,PrivacyPolicyActivity1::class.java)
            startActivity(intent)
        }
        changeLanguagebtn.setOnClickListener {
            val mDialog: Dialog
            mDialog = Dialog(this)
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mDialog.setContentView(R.layout.mailpopup_layout)
            viewModel.selectedLanguage(mDialog)
            mDialog.englishSelcted.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    if (mDialog.englishSelcted.isChecked) {
                        sessionManager.setValue(sessionManager.laguagePef, "")
                        val locale = Locale(sessionManager.getValue(sessionManager.laguagePef, ""))
                        Locale.setDefault(locale)
                        val config = Configuration()
                        config.locale = locale
                        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
                        this@HomeActivity.recreate()

                    }
                }
            })
            mDialog.germanBtn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    sessionManager.setValue(sessionManager.laguagePef, "gr")
                    val locale = Locale(sessionManager.getValue(sessionManager.laguagePef, ""))
                    Locale.setDefault(locale)
                    val config = Configuration()
                    config.locale = locale
                    baseContext.resources.updateConfiguration(
                        config,
                        baseContext.resources.displayMetrics
                    )
                    this@HomeActivity.recreate()

                }
            })
            mDialog.polnischSelected.setOnClickListener {
                if (mDialog.polnischSelected.isChecked) {
                    sessionManager.setValue(sessionManager.laguagePef, "pl")
                    val locale = Locale(sessionManager.getValue(sessionManager.laguagePef, ""))
                    Locale.setDefault(locale)
                    val config = Configuration()
                    config.locale = locale
                    baseContext.resources.updateConfiguration(
                        config,
                        baseContext.resources.displayMetrics
                    )
                    this@HomeActivity.recreate()
                }
            }
            mDialog.turkishSelected.setOnClickListener {
                if (mDialog.turkishSelected.isChecked) {

                    sessionManager.setValue(sessionManager.laguagePef, "tr")
                    val locale = Locale(sessionManager.getValue(sessionManager.laguagePef, ""))
                    Locale.setDefault(locale)
                    val config = Configuration()
                    config.locale = locale
                    baseContext.resources.updateConfiguration(
                        config,
                        baseContext.resources.displayMetrics
                    )
                    this@HomeActivity.recreate()
                }
            }
            mDialog.tschischSelected.setOnClickListener {
                if (mDialog.tschischSelected.isChecked) {
                    sessionManager.setValue(sessionManager.laguagePef, "cs")
                    val locale = Locale(sessionManager.getValue(sessionManager.laguagePef, ""))
                    Locale.setDefault(locale)
                    val config = Configuration()
                    config.locale = locale
                    baseContext.resources.updateConfiguration(
                        config,
                        baseContext.resources.displayMetrics
                    )
                    this@HomeActivity.recreate()
                }
            }
            mDialog.romanianSelected.setOnClickListener {
                sessionManager.setValue(sessionManager.laguagePef, "ro")
                val locale = Locale(sessionManager.getValue(sessionManager.laguagePef, ""))
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
                baseContext.resources.updateConfiguration(
                    config,
                    baseContext.resources.displayMetrics
                )
                this@HomeActivity.recreate()
            }

            mDialog.show()

        }
    }

    private fun logoutWithoutApi() {
        var currentlanguage = sessionManager.getValue(sessionManager.laguagePef, "")
        sessionManager.logOutUser()
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("finish", true)
        startActivity(intent)
        sessionManager.setValue(sessionManager.laguagePef, currentlanguage)
        finish()
    }


    private fun observer() {
        viewModel.logoutApiResponse.observeForever {
            if (it.isLoggedIn == "false") {
                var currentlanguage = sessionManager.getValue(sessionManager.laguagePef, "")
                sessionManager.logOutUser()
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("finish", true)
                startActivity(intent)
                sessionManager.setValue(sessionManager.laguagePef, currentlanguage)
                finish()
            }
        }
    }


    fun locationPermission(activity: Activity, gpsTracker: GpsTracker) {
        Dexter.withActivity(activity).withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {

                        if (gpsTracker.canGetLocation()) {
                            val latitude = gpsTracker.getLatitude()
                            val longitude = gpsTracker.getLongitude()
                            userCurrentLat = latitude
                            userCurrentLang = longitude
                        } else {
                            gpsTracker.showSettingsAlert()
                        }
                    } else {
                        Toast.makeText(activity, "Unable to find location.", Toast.LENGTH_SHORT)
                            .show()
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

    private fun loadContactForm() {
        viewVisibiliy()
        tittle_txt.text = getString(R.string.contact)
        var fragment = ContactFormFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        fragmentTransaction.replace(R.id.frame, fragment, ContactFormFragment::class.java.name)
        fragmentTransaction.commitAllowingStateLoss()
        fragmentTransaction.addToBackStack(null)
    }

    // Load Truck Detail Fragment
    fun loadTruckDetailFragment() {
        viewVisibiliy()
        tittle_txt.text = getString(R.string.truckDetail)
        var fragment = TruckDetailFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        fragmentTransaction.replace(R.id.frame, fragment, TruckDetailFragment::class.java.name)
        fragmentTransaction.commitAllowingStateLoss()
        fragmentTransaction.addToBackStack(null)
    }

    private fun viewVisibiliy() {
        frame.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE
        backBtn.visibility = View.VISIBLE
        home_layout.visibility = View.GONE

    }

    // Load LoadingInformation Fragment
    fun loadLoadingInformationFragment() {
        viewVisibiliy()
        tittle_txt.text = getString(R.string.loading_informationtitle)
        var fragment = LoadingInformationFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        fragmentTransaction.replace(
            R.id.frame,
            fragment,
            LoadingInformationFragment::class.java.name
        )
        fragmentTransaction.commitAllowingStateLoss()
        fragmentTransaction.addToBackStack(null)
    }

    fun loadAWBFragment() {
        var intent = Intent(this, AWBActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.backStackEntryCount
        if (fragments == 1) {
            updateToolbaar()

            super.onBackPressed()
        } else if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }

    }

    fun updateToolbaar() {
        home_layout.visibility = View.VISIBLE
        backBtn.visibility = View.VISIBLE
        toolbar.visibility = View.GONE
        frame.visibility = View.GONE
    }

}
