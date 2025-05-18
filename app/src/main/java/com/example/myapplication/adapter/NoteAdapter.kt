package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.NoteLayoutBinding
import com.example.myapplication.fragments.HomeFragment
import com.example.myapplication.fragments.MainLetterFragment
import com.example.myapplication.fragments.MainLetterFragmentDirections
import com.example.myapplication.model.Note

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){
//    class NoteViewHolder(val itemBinding: NoteLayoutBinding): RecyclerView.ViewHolder(itemBinding.root)

    private val differentCallback = object: DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.noteDesc == newItem.noteDesc &&
                    oldItem.noteTittle == newItem.noteTittle
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differentCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickListener: ((Note) -> Unit)? = null

    fun setOnItemClickListener(listener: (Note) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
         val currentNote = differ.currentList[position]
        holder.bind(currentNote)
//        holder.itemView.setOnClickListener {
//            onItemClickListener?.invoke(currentNote)
//        }
//        holder.itemBinding.noteTitle.text = currentNote.noteTittle
//        holder.itemBinding.noteDesc.text = currentNote.noteDesc
//
//        holder.itemView.setOnClickListener{
//          val direction = MainLetterFragmentDirections.actionHomeFragmentToEditNoteFragment(currentNote)
//        }
    }

    private var onItemLongClickListener: ((Note, View) -> Unit)? = null

    fun setOnItemLongClickListener(listener: (Note, View) -> Unit) {
        onItemLongClickListener = listener
    }

    inner class NoteViewHolder(val itemBinding: NoteLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        // ... существующий код ...

        fun bind(note: Note) {
            // ... существующий код привязки данных ...
            itemBinding.apply {
                noteTitle.text = note.noteTittle
                noteDesc.text = note.noteDesc
            }

            itemView.setOnClickListener {
                onItemClickListener?.invoke(note)
            }

            itemView.setOnLongClickListener { view ->
                onItemLongClickListener?.invoke(note, view)
                true
            }
        }
    }

}