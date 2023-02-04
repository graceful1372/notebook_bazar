package com.graceful1372.notebook.utils.di

import android.app.Application
import com.graceful1372.notebook.utils.calendar.convert.CalendarTool
import com.graceful1372.notebook.utils.calendar.convert.MonthGeneratorClass
import com.graceful1372.notebook.utils.calendar.convert.ResourceUtils
import com.graceful1372.notebook.utils.calendar.model.CalendarModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalendarModule {

    @Provides
    @Singleton
    fun todayProvider(calendar: GregorianCalendar): CalendarModel {
        val calendarTool = CalendarTool(calendar)
        return with(calendarTool) {
            CalendarModel(
                iranianDay, iranianMonth, iranianYear,
                dayOfWeek,
                gregorianDay, gregorianMonth, gregorianYear,
                today = false

            )
        }
    }

    //Provides Gregorian calendar instance
    @Provides
    @Singleton
    fun gregorianCalendarProvider() = GregorianCalendar()


    // Provides CalendarTool instance to convert dates from Gregorian to Shamsi
    @Provides
    @Singleton
    fun calendarToolProvider(calendar: GregorianCalendar) =
        CalendarTool(calendar)


    //Provides local repository

    @Provides
    @Singleton
    fun monthGeneratorProvider(
        app: Application,
        calendarModel: CalendarModel,
        calendarTool: CalendarTool
    ): MonthGeneratorClass {
        ResourceUtils(app)
        return MonthGeneratorClass(calendarTool, calendarModel)
    }

}