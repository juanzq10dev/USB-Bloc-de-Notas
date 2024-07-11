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
import retrofit2.http.PUT
import retrofit2.http.Query

interface NotesApiService {
    @GET("/notes")
    suspend fun getNotes(@Query("user_id") userId: String): Response<ArrayList<Note>>

    @POST("/notes")
    fun insertToApi(@Body note: Note): Call<String>

    @PUT("/notes")
    fun updateToApi(@Body note: Note): Call<String>

    @POST("/login")
    fun login(@Body loginPost: LoginPost): Call<AccessToken>

}