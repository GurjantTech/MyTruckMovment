package com.truckmovement.mvvm.ui.truckdetail.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truckmovement.mvvm.ui.truckdetail.TruckDetailFragment
import com.truckmovement.mvvm.ui.truckdetail.viewmodel.TruckDetailViewModel

class TruckDetailViewModelFactory (private val fragment: TruckDetailFragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TruckDetailViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return TruckDetailViewModel(fragment) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}