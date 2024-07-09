package com.example.blocdenotas.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotesDetailViewModelFactory(val sharedViewModel: NoteShareViewModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesDetailViewModel::class.java)) {
            return NotesDetailViewModel(sharedViewModel) as T
        }
        return super.create(modelClass)
    }
}