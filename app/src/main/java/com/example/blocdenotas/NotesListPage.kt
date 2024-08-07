package com.example.blocdenotas

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources.Theme
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import com.example.blocdenotas.room.models.Note
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blocdenotas.databinding.FragmentNotesListPageBinding
import com.example.blocdenotas.observer.ConnectivityObserver
import com.example.blocdenotas.viewmodels.NoteShareViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesListPage : Fragment(R.layout.fragment_notes_list_page) {
    lateinit var binding: FragmentNotesListPageBinding
    lateinit var viewModel: NoteShareViewModel
    lateinit var adapter: NotesRecyclerViewAdapter
    lateinit var pref: DataStore<Preferences>
    lateinit var connectivityObserver: ConnectivityObserver
    var loggedIn = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotesListPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as MainActivity).noteShareViewModel
        pref = (activity as MainActivity).dataStore
        connectivityObserver = (activity as MainActivity).connectivityObserver
        lifecycleScope.launch {
            connectivityObserver.observe().collect {
                if (it == ConnectivityObserver.InternetStatus.Available) {
                    withContext(Dispatchers.Main) {
                        viewModel.getAllContacts()
                    }
                }
            }
        }

        setupAddButton()
        setupRecyclerView()
        setupLogOutButton()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        lifecycleScope.launch {
            connectivityObserver.observe().collect {
                if (it != ConnectivityObserver.InternetStatus.Available ) {
                    withContext(Dispatchers.Main) {
                        binding.floatingActionButton.isEnabled = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.floatingActionButton.isEnabled = true

                    }
                }
            }
        }

        if (!loggedIn) {
            val direction = NotesListPageDirections.actionNotesListPageToLogin()
            binding.root.findNavController().navigate(direction)
        }
        super.onStart()
    }

    private fun setupAddButton() {
        binding.floatingActionButton.setOnClickListener {
            viewModel.selectedNote = null
            val direction = NotesListPageDirections.actionNotesListPageToNotesDetail()
            binding.root.findNavController().navigate(direction)
        }
    }

    private fun setupLogOutButton() {
        binding.buttonLogOut.setOnClickListener {
            loggedIn = false
            viewModel.removeToken()
            Thread.sleep(100)
            val direction = NotesListPageDirections.actionNotesListPageToLogin()
            binding.root.findNavController().navigate(direction)
        }
    }

    private fun setupRecyclerView() {
        adapter = NotesRecyclerViewAdapter(listOf()) { note ->
            viewModel.selectNote(note)
            val direction = NotesListPageDirections.actionNotesListPageToNotesDetail()
            binding.root.findNavController().navigate(direction)
        }

        val ownerContext = (activity as MainActivity)
        binding.recyclerView.layoutManager = LinearLayoutManager(ownerContext, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter



        activity.let {
            viewModel.notes.observe(viewLifecycleOwner) { notes ->
                adapter.notes = notes
                adapter.notifyDataSetChanged()
            }
        }
    }

}