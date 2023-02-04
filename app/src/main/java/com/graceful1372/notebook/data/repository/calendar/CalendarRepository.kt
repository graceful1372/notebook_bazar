package com.graceful1372.notebook.data.repository.calendar

import com.graceful1372.notebook.data.database.NoteDao
import javax.inject.Inject

class CalendarRepository @Inject constructor(private val dao: NoteDao)
{
    fun loadTodoWithDate(date:String)=dao.getTodoWithDate(date)
}