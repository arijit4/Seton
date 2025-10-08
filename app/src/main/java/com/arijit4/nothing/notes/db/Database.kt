package com.arijit4.nothing.notes.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class],
    version = 2,
    exportSchema = false
)
abstract class NotesDB: RoomDatabase() {
    abstract val dao: NoteDAO

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}