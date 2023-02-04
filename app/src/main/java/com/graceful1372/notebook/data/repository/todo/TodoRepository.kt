package com.graceful1372.notebook.data.repository.todo

import com.graceful1372.notebook.data.database.NoteDao
import com.graceful1372.notebook.data.model.NoteEntity
import javax.inject.Inject

class TodoRepository @Inject constructor(private val dao:NoteDao) {

    fun save(entity: NoteEntity) = dao.saveNote(entity)

    fun loadAllTodo(todo:String) = dao.getNoteString(todo )

    fun deleteAll(todo: String)= dao.deleteAllTodo(todo)

    fun deleteSingel(entity: NoteEntity)= dao.deleteNote(entity)
}