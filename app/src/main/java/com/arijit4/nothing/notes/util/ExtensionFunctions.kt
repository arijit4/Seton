package com.arijit4.nothing.notes.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Long.toTime(format: String): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(Date(this))
}
