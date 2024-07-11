package com.example.blocdenotas.api

import com.example.blocdenotas.room.models.Note
import retrofit2.Response
import retrofit2.http.GET

interface NotesApiService {
    @GET("/notes")
    suspend fun getNotes(): Response<ArrayList<Note>>

}