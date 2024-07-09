package com.example.blocdenotas.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blocdenotas.room.models.Note
import kotlinx.coroutines.launch

class NotesDetailViewModel(val notesShareViewModel: NoteShareViewModel): ViewModel() {
    val repository = notesShareViewModel.repository
    var notesTitle = MutableLiveData<String>()
    var notesDescription = MutableLiveData<String>()
    var isValid = MediatorLiveData<Boolean>()

    init {
        isValid.addSource(notesTitle) {
            isValid.value = checkIfValid()
        }
        isValid.addSource(notesDescription) {
            isValid.value = checkIfValid()
        }
    }

    fun updateTexts() {
        notesTitle.value = notesShareViewModel.selectedNote?.title
        notesDescription.value = notesShareViewModel.selectedNote?.description
    }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun save() {
        if (notesShareViewModel.selectedNote == null) {
            if (!(notesTitle.value).isNullOrBlank() && !(notesDescription.value).isNullOrBlank()) {
                insert(Note(0, notesTitle.value!!, notesDescription.value!!))
                notesTitle.value = ""
                notesDescription.value = ""
            }
        } else {
            if (!(notesTitle.value).isNullOrBlank() && !(notesDescription.value).isNullOrBlank()) {
                notesShareViewModel.selectedNote?.title = notesTitle.value!!
                notesShareViewModel.selectedNote?.description = notesDescription.value!!
                update(notesShareViewModel.selectedNote!!)
                notesTitle.value = ""
                notesDescription.value = ""
            }
        }
    }

    private fun checkIfValid() = !(notesTitle.value).isNullOrBlank()
            && !(notesDescription.value).isNullOrBlank()
}