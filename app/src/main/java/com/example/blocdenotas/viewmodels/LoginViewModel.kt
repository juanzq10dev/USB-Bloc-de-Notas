package com.example.blocdenotas.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    var userEmail = MutableLiveData<String>()
    var userPassword = MutableLiveData<String>()
    var isValid = MediatorLiveData<Boolean>()

    init {
        isValid.addSource(userEmail) {
            isValid.value = checkIfValid()
        }

        isValid.addSource(userPassword) {
            isValid.value = checkIfValid()
        }
    }

    private fun checkIfValid() = !(userEmail.value).isNullOrBlank() &&
            !(userPassword.value).isNullOrBlank()
}