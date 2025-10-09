package com.arijit4.nothing.notes.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.arijit4.nothing.notes.ui.screen.shared.components.CustomScaffold
import com.arijit4.nothing.notes.ui.screen.shared.components.LabelText

@Composable
fun WidgetShapeThemeScreen(
    navController: NavHostController,
) {
    val availableShapes = listOf(
        WidgetShape("Rectangle", RectangleShape),
        WidgetShape("Rounded Rectangle", RoundedCornerShape(12.dp)),
        WidgetShape("Circle", CircleShape)
    )
    CustomScaffold(title = "Shape & Theme") { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                LabelText(text = "Widget shapes")
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(availableShapes) { widgetShape ->
                        OutlinedCard(
                            modifier = Modifier
                                .clickable {

                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(90.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
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
            }
        }
    }
}

data class WidgetShape(
    val name: String,
    val shape: Shape
)