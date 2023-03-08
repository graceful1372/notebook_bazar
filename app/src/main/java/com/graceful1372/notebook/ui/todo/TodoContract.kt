package com.graceful1372.notebook.ui.todo

import com.graceful1372.notebook.base.BasePresenter
import com.graceful1372.notebook.data.model.NoteEntity

interface TodoContract {
    interface View{



        fun showAllTodos(todos:List<NoteEntity>)
        fun showEmpty()


    }

    interface Presenter:BasePresenter{
        fun save(entity: NoteEntity)
        fun loadAllTodos(todo:String)
        fun deleteAll(todo: String)
        fun singleDelete(category:String , id:Int)

        fun update( int: Int,boolean: Boolean )
        fun updateCheckbox( int: Int,boolean: Boolean )

        fun search(title:String, category: String )


    }
}