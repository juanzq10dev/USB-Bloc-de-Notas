package com.example.blocdenotas.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("notes")
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo
    val title: String,

    @ColumnInfo
    val description: String

/*
data class Note(val date: String, val latitude: Int, val longitude: Int,
                val userId: String, val id: String, var body: String, var title: String) {
}
 */
)