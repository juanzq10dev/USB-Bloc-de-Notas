package com.example.blocdenotas.room.repositories

import com.example.blocdenotas.room.dao.NoteDao
import com.example.blocdenotas.room.models.Note

class NoteRepository(private val notesDao: NoteDao) {
    val notes = notesDao.getAllNotes()

    suspend fun insert(note: Note) {
        notesDao.insertNote(note)
    }

    suspend fun update(note: Note) {
        notesDao.updateNote(note)
    }
}