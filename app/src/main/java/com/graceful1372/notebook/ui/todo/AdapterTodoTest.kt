package com.graceful1372.notebook.ui.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.graceful1372.notebook.R
import com.graceful1372.notebook.data.model.NoteEntity
import com.graceful1372.notebook.databinding.ItemChildTodoBinding
import com.graceful1372.notebook.databinding.ItemDateTodoBinding
import com.graceful1372.notebook.databinding.NavHeaderMainBinding
import javax.inject.Inject

class AdapterTodoTest @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var notes= emptyList<NoteEntity>()
    private lateinit var bindingDate:ItemDateTodoBinding
    private lateinit var binding:ItemChildTodoBinding
    companion object {
        private const val VIEW_TYPE_NOTE = 1
        private const val VIEW_TYPE_NOTE_WITH_DATE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        bindingDate = ItemDateTodoBinding.inflate(layoutInflater , parent , false)
        binding = ItemChildTodoBinding.inflate(layoutInflater , parent , false)
        return when (viewType) {
            VIEW_TYPE_NOTE_WITH_DATE -> NoteWithDateViewHolder(bindingDate)
            else -> NoteViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_NOTE_WITH_DATE -> {
                val noteWithDate = notes[position] as NoteWithDate
                (holder as NoteWithDateViewHolder).bind(noteWithDate)
            }
            else -> {
                val note = notes[position] as Note
                (holder as NoteViewHolder).bind(note)
            }
        }
    }

    override fun getItemCount() = notes.size

    override fun getItemViewType(position: Int): Int {
        return if (notes[position].date.isNotEmpty()) {
            VIEW_TYPE_NOTE_WITH_DATE
        } else {
            VIEW_TYPE_NOTE
        }
    }

    class NoteViewHolder(val binding:ItemChildTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            // Bind note to ViewHolder views
            binding.txtShowTodo.text = note.noteEntity.todo.toString()

        }
    }

    class NoteWithDateViewHolder(val bindingDate:ItemDateTodoBinding) : RecyclerView.ViewHolder(bindingDate.root) {
        fun bind(noteWithDate: NoteWithDate) {
            // Bind noteWithDate to ViewHolder views
            bindingDate.showTime.text= noteWithDate.noteEntity.date
        }
    }

    class TodoDiffUtils(
        private val oldItem: List<NoteEntity>,
        private val newItem: List<NoteEntity>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

    }

    fun setData(data: List<NoteEntity>) {
        val todoDiffUtil = TodoDiffUtils(notes, data)
        val diffUtils = DiffUtil.calculateDiff(todoDiffUtil)
        notes = data

        diffUtils.dispatchUpdatesTo(this)
    }

    private var setOnItemClick:((NoteEntity)->Unit)? = null
    fun setOnItemClickListener(listener:(NoteEntity)->Unit){
        setOnItemClick = listener
    }

    // Classes to represent the two types of notes
    data class Note(val noteEntity: NoteEntity)
    data class NoteWithDate(val noteEntity: NoteEntity)
}
