package com.graceful1372.notebook.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.graceful1372.notebook.data.model.NoteEntity
import com.graceful1372.notebook.databinding.ItemChildTodoBinding
import javax.inject.Inject

class AdapterCalendar @Inject constructor():RecyclerView.Adapter<AdapterCalendar.ViewHolder>() {
    //Binding
    private lateinit var binding:ItemChildTodoBinding
    private var todoListMain = emptyList<NoteEntity>()


    inner class ViewHolder :RecyclerView.ViewHolder(binding.root){
        fun bind (data: NoteEntity){
            binding.apply {
                txtShowTodo.text = data.todo

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = ItemChildTodoBinding.inflate(LayoutInflater.from(parent.context),parent ,false)
        return ViewHolder()
    }

    override fun getItemCount(): Int {
       return todoListMain.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todoListMain[position])

        //Not duplicate items
        holder.setIsRecyclable(false)
    }

    //Class diffUtils
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

    fun setData(data: List<NoteEntity>){
        val todoDiffUtil = TodoDiffUtils(todoListMain , data)
        val diffUtils = DiffUtil.calculateDiff(todoDiffUtil)
        todoListMain = data
        diffUtils.dispatchUpdatesTo(this)
    }

    //Click
    private var onItemClickListener: ((NoteEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (NoteEntity) -> Unit) {
        onItemClickListener = listener
    }
}
