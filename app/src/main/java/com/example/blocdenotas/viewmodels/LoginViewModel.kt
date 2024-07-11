package com.example.blocdenotas.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.blocdenotas.observer.ConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(connectivityObserver: ConnectivityObserver): ViewModel() {
    var userEmail = MutableLiveData<String>()
    var userPassword = MutableLiveData<String>()
    var isValid = MediatorLiveData<Boolean>()
    var connecObserver = connectivityObserver

    init {
        isValid.addSource(userEmail) {
            checkFowInternet()
        }

        isValid.addSource(userPassword) {
            checkFowInternet()
        }
    }

    private fun checkIfValid() = !(userEmail.value).isNullOrBlank() &&
            !(userPassword.value).isNullOrBlank()

    private fun checkFowInternet() = viewModelScope.launch {
        connecObserver.observe().collect {
                if (it != ConnectivityObserver.InternetStatus.Available ) {
                    withContext(Dispatchers.Main) {
                       isValid.value = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        isValid.value = checkIfValid() && true
                    }
                }
            }
    }
}
