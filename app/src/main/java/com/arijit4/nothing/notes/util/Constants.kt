package com.arijit4.nothing.notes.util

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.arijit4.nothing.notes.ui.screen.WidgetShape

val TITLE_CHAR_LIMIT = 20
val DESCRIPTION_CHAR_LIMIT = 200

val WIDGET_SHAPES = listOf(
//        WidgetShape("Rectangle", RectangleShape),
    WidgetShape("Rounded Rectangle", RoundedCornerShape(12.dp)),
    WidgetShape("Circle", CircleShape)
)