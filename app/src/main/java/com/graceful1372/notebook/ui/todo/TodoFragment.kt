package com.graceful1372.notebook.ui.todo


import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.*
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.graceful1372.notebook.R
import com.graceful1372.notebook.data.model.NoteEntity
import com.graceful1372.notebook.data.repository.todo.TodoRepository
import com.graceful1372.notebook.databinding.FragmentTodoBinding
import com.graceful1372.notebook.ui.todo.calendarSheet.CalendarSheetFragment
import com.graceful1372.notebook.utils.CHECK
import com.graceful1372.notebook.utils.DELETE
import com.graceful1372.notebook.utils.calendar.model.CalendarModel
import com.graceful1372.notebook.utils.isSHOW
import com.graceful1372.notebook.utils.toPersian
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class TodoFragment : Fragment(), TodoContract.View, FragmentResultListener {

    //Binding
    private lateinit var binding: FragmentTodoBinding

    @Inject
    lateinit var todoAdapter: AdapterTodo
//    lateinit var todoAdapter: AdapterTodo

    @Inject
    lateinit var entity: NoteEntity

    @Inject
    lateinit var repository: TodoRepository

    @Inject
    lateinit var today: CalendarModel


    //Other
    private val presenter by lazy { TodoPresenter(repository, this, today) }
    var date: String? = ""
    var dateDefualt: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(layoutInflater)

        //Active option menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    // Add menu items here
                    menuInflater.inflate(R.menu.menu_note, menu)
                    val searchTodo = menu.findItem(R.id.toolbar_search)
                    val searchViewTodo =
                        searchTodo.actionView as androidx.appcompat.widget.SearchView
                    searchViewTodo.queryHint = getString(R.string.search)
                    searchViewTodo.setOnQueryTextListener(object :
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String): Boolean {
                            presenter.search(newText, "todo")
                            return true
                        }
                    })
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    // Handle the menu selection
                    return when (menuItem.itemId) {
                        //Delete all to,do
                        R.id.action_delete -> {
                            presenter.deleteAll("todo")
                            true
                        }

                        else -> false
                    }
                }

            }, viewLifecycleOwner, Lifecycle.State.RESUMED
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Get data from bottom Sheet fragment
        parentFragmentManager.setFragmentResultListener(
            "requestCode",
            viewLifecycleOwner,
            this
        )

        //Default date
        val t = today.iranianDay.toString()
        val m = today.iranianMonth.toString()
        val y = today.iranianYear.toString()
        dateDefualt = "$y/$m/$t"

        //Load from database
        presenter.loadAllTodos("todo")

        binding.apply {


            //set default date
            val currentLanguage = Locale.getDefault()
            if (currentLanguage.language == "fa") {
                txtShowDate.text = dateDefualt?.toPersian()

            } else {
                txtShowDate.text = dateDefualt
            }

            //Click on the list
            todoAdapter.setOnItemClickListener { entity, type ->
                when (type) {
                    isSHOW -> {
                        presenter.update(entity.id, !entity.isShow)

                    }
                    DELETE -> {
                        presenter.singleDelete("todo", entity.id)
                    }
                    CHECK -> {

                        presenter.updateCheckbox(entity.id, !entity.isCheck)
                        Toast.makeText(
                            activity,
                            "${entity.isCheck} + ${entity.isCheck}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }


            }

            //Save
            btnSave.setOnClickListener {

                val todo = textInput.text.toString()

                if (todo.isNotEmpty()) {
                    if (date?.isNotEmpty() == true) {


                        entity.todo = todo
                        entity.date = date as String
                        entity.category = "todo"
                        entity.isCheck = false
                        entity.isShow = false
                        presenter.save(entity)

                    } else {

                        entity.todo = todo
                        entity.date = dateDefualt.toString()
                        entity.category = "todo"
                        entity.isCheck = false
                        entity.isShow = false
                        Toast.makeText(activity, dateDefualt, Toast.LENGTH_SHORT).show()

                        //Save
                        presenter.save(entity)
                    }


                }

                textInput.setText("")
            }

            //Open calendar
            btnCalendar.setOnClickListener {

                CalendarSheetFragment().show(parentFragmentManager, CalendarSheetFragment().tag)

            }

            //Hide Show LinearLayout when Scroll
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        layoutAddTodo.visibility = GONE

                        //Use Animation
                        val fadeOut = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
                        layoutAddTodo.startAnimation(fadeOut)


                    } else {
                        layoutAddTodo.visibility = VISIBLE

                        //Use Animation
                        val animationFadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
                        layoutAddTodo.startAnimation(animationFadeIn)
                        animationFadeIn.fillAfter = true
                    }
                }
            })


        }

    }

    data class PojoOfJsonArray(
        val name: String,
        val date: String
    )

    override fun showAllTodos(todos: List<NoteEntity>) {
        binding.emptyLayout.visibility = GONE
        binding.recyclerView.visibility = VISIBLE

        //Group item with data sort
        val groupedMapMap: Map<String, List<NoteEntity>> = todos.groupBy {
            it.date
        }

        val consolidatedList = mutableListOf<AdapterTodo.ListItem>()
        for (date: String in groupedMapMap.keys) {
            consolidatedList.add(AdapterTodo.DateItem(date))
            val groupItems: List<NoteEntity>? = groupedMapMap[date]
            groupItems?.forEach {
                consolidatedList.add(AdapterTodo.GeneralItem(it.id, it.todo, it.isShow, it.isCheck))
            }
        }

        todoAdapter.setData(consolidatedList)
//        val myAdapter = AdapterTodo(consolidatedList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = todoAdapter
        }


    }

    override fun showEmpty() {
        binding.emptyLayout.visibility = VISIBLE
        binding.recyclerView.visibility = GONE

    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {

            "requestCode" -> {
                val resultFromBundle = result.getString("MY_KEY")
                binding.txtShowDate.text = resultFromBundle
                date = resultFromBundle
            }
        }
    }


}