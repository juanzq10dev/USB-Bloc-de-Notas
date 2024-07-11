package com.example.blocdenotas

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.blocdenotas.observer.ConnectivityObserver
import com.example.blocdenotas.observer.NetworkConnectivityObserver
import com.example.blocdenotas.viewmodels.LoginViewModel
import com.example.blocdenotas.viewmodels.NoteShareViewModel
import com.example.blocdenotas.viewmodels.NoteShareViewModelFactory
import com.example.blocdenotas.viewmodels.NotesDetailViewModel
import com.example.blocdenotas.viewmodels.NotesDetailViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_db")
class MainActivity : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel
    lateinit var noteShareViewModel: NoteShareViewModel
    lateinit var noteDetailViewModel: NotesDetailViewModel
    lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        connectivityObserver = NetworkConnectivityObserver(applicationContext)

        val factory = NoteShareViewModelFactory(applicationContext, dataStore)
        noteShareViewModel = ViewModelProvider(this, factory).get(NoteShareViewModel::class.java)

        val detailFactory = NotesDetailViewModelFactory(noteShareViewModel)
        noteDetailViewModel = ViewModelProvider(this, detailFactory).get(NotesDetailViewModel::class.java)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            noteShareViewModel.getAccessToken().collect { accessToken ->
                withContext(Dispatchers.Main) {
                    val navController = findNavController(R.id.fragmentContainerView0)
                    if (accessToken.token.isEmpty()) {
                        navController.navigate(R.id.login)
                    } else {
                        navController.navigate(R.id.notesListPage)
                    }
                }
            }
        }
    }
}