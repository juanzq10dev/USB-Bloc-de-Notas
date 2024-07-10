package com.example.blocdenotas.room.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import okio.Path.Companion.toPath

class DataStoreInstance {
    companion object {
        private var INSTANCE: DataStore<Preferences> ?= null

        fun getDataStore(context: Context): DataStore<Preferences> {
            var instance = INSTANCE
            if (instance == null) {
                instance = createDataStore(context)
            }
            INSTANCE = instance
            return instance
        }
        fun createDataStore(context: Context): DataStore<Preferences> = createDataStore(
            producePath = { context.filesDir.resolve("my_db").absolutePath }
        )

        private fun createDataStore(producePath: () -> String): DataStore<Preferences> =
            PreferenceDataStoreFactory.createWithPath(
                produceFile = { producePath().toPath() })
    }
}