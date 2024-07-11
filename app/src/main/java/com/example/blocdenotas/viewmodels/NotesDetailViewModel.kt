package com.example.blocdenotas.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blocdenotas.observer.ConnectivityObserver
import com.example.blocdenotas.retrofit.entity.NoteDelete
import com.example.blocdenotas.room.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class NotesDetailViewModel(val notesShareViewModel: NoteShareViewModel, connectivityObserver: ConnectivityObserver): ViewModel() {
    val repository = notesShareViewModel.repository
    var notesTitle = MutableLiveData<String>()
    var notesDescription = MutableLiveData<String>()
    var connecObserver = connectivityObserver
    var isValid = MediatorLiveData<Boolean>()

    init {
        isValid.addSource(notesTitle) {
            checkFowInternet()
        }
        isValid.addSource(notesDescription) {
            checkFowInternet()
        }
    }

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

    fun updateTexts() {
        notesTitle.value = notesShareViewModel.selectedNote?.title
        notesDescription.value = notesShareViewModel.selectedNote?.description
    }

    fun deleteAll()= viewModelScope.launch {
        repository.deleteAll()
    }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insertToApi(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.updateToApi(note)
    }

    fun delete(noteDelete: NoteDelete) = viewModelScope.launch {
        repository.deleteToApi(noteDelete)
    }

    fun save() {
        var userId = ""
        viewModelScope.launch(Dispatchers.Main) {
            notesShareViewModel.getAccessToken().collect {
                userId = it.token
                if (notesShareViewModel.selectedNote == null) {
                    if (!(notesTitle.value).isNullOrBlank() && !(notesDescription.value).isNullOrBlank()) {
                        insert(Note(
                            "",
                            LocalDate.now().toString(),
                            0.1,
                            0.1,
                            userId,
                            notesTitle.value!!,
                            notesDescription.value!!,
                        ))
                        notesTitle.value = ""
                        notesDescription.value = ""
                    }
                } else {
                    if (!(notesTitle.value).isNullOrBlank() && !(notesDescription.value).isNullOrBlank()) {
                        notesShareViewModel.selectedNote?.title = notesTitle.value!!
                        notesShareViewModel.selectedNote?.description = notesDescription.value!!
                        update(
                            notesShareViewModel.selectedNote!!
                        )
                        notesTitle.value = ""
                        notesDescription.value = ""
                        /*
                                        notesShareViewModel.selectedNote?.title = notesTitle.value!!
                        notesShareViewModel.selectedNote?.description = notesDescription.value!!
                        update(notesShareViewModel.selectedNote!!)
                        notesTitle.value = ""
                        notesDescription.value = ""
                         */
                    }
                }

            }
        }

    }

    fun remove() {
        if (notesShareViewModel.selectedNote != null) {
            delete(NoteDelete(
                notesShareViewModel.selectedNote!!.id
            ))
        }
    }

    private fun checkIfValid() = !(notesTitle.value).isNullOrBlank()
            && !(notesDescription.value).isNullOrBlank()
}