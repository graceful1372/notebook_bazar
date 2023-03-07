package com.graceful1372.notebook.ui.todo

import android.opengl.Visibility
import android.provider.ContactsContract.CommonDataKinds.Note
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.graceful1372.notebook.data.model.NoteEntity
import com.graceful1372.notebook.databinding.ItemChildTodoBinding
import com.graceful1372.notebook.databinding.ItemDateTodoBinding
import com.graceful1372.notebook.utils.CHECK
import com.graceful1372.notebook.utils.DELETE
import com.graceful1372.notebook.utils.isSHOW
import com.graceful1372.notebook.utils.toPersian
import java.util.IllegalFormatCodePointException
import java.util.Locale
import javax.inject.Inject

class AdapterTodo @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //Binding
    private lateinit var binding: ItemDateTodoBinding
    private lateinit var bindingChild: ItemChildTodoBinding
    private var items = emptyList<ListItem>()
    private var items2 = emptyList<NoteEntity>()

    //Check to persian mode in device
    val currentLocal = Locale.getDefault()

    inner class DateViewHolder(val binding: ItemDateTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DateItem) {

            if (currentLocal.language == "fa") {
                binding.showTime.text = item.date.toPersian()
            } else {
                binding.showTime.text = item.date
            }
        }
    }

    inner class TodoViewModel(binding: ItemChildTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GeneralItem) {


            bindingChild.apply {
                txtShowTodo.text = item.todo
                txtShowTodo.maxLines = 1;
                txtShowTodo.ellipsize = TextUtils.TruncateAt.END;
                oneDelete.visibility = if (item.isShow) View.VISIBLE else View.GONE
                checkbox.isChecked = item.isCheck
            }
            //Show
            bindingChild.root.setOnClickListener {
                onItemClickListener?.let { i ->
//                    item.isShow = !item.isShow
                    bindingChild.oneDelete.visibility = if (item.isShow) View.VISIBLE else View.GONE
                    i(item, isSHOW)

                }
            }

            //Delete
            bindingChild.oneDelete.setOnClickListener {
                onItemClickListener?.let {
                    it(item, DELETE)
                }
            }

            //Check
            bindingChild.checkbox.setOnClickListener {
                onItemClickListener?.let {

                        bindingChild.checkbox.isChecked = item.isCheck

                    it(item, CHECK)
                }
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemDateTodoBinding.inflate(layoutInflater, parent, false)
        bindingChild = ItemChildTodoBinding.inflate(layoutInflater, parent, false)


        return when (viewType) {
            ListItem.TYPE_DATE ->
                DateViewHolder(binding)
            else ->
                //با فعال کردن این جا تنظیمات لایه به هم میخورد
//                GeneralViewHolder(ItemChildTodoBinding.inflate(layoutInflater))
                TodoViewModel(bindingChild)


        }

    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            ListItem.TYPE_DATE -> (holder as DateViewHolder).bind(
                item = items[position] as DateItem
            )

            ListItem.TYPE_GENERAL -> {
                (holder as TodoViewModel).bind(item = items[position] as GeneralItem)

                // این قسمت برای تیک زدن وظایف انجام شده است
//                holder.click( items2[position] )

            }
        }

        //Not duplicate items
        holder.setIsRecyclable(false)
    }

    //Class diffUtils
    class TodoDiffUtils(
        private val oldItem: List<ListItem>,
        private val newItem: List<ListItem>
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

    fun setData(data: List<ListItem>) {
        val todoDiffUtil = TodoDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(todoDiffUtil)
        items = data

        diffUtils.dispatchUpdatesTo(this)
    }


    //Click
    private var onItemClickListener: ((GeneralItem, String) -> Unit)? = null
    fun setOnItemClickListener(listener: (GeneralItem, String) -> Unit) {
        onItemClickListener = listener
    }

    open class ListItem(
        val type: Int
    ) {
        companion object {
            const val TYPE_DATE = 0
            const val TYPE_GENERAL = 1
        }
    }

    class DateItem(
        val date: String
    ) : ListItem(TYPE_DATE)

    class GeneralItem(
        var id: Int,
        var todo: String,
        var isShow: Boolean,
        var isCheck:Boolean
    ) : ListItem(TYPE_GENERAL)
}
