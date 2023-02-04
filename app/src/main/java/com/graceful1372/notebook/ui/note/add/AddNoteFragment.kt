package com.graceful1372.notebook.ui.note.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.graceful1372.notebook.databinding.FragmentAddNoteBinding
import com.graceful1372.notebook.data.model.NoteEntity
import com.graceful1372.notebook.data.repository.note.NoteRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddNoteFragment : BottomSheetDialogFragment() {
    //Binding
    private lateinit var binding: FragmentAddNoteBinding

    @Inject
    lateinit var repository: NoteRepository

    @Inject
    lateinit var entity: NoteEntity

    private val presenter by lazy { AddNotePresenter(repository) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitView
        binding.apply {


            //save
            btnSaveNote.setOnClickListener {
                val title = titleNote.text.toString()
                val text = textNote.text.toString()
                if (title.isNotEmpty() && text.isNotEmpty()) {
                    entity.id=0
                    entity.title = title
                    entity.text = text
                    entity.category = "note"
                    presenter.save(entity)
                    dismiss()
                }

            }

        }
    }

}