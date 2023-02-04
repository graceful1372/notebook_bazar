package com.graceful1372.notebook.ui.note

import com.graceful1372.notebook.base.BasePresenterImpl
import com.graceful1372.notebook.data.model.NoteEntity
import com.graceful1372.notebook.data.repository.note.NoteRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class NotePresenter @Inject constructor(
    private val repository: NoteRepository,
    private val view: NoteContract.View
) : NoteContract.Presenter, BasePresenterImpl() {

    override fun loadNote(category: String) {
        disposable = repository.loadNote(category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNotEmpty()) {
                    view.showNote(it)
                } else {
                    view.showEmpty()
                }

            }
    }

    override fun filterNote(priority: String) {
        TODO("Not yet implemented")
    }

    override fun searchNote(title: String,category: String) {
        disposable = repository.searchNote(title, category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNotEmpty()) {
                    view.showNote(it)
                } else {
                    view.showEmpty()
                }
            }


    }

    override fun delete(entity: NoteEntity) {
        disposable = repository.deleteNote(entity)
            .subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }


}