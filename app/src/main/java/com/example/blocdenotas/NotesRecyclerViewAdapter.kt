package com.example.blocdenotas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blocdenotas.databinding.NotesCardBinding
import com.example.blocdenotas.room.models.Note

class NotesRecyclerViewAdapter(var notes: List<Note>, val clickListener: (Note) -> Unit): RecyclerView.Adapter<NotesRecyclerViewAdapter.NotesViewHolder>() {
    inner class NotesViewHolder(val binding: NotesCardBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NotesCardBinding.inflate(inflater, parent, false)
        return NotesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.note = note
        holder.binding.notesCell.setOnClickListener { clickListener(note) }
    }
}