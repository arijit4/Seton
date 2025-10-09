package com.arijit4.nothing.notes.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.arijit4.nothing.notes.db.Note
import com.arijit4.nothing.notes.ui.screen.shared.components.CustomScaffold
import com.arijit4.nothing.notes.ui.screen.shared.components.CustomTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    note: Note? = null,
    onNoteAdded: (Note) -> Unit,
    navController: NavHostController
) {
    val titleState = remember { mutableStateOf(note?.title ?: "") }
    val descriptionState = remember { mutableStateOf(note?.description ?: "") }

    val TITLE_CHAR_LIMIT = 20
    val DESCRIPTION_CHAR_LIMIT = 200

    val descriptionFocusRequester = remember { FocusRequester() }

    CustomScaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        topBar = {
            CustomTopBar(
                title = if (note == null) "Add Note" else "Edit Note",
                navController = navController,
                actions = {
                    IconButton(
                        onClick = {
                            onNoteAdded(
                                Note(
                                    title = titleState.value,
                                    description = descriptionState.value
                                )
                            )
                        }) {
                        Icon(Icons.Default.Done, contentDescription = "Add Note")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                TextField(
                    value = titleState.value,
                    onValueChange = { if (it.length <= TITLE_CHAR_LIMIT) titleState.value = it },
                    placeholder = { Text("Title", style = MaterialTheme.typography.headlineSmall) },
                    textStyle = MaterialTheme.typography.headlineSmall,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        Text(
                            text = "${titleState.value.length} / $TITLE_CHAR_LIMIT",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { descriptionFocusRequester.requestFocus() })
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding(),
                    value = descriptionState.value,
                    onValueChange = {
                        if (it.length <= DESCRIPTION_CHAR_LIMIT) descriptionState.value = it
                    },
                    placeholder = { Text("Description") },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    supportingText = {
                        Text(
                            text = "${descriptionState.value.length} / $DESCRIPTION_CHAR_LIMIT",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    },
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    capitalization = KeyboardCapitalization.Sentences,
//                    imeAction = ImeAction.Done
//                ),
//                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }
        }
    }
}
