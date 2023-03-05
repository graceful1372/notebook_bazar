package com.graceful1372.notebook.ui.note

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.annotation.NonNull
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.graceful1372.notebook.R
import com.graceful1372.notebook.databinding.FragmentNoteBinding
import com.graceful1372.notebook.data.model.NoteEntity
import com.graceful1372.notebook.data.repository.note.NoteRepository
import com.graceful1372.notebook.ui.note.add.AddNoteFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NoteFragment : Fragment(), NoteContract.View {
    //Binding
    private lateinit var binding: FragmentNoteBinding

    //inject
    @Inject
    lateinit var adapterNote: AdapterNote

    @Inject
    lateinit var repository: NoteRepository

    //Other
    private val presenter by lazy { NotePresenter(repository, this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.loadNote("note")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(layoutInflater)


        //Active toolbar item
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    // Add menu items here
                    menuInflater.inflate(R.menu.menu_note, menu)
                    val search = menu.findItem(R.id.toolbar_search)
                    val searchView = search.actionView as SearchView
                    searchView.queryHint = getString(R.string.search)
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        //Search fun
                        override fun onQueryTextChange(newText: String): Boolean {

                            //Search file note with title Filter
                            presenter.searchNote(newText, "note")

                            return true
                        }
                    })



                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    // Handle the menu selection
                    return when (menuItem.itemId) {
                        R.id.toolbar_search -> {
                            true
                        }
                        /* R.id.toolbar_filter -> {
                             // loadTasks(true)
                             true
                         }*/
                        else -> false
                    }
                }

                /*If this line of code is removed, the item toolbar will be displayed repeatedly for each fragment.
                Actually, these items toolbar show only this fragment.*/
            }, viewLifecycleOwner, Lifecycle.State.RESUMED
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Load data from database
        presenter.loadNote("note")

        //InitView
        binding.apply {

            //Click on fab
            btnAddNote.setOnClickListener {
                AddNoteFragment().show(parentFragmentManager, AddNoteFragment().tag)
            }

            //Hide ,Show fab when Scroll
            noteList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        btnAddNote.visibility = View.GONE

                        //Use Animation
                        val fadeOut = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
                        btnAddNote.startAnimation(fadeOut)


                    } else {
                        btnAddNote.visibility = View.VISIBLE

                        //Use Animation
                        val animationFadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
                        btnAddNote.startAnimation(animationFadeIn)
                        animationFadeIn.fillAfter = true
                    }
                }
            })

            //Click on item recyclerview
            adapterNote.setOnItemClickListener {
                presenter.delete(it)

            }
        }


    }

    override fun showNote(note: List<NoteEntity>) {
        binding.noteList.visibility = View.VISIBLE
        binding.emptyNote.visibility = View.GONE
        adapterNote.setData(note)
        binding.noteList.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = adapterNote
        }

    }

    override fun showEmpty() {
        binding.noteList.visibility = View.GONE
        binding.emptyNote.visibility = View.VISIBLE
    }


}