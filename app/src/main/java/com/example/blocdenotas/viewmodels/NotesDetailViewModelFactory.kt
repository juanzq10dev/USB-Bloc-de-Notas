package com.example.blocdenotas.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blocdenotas.observer.ConnectivityObserver

class NotesDetailViewModelFactory(val sharedViewModel: NoteShareViewModel, val connectivityObserver: ConnectivityObserver): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesDetailViewModel::class.java)) {
            return NotesDetailViewModel(sharedViewModel, connectivityObserver) as T
        }
        return super.create(modelClass)
    }
}