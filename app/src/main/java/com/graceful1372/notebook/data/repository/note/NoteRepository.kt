package com.graceful1372.notebook.data.repository.note

import com.graceful1372.notebook.data.database.NoteDao
import com.graceful1372.notebook.data.model.NoteEntity
import javax.inject.Inject

class NoteRepository @Inject constructor(private val dao: NoteDao) {
    fun saveNote(entity: NoteEntity)=dao.saveNote(entity)
    fun loadNote(entity: String)=dao.getNoteString(entity)
    fun searchNote(title:String,category:String)=dao.searchNote(title,category)
    fun deleteNote(entity: NoteEntity)=dao.deleteNote(entity)
}