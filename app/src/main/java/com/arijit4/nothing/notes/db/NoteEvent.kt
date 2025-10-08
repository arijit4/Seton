package com.arijit4.nothing.notes.db

sealed interface NoteEvent {
    object AddNote: NoteEvent
    data class SetTitle(val title: String): NoteEvent
    data class SetDescription(val description: String): NoteEvent
    data class SetTags(val tags: List<String>): NoteEvent
    object DeleteNote: NoteEvent
    object UpdateNote: NoteEvent
}