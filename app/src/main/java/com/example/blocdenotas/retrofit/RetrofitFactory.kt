package com.example.blocdenotas.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {
    companion object {
        val BASE_URL = "https://notes-app-9c438.web.app"

        fun getInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()
        }
    }
}