package com.graceful1372.notebook.ui.todo


import com.graceful1372.notebook.base.BasePresenterImpl
import com.graceful1372.notebook.data.model.NoteEntity
import com.graceful1372.notebook.data.repository.todo.TodoRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class TodoPresenter @Inject constructor(
    private val repository: TodoRepository,
    private val view: TodoContract.View
) : TodoContract.Presenter, BasePresenterImpl() {

    override fun save(entity: NoteEntity) {
        disposable = repository.save(entity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {


            }
    }

    override fun loadAllTodos(todo:String) {
        disposable = repository.loadAllTodo(todo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNotEmpty()) {
                    view.showAllTodos(it)

                } else {
                    view.showEmpty()

                }
            }

    }

    override fun deleteAll(todo: String) {
        disposable = repository.deleteAll(todo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{}
    }

    override fun delete(entity: NoteEntity) {
        disposable = repository.deleteSingel(entity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }




}
