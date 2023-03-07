package com.graceful1372.notebook.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.graceful1372.notebook.data.model.NoteEntity
import com.graceful1372.notebook.data.repository.calendar.CalendarRepository
import com.graceful1372.notebook.databinding.FragmentCalendarBinding
import com.graceful1372.notebook.databinding.PersianCalendarBinding
import com.graceful1372.notebook.utils.calendar.adapter.CalendarAdapter
import com.graceful1372.notebook.utils.calendar.model.CalendarModel
import com.graceful1372.notebook.utils.calendar.utils.toPersianMonth
import com.graceful1372.notebook.utils.calendar.utils.toPersianNumber
import com.graceful1372.notebook.utils.calendar.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CalendarFragment : Fragment(), CalendarContract.View {
    //Binding
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var calendarBinding: PersianCalendarBinding

    @Inject
    lateinit var adapterTodo: AdapterCalendar

    @Inject
    lateinit var repository: CalendarRepository

    @Inject
    lateinit var today: CalendarModel


    private val viewModel: CalendarViewModel by viewModels()

    private var dateDefualt: String = ""

    private val adapterCalendar: CalendarAdapter = CalendarAdapter {

        val y = it.iranianYear.toString()
        val m = it.iranianMonth.toString()
        val d = it.iranianDay.toString()

        val date = "$y/$m/$d"

        presenter.loadWithDate(date)
//        Toast.makeText(activity, date, Toast.LENGTH_SHORT).show()
    }


    private val presenter by lazy { CalendarPresenter(repository, this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        calendarBinding = binding.calendar

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get data with current date

        val t = today.iranianDay.toString()
        val m = today.iranianMonth.toString()
        val y = today.iranianYear.toString()
        dateDefualt = "$y/$m/$t"
        presenter.loadWithDate(dateDefualt)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //InitValues
        binding.apply {


        }

        //Show Calendar
        calendarBinding.apply {

            viewModel.getMonthLiveData().observe(viewLifecycleOwner) {
                showCalendar(it.toMutableList())
            }


            recyclerCalendar.adapter = adapterCalendar
            recyclerCalendar.itemAnimator = null

            //button next month and previous
            imgNextMonth.setOnClickListener {
                viewModel.getNextMonth()
            }

            //button previous month
            imgPreviousMonth.setOnClickListener {
                viewModel.getPreviousMonth()
            }

            //Gone topView calenar
            viewTop.visibility = View.GONE
        }


    }


    override fun showTodoWithDate(todo: List<NoteEntity>) {
        binding.recyclerViewCalendar.visibility = View.VISIBLE
        binding.emptyListCalendar.visibility = View.GONE
        adapterTodo.setData(todo)
        binding.recyclerViewCalendar.apply {
            layoutManager = LinearLayoutManager(activity)

            adapter = adapterTodo
        }
    }

    override fun showEmpty() {
        binding.recyclerViewCalendar.visibility = View.GONE
        binding.emptyListCalendar.visibility = View.VISIBLE
    }

    private fun showCalendar(list: List<CalendarModel>) {
        calendarBinding.txtMonthName.text =
            list.last().iranianMonth.toPersianMonth(requireContext())
        calendarBinding.txtYear.text = list.last().iranianYear.toPersianNumber()
        adapterCalendar.submitList(list)
    }


}