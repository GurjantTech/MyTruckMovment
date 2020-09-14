package com.truckmovement.mvvm.ui.LogIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory(private val loginActivity: LoginActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(loginActivity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}