package com.truckmovement.mvvm.ui.home.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truckmovement.mvvm.ui.home.HomeActivity
import com.truckmovement.mvvm.ui.home.viewmodel.HomeActivityViewModel

class HomeViewModelFactory(private val homeActivity: HomeActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeActivityViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return HomeActivityViewModel(homeActivity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}