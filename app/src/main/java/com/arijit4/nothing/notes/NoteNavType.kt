package com.arijit4.nothing.notes

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.arijit4.nothing.notes.db.Note
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val NoteNavType = object : NavType<Note?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): Note? {
        val jsonString = bundle.getString(key)

        if (jsonString.isNullOrEmpty() || jsonString == "null") {
            return null
        }

        return try {
            Json.decodeFromString<Note>(jsonString)
        } catch (e: Exception) {
            null
        }
    }

    override fun put(bundle: Bundle, key: String, value: Note?) {
        val jsonString = Json.encodeToString(value)
        bundle.putString(key, jsonString)
    }

    override fun parseValue(value: String): Note? {
        return Json.decodeFromString<Note>(Uri.decode(value))

    }

    override fun serializeAsValue(value: Note?): String {
        return Uri.encode(Json.encodeToString(value))
    }
}
