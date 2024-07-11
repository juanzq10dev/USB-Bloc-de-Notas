package com.example.blocdenotas.observer

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<InternetStatus>

    enum class InternetStatus {
        Available, Unavailable, Losing, Lost
    }
}