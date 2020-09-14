package com.truckmovement.mvvm.ui.privacy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.truckmovement.mvvm.R
import kotlinx.android.synthetic.main.activity_privacy_policy1.*

class PrivacyPolicyActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy1)
        backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}