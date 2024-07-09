package com.example.blocdenotas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.blocdenotas.viewmodels.LoginViewModel
import com.example.blocdenotas.viewmodels.NoteShareViewModel
import com.example.blocdenotas.viewmodels.NoteShareViewModelFactory
import com.example.blocdenotas.viewmodels.NotesDetailViewModel
import com.example.blocdenotas.viewmodels.NotesDetailViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel
    lateinit var noteShareViewModel: NoteShareViewModel
    lateinit var noteDetailViewModel: NotesDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factory = NoteShareViewModelFactory(applicationContext)
        noteShareViewModel = ViewModelProvider(this, factory).get(NoteShareViewModel::class.java)

        val detailFactory = NotesDetailViewModelFactory(noteShareViewModel)
        noteDetailViewModel = ViewModelProvider(this, detailFactory).get(NotesDetailViewModel::class.java)

        loginViewModel = LoginViewModel()
    }
}