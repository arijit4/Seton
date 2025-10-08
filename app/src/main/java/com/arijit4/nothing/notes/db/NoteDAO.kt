package com.arijit4.nothing.notes.db

import androidx.compose.runtime.collectAsState
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDAO {
    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Transaction
    suspend fun deleteSelectedNotes(notes: List<Note>) {
        notes.forEach {
            deleteNote(it)
        }
    }

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>>


    @Query("SELECT * FROM notes WHERE :condition")
    suspend fun getAllNotesWhichSatisfy(condition: String): List<Note>

    @Query("SELECT * FROM notes WHERE showInWidget = 1")
    fun getWidgetNote(): Flow<Note?>

    @Transaction
    suspend fun setWidgetNote(noteId: Int) {
        clearAllWidgetNotes()
        setNoteForWidget(noteId)
    }

    @Query("UPDATE notes SET showInWidget = 0")
    suspend fun clearAllWidgetNotes()

    @Query("UPDATE notes SET showInWidget = 1 WHERE id = :noteId")
    suspend fun setNoteForWidget(noteId: Int)

    @Query("SELECT * FROM notes WHERE id = -9999")
    fun getDefaultNote(): Flow<Note?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun setupDefaultNote(note: Note)
}