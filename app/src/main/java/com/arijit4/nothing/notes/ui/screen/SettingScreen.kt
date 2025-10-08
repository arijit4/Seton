package com.arijit4.nothing.notes.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.glance.LocalContext
import androidx.glance.appwidget.updateAll
import androidx.navigation.NavHostController
import com.arijit4.nothing.notes.NoteWidget
import com.arijit4.nothing.notes.Screen
import com.arijit4.nothing.notes.db.Note
import com.arijit4.nothing.notes.db.NoteDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    noteDao: NoteDAO,
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(
                        containerColor = Color.Transparent
                    ),
                actions = {

                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Add"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val TITLE_CHAR_LIMIT = 20
        val DESCRIPTION_CHAR_LIMIT = 200

        val defaultNote by noteDao.getDefaultNote().collectAsState(initial = null)

        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }

        if (defaultNote != null) {
            title = defaultNote!!.title
            description = defaultNote!!.description
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Default note",
                color = Color(0xffC6102E),
                style = MaterialTheme.typography.labelLarge
            )
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = { if (it.length <= TITLE_CHAR_LIMIT) title = it },
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
                            text = "${title.length} / $TITLE_CHAR_LIMIT",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    },
                    singleLine = true
                )
                TextField(
                    value = description,
                    onValueChange = { if (it.length <= DESCRIPTION_CHAR_LIMIT) description = it },
                    placeholder = { Text("Description") },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    supportingText = {
                        Text(
                            text = "${description.length} / $DESCRIPTION_CHAR_LIMIT",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    }
                )
                Button(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp),
                    onClick = {
                        coroutineScope.launch {
                            noteDao.updateNote(
                                defaultNote!!.copy(
                                    title = title,
                                    description = description
                                )
                            )
                            NoteWidget().updateAll(navController.context)
                        }
                        navController.popBackStack()
                    }
                ) {
                    Text("Update")
                }
            }
        }
    }
}

