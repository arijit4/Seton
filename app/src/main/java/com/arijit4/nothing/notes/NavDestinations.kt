package com.arijit4.nothing.notes

import com.arijit4.nothing.notes.db.Note
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination


@Serializable
data object HomeDestination : Destination

@Serializable
data object SettingsDestination : Destination

@Serializable
data object DefaultNoteDestination : Destination

@Serializable
data class AddNoteDestination(
    val note: Note? = null
) : Destination