package com.arijit4.nothing.notes

import android.os.Bundle
import androidx.navigation.NavType
import com.arijit4.nothing.notes.db.Note
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val NoteNavType2 = object : NavType<Note?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): Note? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): Note? {
        return Json.decodeFromString<Note>(value)
    }

    override fun put(bundle: Bundle, key: String, value: Note?) {
        bundle.putString(key, Json.encodeToString(value))
    }
}