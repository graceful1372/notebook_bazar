package com.graceful1372.notebook.utils.calendar.convert



import com.graceful1372.notebook.utils.calendar.utils.EMPTY_DATE
import com.graceful1372.notebook.utils.calendar.model.CalendarModel
import com.graceful1372.notebook.utils.calendar.model.DateModel
import com.graceful1372.notebook.utils.calendar.model.MonthType
import javax.inject.Inject

private const val MONDAY = 0
private const val TUESDAY = 1
private const val WEDNESDAY = 2
private const val THURSDAY = 3
private const val FRIDAY = 4
private const val SATURDAY = 5
private const val SUNDAY = 6

/**
 * This class generated a month before or after the current month that the user is looking at
 *
 * @property calendar: The object that holds the current date that the user is looking
 */
class MonthGeneratorClass @Inject constructor(
    private var calendar: CalendarTool,
    private val currentDate: CalendarModel
) {

    /**
     * Generates list of all days of the next or previous month
     *
     * @param monthType: it can be [NEXT_MONTH] or [PREVIOUS_MONTH]
     * @return
     */
    fun  getMonthList(monthType: MonthType): List<CalendarModel> {
        val list = arrayListOf<CalendarModel>()
        val month = if (monthType == MonthType.NEXT_MONTH) getNextMonthDate() else getPreviousMonthDate()

        // این قسمت برای روز های خالی استفاده شده
        // این قسمت باید در نشان دادن تقویم به صورت چارت فعال باشد
        // در صورت غیر فعال بودن باعث بهم ریخته گی میشود
        list.addAll(addEmptyDays(calendar.dayOfWeek))
        for (i in 1..month.dayNumber) {
            with(calendar) {
                setIranianDate(month.year, month.month, i)
                list.add(
                    CalendarModel(
                        iranianDay,
                        iranianMonth,
                        iranianYear,
                        dayOfWeek,
                        gregorianDay,
                        gregorianMonth,
                        gregorianYear
                    ).apply{
                        checkIsHoliday(this)
                        checkIsToday(this)
                    })
            }
        }
        return list
    }


    private fun checkIsToday(calendarModel: CalendarModel) {
        calendarModel.today = currentDate == calendarModel
    }

    private fun checkIsHoliday(calendarModel: CalendarModel) = with(calendarModel) {
        if (dayOfWeek == FRIDAY
//            || (iranianYear == currentDate.iranianYear && ResourceUtils.vacationP.containsKey(
//                iranianMonth * 100 + iranianDay
//            ))
        )
            isHoliday = true
    }

    private fun getNextMonthDate(): DateModel {
        var month = calendar.iranianMonth
        var year = calendar.iranianYear

        if (month + 1 <= 12) {
            month++
        } else {
            month = 1
            year++
        }

        calendar.setIranianDate(year, month, 1)
        val dayNumber =
            if (month <= 6) 31 else if (month == 12 && !calendar.isLeap(calendar.iranianYear)) 29 else 30
        return DateModel(year, month, dayNumber)
    }

    private fun getPreviousMonthDate(): DateModel {
        var month = calendar.iranianMonth
        var year = calendar.iranianYear

        if (month - 1 > 1)
            month--
        else {
            month = 12
            year--
        }

        calendar.setIranianDate(year, month, 1)
        val dayNumber =
            if (month <= 6) 31 else if (month == 12 && !calendar.isLeap(calendar.iranianYear)) 29 else 30
        return DateModel(year, month, dayNumber)
    }

    private fun addEmptyDays(dayOfWeek: Int): ArrayList<CalendarModel> = when (dayOfWeek) {
        MONDAY -> emptyDayMaker(2)
        TUESDAY -> emptyDayMaker(3)
        WEDNESDAY -> emptyDayMaker(4)
        THURSDAY -> emptyDayMaker(5)
        FRIDAY -> emptyDayMaker(6)
        SATURDAY -> emptyDayMaker(0)
        SUNDAY -> emptyDayMaker(1)
        else -> throw IllegalArgumentException("Day is not defined in the week!")
    }

    private fun emptyDayMaker(dayOfWeek: Int): ArrayList<CalendarModel> {
        val list = arrayListOf<CalendarModel>()
        for (i in 1..dayOfWeek) {
            list.add(
                CalendarModel(
                    EMPTY_DATE,
                    EMPTY_DATE,
                    EMPTY_DATE,
                    EMPTY_DATE,
                    EMPTY_DATE,
                    EMPTY_DATE,
                    EMPTY_DATE
                )
            )
        }
        return list
    }
}

