package com.graceful1372.notebook.ui.calendar

import com.graceful1372.notebook.base.BasePresenter
import com.graceful1372.notebook.data.model.NoteEntity

interface CalendarContract {
    interface View{

        fun showTodoWithDate(todo:List<NoteEntity>)
        fun showEmpty()
    }

    interface Presenter:BasePresenter{
        fun loadWithDate(entity: String)

    }
}