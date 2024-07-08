package com.example.blocdenotas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.blocdenotas.databinding.FragmentNotesListPageBinding

class NotesListPage : Fragment(R.layout.fragment_notes_list_page) {
    lateinit var binding: FragmentNotesListPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotesListPageBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_notes_list_page, container, false)
    }

    private fun setupAddButton() {

    }

}