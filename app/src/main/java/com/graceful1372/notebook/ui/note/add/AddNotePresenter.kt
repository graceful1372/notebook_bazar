package com.graceful1372.notebook.ui.note.add

import com.graceful1372.notebook.base.BasePresenterImpl
import com.graceful1372.notebook.data.model.NoteEntity
import com.graceful1372.notebook.data.repository.note.NoteRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AddNotePresenter @Inject constructor(
    private val repository: NoteRepository
):AddNoteContract.Presenter , BasePresenterImpl() {

    override fun save(entity: NoteEntity) {
        disposable = repository.saveNote(entity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{

            }
    }


}