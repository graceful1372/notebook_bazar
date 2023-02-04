package com.graceful1372.notebook.utils.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graceful1372.notebook.utils.calendar.utils.EMPTY_DATE
import com.graceful1372.notebook.utils.calendar.model.CalendarModel
import com.graceful1372.notebook.utils.calendar.utils.toPersianNumber
import com.graceful1372.notebook.R
import com.graceful1372.notebook.databinding.CalendarItemBinding


class CalendarAdapter(val clickListener: (CalendarModel) -> Unit) :
    ListAdapter<CalendarModel, CalendarAdapter.CalendarViewHolder>(CalendarDiffUtils()) {
    private lateinit var context: Context
    private var selectedItem = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding =
            CalendarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CalendarViewHolder(binding, binding.root)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.onBind(getItem(position))

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class CalendarViewHolder(private val binding: CalendarItemBinding, containerView: View) :
        RecyclerView.ViewHolder(containerView) {


        fun onBind(dateModel: CalendarModel) {
            with(dateModel) {
                with(binding) {
                    txtIranianDate.isVisible = iranianDay != EMPTY_DATE
                    txtGregorianDate.isVisible = iranianDay != EMPTY_DATE
                    if (iranianDay == EMPTY_DATE) {
                        return
                    }

                    //Click
                    itemView.setOnClickListener {
                        clickListener(dateModel)
                        selectedItem = adapterPosition
                        notifyDataSetChanged()
                    }
                    //Change color item clicked
                    if (selectedItem == adapterPosition) {
                        cardDays.setBackgroundResource(R.drawable.bg_click_item_calendar)
                    } else {
                        cardDays.setBackgroundResource(R.drawable.background_item_calendar)
                    }

                    txtIranianDate.text = iranianDay.toPersianNumber()
//                    txtGregorianDate.text = gDay.toString()
                    txtIranianDate.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.text_color
                        )
                    )



                    if (today) {
                        txtIranianDate.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.light_navy
                            )
                        )
                        txtGregorianDate.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.white
                            )
                        )

                        cardDays.background =
                            ContextCompat.getDrawable(context, R.drawable.background_today)
                    }

                    //Show Holiday
                    if (isHoliday)
                        txtIranianDate.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.text_color
                            )
                        )
                }
            }


        }


    }
}

private class CalendarDiffUtils : DiffUtil.ItemCallback<CalendarModel>() {

    override fun areItemsTheSame(oldItem: CalendarModel, newItem: CalendarModel) =
        oldItem.iranianDay == newItem.iranianDay && oldItem.iranianMonth == newItem.iranianMonth
                && oldItem.iranianYear == newItem.iranianYear

    override fun areContentsTheSame(oldItem: CalendarModel, newItem: CalendarModel) =
        oldItem.iranianDay == newItem.iranianDay && oldItem.iranianMonth == newItem.iranianMonth
                && oldItem.iranianYear == newItem.iranianYear
}