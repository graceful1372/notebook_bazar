package com.graceful1372.notebook.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.graceful1372.notebook.utils.NOTE_TABLE

@Entity(tableName = NOTE_TABLE)
data class NoteEntity (
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var todo:String = "",
    var date:String="",
    var title :String = "",
    var text:String = "" , // The text  entered in  FragmentNote
    var category:String = "" ,


    )
