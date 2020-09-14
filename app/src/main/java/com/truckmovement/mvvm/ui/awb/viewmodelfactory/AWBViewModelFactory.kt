package com.truckmovement.mvvm.ui.awb.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truckmovement.mvvm.ui.awb.AWBActivity
import com.truckmovement.mvvm.ui.awb.viewmodel.AWBviewmodel

class AWBViewModelFactory (private var activity:AWBActivity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AWBviewmodel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return AWBviewmodel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}