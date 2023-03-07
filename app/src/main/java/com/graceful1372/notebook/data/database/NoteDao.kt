package com.graceful1372.notebook.data.database

import androidx.room.*
import com.graceful1372.notebook.data.model.NoteEntity
import com.graceful1372.notebook.utils.NOTE_TABLE
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable


@Dao
interface  NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNote(entity: NoteEntity): Completable

    @Delete
    fun deleteNote(entity: NoteEntity): Completable

    @Update
    fun updateNote(entity: NoteEntity): Completable

    @Query("SELECT * FROM $NOTE_TABLE")
    fun getAllNote(): Observable<List<NoteEntity>>

    @Query("SELECT * FROM $NOTE_TABLE WHERE  id  == :id")
    fun getNote(id: Int): Observable<NoteEntity>

    //Delete All
    @Query("DELETE FROM $NOTE_TABLE WHERE category ==:todo")
    fun deleteAllTodo(todo:String): Completable

    @Query("SELECT * FROM $NOTE_TABLE WHERE date == :date")
    fun getTodoWithDate(date: String): Observable<List<NoteEntity>>

    @Query("SELECT * FROM $NOTE_TABLE  WHERE category == :category " )
    fun getNoteString(category: String): Observable<List<NoteEntity>>

    @Query("SELECT * FROM $NOTE_TABLE WHERE title LIKE '%' || :title || '%' AND category LIKE '%' || :category || '%'")
    fun searchNote(title:String,category: String):Observable<List<NoteEntity>>


    @Query("UPDATE $NOTE_TABLE SET isShow=:b WHERE id=:id")
    fun updateIsShow (b: Boolean ,id: Int ) : Completable

    @Query("UPDATE $NOTE_TABLE SET isCheck=:b WHERE id=:id")
    fun updateCheckBox(b: Boolean, id: Int): Completable

    @Query("DELETE FROM $NOTE_TABLE WHERE id=:id AND category =:s ")
    fun deleteSingle(s: String, id: Int): Completable
}
