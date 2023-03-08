package com.graceful1372.notebook.ui.todo.calendarSheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.graceful1372.notebook.databinding.FragmentCalendarSheetBinding
import com.graceful1372.notebook.utils.calendar.adapter.CalendarAdapter
import com.graceful1372.notebook.utils.calendar.model.CalendarModel
import com.graceful1372.notebook.utils.calendar.utils.toPersianMonth
import com.graceful1372.notebook.utils.calendar.utils.toPersianNumber
import com.graceful1372.notebook.utils.calendar.utils.toPersianWeekDay
import com.graceful1372.notebook.utils.calendar.viewmodel.CalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CalendarSheetFragment : BottomSheetDialogFragment() {
    //Binding
    private lateinit var binding: FragmentCalendarSheetBinding


    //Other
    private val viewModel: CalendarViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    private val adapter: CalendarAdapter = CalendarAdapter { cm ->
        //InitViews in Calendar sheet
        binding.apply {
            txtDayOfWeek.text = cm.dayOfWeek.toPersianWeekDay(requireContext()) + " " +
                    cm.iranianDay.toPersianNumber() + " " +
                    cm.iranianMonth.toPersianMonth(requireContext())




            //Show btn
            btnOk.visibility = View.VISIBLE


            //Button ok
            btnOk.setOnClickListener {
                val y = cm.iranianYear.toString()
                val m = cm.iranianMonth.toString()
                val d = cm.iranianDay.toString()

                val date = "$y/$m/$d"

                //Send to TodoFragment
                val result = Bundle().apply {
                    putString("MY_KEY", date)
                }
                setFragmentResult("requestCode", result)
                dismiss()


            }
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarSheetBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {

            //Show Calendar
            viewModel.getMonthLiveData().observe(viewLifecycleOwner) {
                showCalendar(it.toMutableList())
            }

            //Calendar
            recyclerCalendar.adapter = adapter
            recyclerCalendar.itemAnimator = null

            //button next month and previous
            imgNextMonth.setOnClickListener {
                viewModel.getNextMonth()
            }

            //button previous month
            imgPreviousMonth.setOnClickListener {
                viewModel.getPreviousMonth()
            }


        }
    }

    private fun showCalendar(list: List<CalendarModel>) {
        binding.txtMonthName.text = list.last().iranianMonth.toPersianMonth(requireContext())
        binding.txtYear.text = list.last().iranianYear.toPersianNumber()
        adapter.submitList(list)
    }


}


