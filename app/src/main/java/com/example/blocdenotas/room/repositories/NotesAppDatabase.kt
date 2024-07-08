package com.example.blocdenotas.room.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.blocdenotas.room.dao.NoteDao
import com.example.blocdenotas.room.models.Note

@Database(entities = [Note::class], version = 1)
abstract class NotesAppDatabase: RoomDatabase() {
    abstract val notesDao: NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NotesAppDatabase? = null
        fun getInstance(context: Context): NotesAppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, NotesAppDatabase::class.java, "notes_db").build()
                }

                INSTANCE = instance
                return instance
            }
        }
    }
}