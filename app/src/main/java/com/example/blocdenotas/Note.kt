package com.example.blocdenotas

data class Note(val date: String, val latitude: Int, val longitude: Int,
                val userId: String, val id: String, var body: String, var title: String) {
}