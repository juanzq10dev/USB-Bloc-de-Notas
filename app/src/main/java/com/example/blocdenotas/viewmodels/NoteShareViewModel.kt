package com.example.blocdenotas.viewmodels

import android.hardware.biometrics.BiometricManager.Strings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blocdenotas.retrofit.entity.LoginPost
import com.example.blocdenotas.room.models.Note
import com.example.blocdenotas.room.repositories.NoteRepository
import kotlinx.coroutines.launch

class NoteShareViewModel(val repository: NoteRepository): ViewModel() {
    var selectedNote: Note? = null
    val notes = repository.notes

    fun selectNote(note: Note) {
        selectedNote = note
    }

    fun getAccessToken() = repository.getToken()

    fun getAllContacts() = viewModelScope.launch {
        repository.getAll().collect { result ->
            if (!result) {
                // Error message goes gere
            }
        }
    }

    fun saveToken(token: String) = viewModelScope.launch {
        repository.saveToken(token)
    }

    fun removeToken() = viewModelScope.launch {
        repository.saveToken("")
        repository.deleteAll()
    }

    fun login(loginPost: LoginPost) = viewModelScope.launch {
        repository.login(loginPost)
    }

}