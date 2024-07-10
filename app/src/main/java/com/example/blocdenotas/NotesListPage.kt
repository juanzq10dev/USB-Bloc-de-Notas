package com.example.blocdenotas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.blocdenotas.room.models.Note
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blocdenotas.databinding.FragmentNotesListPageBinding
import com.example.blocdenotas.viewmodels.NoteShareViewModel

class NotesListPage : Fragment(R.layout.fragment_notes_list_page) {
    lateinit var binding: FragmentNotesListPageBinding
    lateinit var viewModel: NoteShareViewModel
    lateinit var adapter: NotesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotesListPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).noteShareViewModel
        setupAddButton()
        setupRecyclerView()
    }

    private fun setupAddButton() {
        binding.floatingActionButton.setOnClickListener {
            val direction = NotesListPageDirections.actionNotesListPageToNotesDetail()
            binding.root.findNavController().navigate(direction)
        }
    }

    private fun setupLogginButton() {

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