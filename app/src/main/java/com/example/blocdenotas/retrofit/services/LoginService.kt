package com.example.blocdenotas.retrofit.services

import com.example.blocdenotas.retrofit.entity.LoginGet
import com.example.blocdenotas.retrofit.entity.LoginPost
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body

interface LoginService {
    @POST("/login")
    suspend fun login(@Body loginPost: LoginPost): Response<LoginGet>
}