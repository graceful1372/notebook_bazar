package com.graceful1372.notebook.ui.calendar


import com.graceful1372.notebook.base.BasePresenterImpl
import com.graceful1372.notebook.data.repository.calendar.CalendarRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class CalendarPresenter @Inject constructor(
    private val repository: CalendarRepository,
    private val view: CalendarContract.View
) : CalendarContract.Presenter, BasePresenterImpl() {

    override fun loadWithDate(entity: String) {
        disposable = repository.loadTodoWithDate(entity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNotEmpty()) {
                    view.showTodoWithDate(it)

                } else {
                    view.showEmpty()
                }

            }

    }


}
