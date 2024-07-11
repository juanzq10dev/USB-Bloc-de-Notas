package com.example.blocdenotas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.blocdenotas.databinding.FragmentNotesDetailBinding
import com.example.blocdenotas.retrofit.entity.NoteDelete
import com.example.blocdenotas.viewmodels.NoteShareViewModel
import com.example.blocdenotas.viewmodels.NotesDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesDetail : Fragment() {
    lateinit var binding: FragmentNotesDetailBinding
    lateinit var viewModel: NoteShareViewModel
    lateinit var detailViewModel: NotesDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotesDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).noteShareViewModel
        detailViewModel = (activity as MainActivity).noteDetailViewModel
        binding.viewModel = viewModel
        binding.detailsViewModel = detailViewModel
        detailViewModel.updateTexts()
        binding.lifecycleOwner = this
        setupDeleteButton()
        binding.button.setOnClickListener {
            detailViewModel.save()
            binding.root.findNavController().navigate(R.id.action_notesDetail_to_notesListPage)
        }

    }

    private fun setupDeleteButton() {
        if (viewModel.selectedNote == null) {
            binding.deleteButton.isVisible = false
        }
            binding.deleteButton.setOnClickListener {
                detailViewModel.remove()
                binding.root.findNavController().navigate(R.id.action_notesDetail_to_notesListPage)
            }

    }
}