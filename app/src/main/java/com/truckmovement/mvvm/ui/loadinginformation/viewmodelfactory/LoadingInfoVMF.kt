package com.truckmovement.mvvm.ui.loadinginformation.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truckmovement.mvvm.ui.loadinginformation.LoadingInformationFragment
import com.truckmovement.mvvm.ui.loadinginformation.viewmodel.LodingInfoVM

class LoadingInfoVMF(private var fragment: LoadingInformationFragment): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LodingInfoVM::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return LodingInfoVM(fragment) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}