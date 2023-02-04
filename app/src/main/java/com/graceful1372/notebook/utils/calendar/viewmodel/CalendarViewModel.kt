package com.graceful1372.notebook.utils.calendar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graceful1372.notebook.utils.calendar.convert.MonthGeneratorClass
import com.graceful1372.notebook.utils.calendar.model.MonthType
import com.graceful1372.notebook.utils.calendar.model.CalendarModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val monthGenerator: MonthGeneratorClass
) : ViewModel() {


    //This for Iranian Calendar----------------------------
    private val monthLiveData = MutableLiveData<List<CalendarModel>>()

    init {
        monthGenerator.getMonthList(MonthType.PREVIOUS_MONTH)
        monthLiveData.value = monthGenerator.getMonthList(MonthType.NEXT_MONTH)
    }

    /**
     * Fetches and publishes the next month (from the month that currently user is watching) as a list of days
     */
    fun getNextMonth() {
        monthLiveData.value = monthGenerator.getMonthList(MonthType.NEXT_MONTH)
    }

    /**
     * Fetches and publishes the previous month (from the month that currently user is watching) as a list of days
     */
    fun getPreviousMonth() {
        monthLiveData.value = monthGenerator.getMonthList(MonthType.PREVIOUS_MONTH)
    }

    /**
     * Returns live data of [monthLiveData]
     */
    fun getMonthLiveData(): LiveData<List<CalendarModel>> = monthLiveData

}

