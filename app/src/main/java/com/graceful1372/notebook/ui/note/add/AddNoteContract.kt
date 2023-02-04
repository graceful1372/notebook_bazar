package com.graceful1372.notebook.ui.note.add

import com.graceful1372.notebook.base.BasePresenter
import com.graceful1372.notebook.data.model.NoteEntity

interface AddNoteContract {
    interface Presenter: BasePresenter {
        fun save (entity: NoteEntity)
    }
}