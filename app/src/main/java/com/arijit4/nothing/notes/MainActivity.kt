package com.arijit4.nothing.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.glance.appwidget.updateAll
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.arijit4.nothing.notes.db.Note
import com.arijit4.nothing.notes.db.NoteDAO
import com.arijit4.nothing.notes.ui.screen.AddNoteScreen
import com.arijit4.nothing.notes.ui.screen.DefaultNoteScreen
import com.arijit4.nothing.notes.ui.screen.HomeScreen
import com.arijit4.nothing.notes.ui.screen.SettingScreen
import com.arijit4.nothing.notes.ui.theme.NotesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

val ntype82 = FontFamily(Font(R.font.ntype82))

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var noteDao: NoteDAO

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NotesTheme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()
                Surface(Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = HomeDestination
                    ) {
                        composable<HomeDestination> {
                            HomeScreen(
                                navController,
                                noteDao = noteDao
                            )
                        }
                        composable<AddNoteDestination>(
                            typeMap = mapOf(
                                typeOf<Note?>() to NoteNavType
                            )
                        ) {
                            val args = it.toRoute<AddNoteDestination>()
                            val curNote = args.note
                            AddNoteScreen(
                                note = args.note,
                                onNoteAdded = { note ->
                                    coroutineScope.launch {
                                        if (curNote != null) {
                                            noteDao.updateNote(
                                                note.copy(
                                                    creationTime = curNote.creationTime,
                                                    showInWidget = curNote.showInWidget,
                                                    id = curNote.id,
                                                    isDeleted = curNote.isDeleted,
                                                    deletionTime = curNote.deletionTime
                                                )
                                            )
                                            NoteWidget().updateAll(navController.context)
                                        } else {
                                            noteDao.insertNote(note)
                                        }
                                    }
                                    navController.popBackStack()
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable<SettingsDestination> {
                            SettingScreen(noteDao, navController)
                        }
                        composable<DefaultNoteDestination> {
                            DefaultNoteScreen(noteDao = noteDao, navController = navController)
                        }
                    }
                }
            }
        }
    }
}
