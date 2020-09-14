package com.truckmovement.mvvm.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RegisterViewModelFactory(private var activity: RegisterActivity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}