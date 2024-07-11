package com.example.blocdenotas.room.models

import com.google.gson.annotations.SerializedName

class AccessToken(
    @SerializedName("user_id")
    val token: String
)