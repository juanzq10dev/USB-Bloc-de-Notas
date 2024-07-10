package com.example.blocdenotas.viewmodels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blocdenotas.room.repositories.DataStoreInstance
import com.example.blocdenotas.room.repositories.NoteRepository
import com.example.blocdenotas.room.repositories.NotesAppDatabase

class NoteShareViewModelFactory(val context: Context, val dataStore: DataStore<Preferences>): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteShareViewModel::class.java)) {
            val database = NotesAppDatabase.getInstance(context)
            val contactDao = database.notesDao
            val contactRepository = NoteRepository(contactDao, dataStore)
            return NoteShareViewModel(contactRepository) as T
        }
        return super.create(modelClass)
    }
}