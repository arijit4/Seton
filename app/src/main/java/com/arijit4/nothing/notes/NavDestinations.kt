package com.arijit4.nothing.notes

import com.arijit4.nothing.notes.db.Note
import kotlinx.serialization.Serializable

@Serializable
object Screen {
    @Serializable
    data object Home

    @Serializable
    data object Settings

    @Serializable
    data class AddNote(
        val note: Note? = null
    )
}