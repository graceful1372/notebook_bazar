package com.graceful1372.notebook.ui.todo


import com.graceful1372.notebook.base.BasePresenterImpl
import com.graceful1372.notebook.data.model.NoteEntity
import com.graceful1372.notebook.data.repository.todo.TodoRepository
import com.graceful1372.notebook.utils.calendar.model.CalendarModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TodoPresenter @Inject constructor(
    private val repository: TodoRepository,
    private val view: TodoContract.View,
    today: CalendarModel
) : TodoContract.Presenter, BasePresenterImpl() {


    //use this section for filter date
    val t = today.iranianDay.toString()
    val m = today.iranianMonth.toString()
    val y = today.iranianYear.toString()
    val curentdate = "$y/$m/$t"
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
    val filterDate = dateFormat.parse(curentdate)


    override fun save(entity: NoteEntity) {
        disposable = repository.save(entity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {


            }
    }

    override fun loadAllTodos(todo: String) {
        disposable = repository.loadAllTodo(todo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { todoList ->
                if (todoList.isNotEmpty()) {
                    //Filter previous date
                    val filter = todoList.filter { noteEntity ->
                        val date = dateFormat.parse(noteEntity.date)
                        val cal = Calendar.getInstance()
                        if (date != null) {
                            cal.time = date
                        }
                        cal.time >= filterDate

                    }

                    //Sort data with date
                    val sortData = filter.sortedBy { it.date }
                    view.showAllTodos(sortData)

                } else {
                    view.showEmpty()

                }
            }

    }

    override fun deleteAll(todo: String) {
        disposable = repository.deleteAll(todo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {}
    }

    override fun singleDelete(category: String, id: Int) {
        disposable = repository.deleteSingle(category, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    override fun update(int: Int, boolean: Boolean) {
        disposable = repository.update(int, boolean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()


    }

    override fun updateCheckbox(int: Int, boolean: Boolean) {
        disposable = repository.updateCheckBox(int, boolean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    override fun search(title: String, category: String) {
        disposable = repository.searchNote(title, category)
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


}
