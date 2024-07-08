package com.example.blocdenotas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.blocdenotas.room.models.Note
import com.example.blocdenotas.room.repositories.NoteRepository

class NoteShareViewModel(val repository: NoteRepository): ViewModel() {
    var selectedNote: Note? = null
    val notes = repository.notes

    fun selectNote(note: Note) {
        selectedNote = note
    }

}