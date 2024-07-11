package com.example.blocdenotas.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("notes")
data class Note (
    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @ColumnInfo()
    @SerializedName("fecha")
    val date: String,
    @ColumnInfo
    @SerializedName("latitud")
    val latitude: Double,

    @ColumnInfo
    @SerializedName("longitud")
    val longitude: Double,

    @ColumnInfo
    @SerializedName("user_id")
    val userId: String,

    @ColumnInfo
    @SerializedName("titulo")
    var title: String,

    @ColumnInfo
    @SerializedName("body")
    var description: String

/*
data class Note(val date: String, val latitude: Int, val longitude: Int,
                val userId: String, val id: String, var body: String, var title: String) {
}
 */
)