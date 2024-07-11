package com.example.blocdenotas.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blocdenotas.observer.ConnectivityObserver

class LoginViewModelFactory(val connectivityObserver: ConnectivityObserver ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(connectivityObserver) as T
        }
        return super.create(modelClass)
    }
}