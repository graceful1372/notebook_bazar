package com.graceful1372.notebook.ui.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.graceful1372.notebook.databinding.ItemNoteBinding
import com.graceful1372.notebook.data.model.NoteEntity
import javax.inject.Inject

class AdapterNote @Inject constructor() : RecyclerView.Adapter<AdapterNote.ViewHolder>() {
    private lateinit var binding: ItemNoteBinding
    private var noteList = emptyList<NoteEntity>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemNoteBinding.inflate(inflater, parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(noteList[position])
        //Not duplicate item
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    //Inner class
    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NoteEntity) {
            binding.apply {
                titleNote.text = item.title
                textNote.text = item.text


            }

            //Click
            binding.btnDeleteNote.setOnClickListener {

                    onItemClickListener?.let {
                        it(item)

                }
            }
            /*binding.root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }

            }*/

        }

    }

    //DifUtil class
    class NoteDiffUtils(
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

    //Set data to list adapter
    fun setData(data: List<NoteEntity>) {
        //Check data with DiffUtils class
        val noteDiffUtil = NoteDiffUtils(noteList, data)
        val diffUtils = DiffUtil.calculateDiff(noteDiffUtil)

        //Filling the list so that it is not empty
        noteList = data
        diffUtils.dispatchUpdatesTo(this)

    }

    //Click
    private var onItemClickListener: ((NoteEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (NoteEntity) -> Unit) {
        onItemClickListener = listener
    }

}