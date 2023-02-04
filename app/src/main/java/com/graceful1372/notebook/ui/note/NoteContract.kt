package com.graceful1372.notebook.ui.note

import com.graceful1372.notebook.data.model.NoteEntity

interface NoteContract {
    interface View{
        fun showNote(note:List<NoteEntity>)
        fun showEmpty()
    }
    interface Presenter{
        fun loadNote(category: String)
        fun filterNote(priority:String)
        fun searchNote(title:String,category: String)
        fun delete(entity:NoteEntity)
    }
}