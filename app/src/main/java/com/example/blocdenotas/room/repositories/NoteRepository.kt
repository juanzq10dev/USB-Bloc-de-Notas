package com.example.blocdenotas.room.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.blocdenotas.room.dao.NoteDao
import com.example.blocdenotas.room.models.AccessToken
import com.example.blocdenotas.room.models.Note
import kotlinx.coroutines.flow.map

class NoteRepository(private val notesDao: NoteDao, private val dataStore: DataStore<Preferences>) {
    val notes = notesDao.getAllNotes()

    suspend fun insert(note: Note) {
        notesDao.insertNote(note)
    }

    suspend fun update(note: Note) {
        notesDao.updateNote(note)
    }


    fun getToken() = dataStore.data.map { preferences ->
        AccessToken(
            token = preferences[stringPreferencesKey("accessToken")].orEmpty()
        )
    }

    suspend fun saveToken( accessToken: String ) {
        dataStore.edit { preference ->
            preference[stringPreferencesKey("accessToken")] = accessToken
        }
    }
}