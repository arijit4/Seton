package com.arijit4.nothing.notes.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.arijit4.nothing.notes.db.NoteDAO
import com.arijit4.nothing.notes.ui.screen.shared.components.CustomScaffold
import com.arijit4.nothing.notes.ui.screen.shared.components.InfoCard
import com.arijit4.nothing.notes.ui.screen.shared.components.LabelText
import com.arijit4.nothing.notes.ui.theme.NothingRed
import com.arijit4.nothing.notes.util.DataStoreKeys
import com.arijit4.nothing.notes.util.WIDGET_SHAPES
import com.arijit4.nothing.notes.util.read
import com.arijit4.nothing.notes.util.saveInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WidgetShapeThemeScreen(
    navController: NavHostController,
    noteDao: NoteDAO
) {
    val coroutineScope = rememberCoroutineScope()
    val context = navController.context

    val availableShapes = WIDGET_SHAPES

    var selectedShapeIndex: Int? by remember { mutableStateOf(null) }
    LaunchedEffect(Dispatchers.IO) {
        coroutineScope.launch {
            selectedShapeIndex = context.read(DataStoreKeys.WIDGET_SHAPE.toString())
        }
    }

    val note by noteDao.getDefaultNote().collectAsState(initial = null)
    CustomScaffold(
        title = "Shape & Theme",
        navController = navController
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            LabelText(text = "Preview")
            AnimatedVisibility(note != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val height = 180//dp
                    Column(
                        modifier = Modifier
                            .size(height.dp)
                            .clip(
                                when (selectedShapeIndex) {
                                    0 -> RoundedCornerShape(16.dp)
                                    else -> CircleShape
                                }
                            )
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(
                                when (selectedShapeIndex) {
                                    1 -> ((height / 2 - height / (2 * 1.414f)).toInt()).dp
                                    else -> 0.dp
                                }
                            )
                            .padding(16.dp),
                    ) {
                        if (note!!.title.trim().isNotEmpty()) {
                            Text(
                                text = note!!.title,
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = note!!.description,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            LabelText(text = "Widget shapes")
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(availableShapes) { index, widgetShape ->
                    OutlinedCard(
                        modifier = Modifier
                            .clickable {
                                selectedShapeIndex = index

                                coroutineScope.launch {
                                    context.saveInt(DataStoreKeys.WIDGET_SHAPE.toString(), index)
                                }
                            },
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (index == selectedShapeIndex) NothingRed
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = widgetShape.shape
                                    )
                            )
                            Text(
                                modifier = Modifier.padding(top = 8.dp),
                                text = widgetShape.name,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f)
                            )
                        }
                    }
                }
            }
            LabelText(
                modifier = Modifier.padding(top = 12.dp),
                text = "Widget themes"
            )
            InfoCard(
                title = "Work in progress :)",
                description = "This feature is still under development but is sure to be launched... Stay tuned"
            )
        }
    }
}

data class WidgetShape(
    val name: String,
    val shape: Shape
)