package com.arijit4.nothing.notes.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val creationTime: Long = System.currentTimeMillis(),
    val showInWidget: Boolean = false,
    val isDeleted: Boolean = false,
    val deletionTime: String? = null
)
