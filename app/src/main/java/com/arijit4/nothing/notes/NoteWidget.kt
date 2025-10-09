package com.arijit4.nothing.notes

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontFamily
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.arijit4.nothing.notes.db.Note
import com.arijit4.nothing.notes.db.NoteDAO
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

class NoteWidget : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface NoteWidgetEntryPoint {
        fun noteDao(): NoteDAO
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val noteDao = EntryPoints.get(context, NoteWidgetEntryPoint::class.java).noteDao()

        provideContent {
            GlanceTheme {
                val widgetNote by noteDao.getWidgetNote().collectAsState(initial = null)
                val defaultNote by noteDao.getDefaultNote().collectAsState(initial = null)

                WidgetContent(widgetNote, defaultNote)
            }
        }
    }

    @Composable
    fun WidgetContainer(
        note: Note,
        modifier: GlanceModifier = GlanceModifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .cornerRadius(16.dp)
                .background(GlanceTheme.colors.surface)
                .padding(16.dp),
        ) {
            if (note.title.trim().isNotEmpty()) {
                Text(
                    text = note.title,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = FontFamily("monospace"),
                        color = GlanceTheme.colors.primary
                    ),
                    maxLines = 1
                )
            }
            Text(
                text = note.description,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily("monospace"),
                    color = GlanceTheme.colors.onSurface
                )
            )
        }
    }

    @Composable
    private fun WidgetContent(note: Note?, defaultNote: Note?) {
        if (note == null) {
            if (defaultNote != null)
                WidgetContainer(defaultNote)
            else
                WidgetContainer(
                    Note(
                        title = "",
                        description = "No notes"
                    )
                )
        } else {
            WidgetContainer(note)
        }
    }
}
