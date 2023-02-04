package com.graceful1372.notebook.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.graceful1372.notebook.data.model.NoteEntity

@Database(entities = [NoteEntity::class] , version = 1 , exportSchema = false)
abstract class NoteDatabase:RoomDatabase() {
    abstract fun noteDao(): NoteDao
}