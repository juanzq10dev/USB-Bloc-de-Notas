package com.example.blocdenotas.api

import com.example.blocdenotas.retrofit.entity.LoginGet
import com.example.blocdenotas.retrofit.entity.LoginPost
import com.example.blocdenotas.room.models.AccessToken
import com.example.blocdenotas.room.models.Note
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotesApiService {
    @GET("/notes")
    suspend fun getNotes(): Response<ArrayList<Note>>

    @POST("/login")
    fun login(@Body loginPost: LoginPost): Call<AccessToken>

}