package com.graceful1372.notebook.utils.calendar.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class
CalendarModel(
    val iranianDay: Int,
    val iranianMonth: Int,
    val iranianYear: Int,
    val dayOfWeek: Int,
    var gDay: Int,
    val gMonth: Int,
    val gYear: Int,
    var isHoliday: Boolean = false,
    var today: Boolean = false,

    //For change color item recyclerview
    var isSelect:Boolean = false
)
    : Parcelable