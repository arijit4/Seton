package com.arijit4.nothing.notes.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkRemove
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.updateAll
import androidx.navigation.NavHostController
import com.arijit4.nothing.notes.NoteWidget
import com.arijit4.nothing.notes.Screen
import com.arijit4.nothing.notes.db.Note
import com.arijit4.nothing.notes.db.NoteDAO
import com.arijit4.nothing.notes.ui.theme.NothingRed
import com.arijit4.nothing.notes.util.toTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    noteDao: NoteDAO
) {
    var selected by remember { mutableStateOf(listOf<Note>()) }
    val coroutineScope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteClicked by remember { mutableStateOf(false) }


    val defaultDescriptions = listOf(
        "I've a grand memory for forgetting.\n~ L. J. Peter ",
        "Forgetfulness is a form of freedom.\n~ Khalil Gibran",
        "My memory is like a sieve. It filters out the important stuff and leaves the rest."
    )

    val defaultNote = Note(
        id = -9999,
        title = "",
        description = defaultDescriptions[2]
//            defaultDescriptions[
//            Random(System.currentTimeMillis()).nextInt(0, defaultDescriptions.size)
//        ]
    )

    LaunchedEffect(Dispatchers.IO) {
        coroutineScope.launch {
            noteDao.setupDefaultNote(defaultNote)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Seton") },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(
                        containerColor = Color.Transparent
                    ),
                actions = {
                    AnimatedVisibility(selected.size == 1) {
                        val pinned = selected.firstOrNull()?.showInWidget
                        if (pinned != null) {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        noteDao.updateNote(
                                            selected[0].copy(
                                                showInWidget = !pinned
                                            )
                                        )
                                        selected = emptyList()
                                        NoteWidget().updateAll(navController.context)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.run {
                                        if (pinned) BookmarkRemove
                                        else BookmarkAdd
                                    },
                                    contentDescription = "Add"
                                )
                            }
                        }
                    }
                    AnimatedVisibility(selected.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                showDeleteDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                    AnimatedVisibility(selected.isEmpty()) {
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.Settings)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Add"
                            )
                        }
                    }
                },
                navigationIcon = {
                    AnimatedVisibility(selected.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                selected = emptyList()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = NothingRed,
                onClick = {
                    navController.navigate(Screen.AddNote())
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            val notes by noteDao.getAllNotes().collectAsState(initial = emptyList())

            NotesGrid(
                notes = notes.filter { it.id != -9999 },
                selectedNotes = selected,
                deleteConfirmed = deleteClicked,
                onNoteClicked = { note ->
                    navController.navigate(Screen.AddNote(note = note))
                },
                onNoteLongClicked = { note ->
                    selected = if (selected.contains(note))
                        selected - note
                    else selected + note
                }
            )
        }

    }
    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                deleteClicked = true
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }

    LaunchedEffect(key1 = deleteClicked) {
        coroutineScope.launch {
            noteDao.deleteSelectedNotes(selected)
            selected = emptyList()
            NoteWidget().updateAll(navController.context)

            deleteClicked = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = { Text("Delete selected notes?") },
        text = { Text("Are you sure you want to delete the selected notes? This action cannot be undone.") },
        confirmButton = {
            Button(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        onDismissRequest = {
            onDismiss()
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    onNoteClicked: (Note) -> Unit,
    onNoteLongClicked: (Note) -> Unit,
    selected: Boolean,
    inSelectionMode: Boolean,
    deleteConfirmed: Boolean
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) NothingRed
            else MaterialTheme.colorScheme.outlineVariant

        )
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        if (inSelectionMode) onNoteLongClicked(note)
                        else onNoteClicked(note)
                    },
                    onLongClick = { onNoteLongClicked(note) }
                )
                .padding(bottom = 16.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(16.dp),
            ) {
                Spacer(Modifier.weight(1f))
                if (note.showInWidget) {
                    Box(
                        Modifier
                            .padding(top = 8.dp, end = 8.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(NothingRed)
                    )
                }
            }
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = note.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f),
                    maxLines = 4,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = "at " + note.creationTime.toTime("h:mm aa"),
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.36f)
                )
            }
        }
    }
}

@Composable
private fun rememberTransitionState(): MutableTransitionState<Boolean> {
    return remember { MutableTransitionState(false).apply { targetState = true } }
}

@Composable
fun NotesGrid(
    notes: List<Note>,
    onNoteClicked: (Note) -> Unit,
    onNoteLongClicked: (Note) -> Unit,
    selectedNotes: List<Note>,
    deleteConfirmed: Boolean
) {
    if (notes.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No notes",
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f)
            )
        }
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .padding(4.dp)
                .animateContentSize(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalItemSpacing = 4.dp
        ) {
            items(notes.size) { index ->
                NoteItem(
                    note = notes[index],
                    selected = selectedNotes.contains(notes[index]),
                    deleteConfirmed = deleteConfirmed,
                    onNoteClicked = onNoteClicked,
                    onNoteLongClicked = onNoteLongClicked,
                    inSelectionMode = selectedNotes.isNotEmpty()
                )
            }
        }
    }
}
