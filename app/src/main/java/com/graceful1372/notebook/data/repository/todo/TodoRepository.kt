package com.graceful1372.notebook.data.repository.todo

import com.graceful1372.notebook.data.database.NoteDao
import com.graceful1372.notebook.data.model.NoteEntity
import javax.inject.Inject

class TodoRepository @Inject constructor(private val dao:NoteDao) {

    fun save(entity: NoteEntity) = dao.saveNote(entity)

    fun loadAllTodo(todo:String) = dao.getNoteString(todo )

    fun searchNote(title:String,category:String)=dao.searchNote(title,category)

    fun deleteAll(todo: String)= dao.deleteAllTodo(todo)

    fun deleteSingle(category:String , id: Int)= dao.deleteSingle(category , id )

    fun update(id:Int , boolean: Boolean ) = dao.updateIsShow(boolean , id)
    fun updateCheckBox(id: Int, boolean: Boolean) = dao.updateCheckBox(boolean, id)
}