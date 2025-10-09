package com.arijit4.nothing.notes.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.arijit4.nothing.notes.BuildConfig
import com.arijit4.nothing.notes.DefaultNoteDestination
import com.arijit4.nothing.notes.Destination
import com.arijit4.nothing.notes.ShapeAndThemeDestination
import com.arijit4.nothing.notes.ui.screen.shared.components.CustomTopBar
import com.arijit4.nothing.notes.ui.screen.shared.components.LabelText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopBar(title = "Settings", navController = navController)
        }
    ) { innerPadding ->
        val items = listOf(
            SettingGroup(
                label = "Widget behaviour",
                items = listOf(
                    SettingObj(
                        title = "Default note",
                        description = "Choose what to show when no note is pinned.",
                        icon = Icons.Default.CropSquare,
                        destination = DefaultNoteDestination
                    ),
                    SettingObj(
                        title = "Widget shape & theme",
                        description = "Choose how your widget will look like.",
                        icon = Icons.Filled.Category,
                        destination = ShapeAndThemeDestination
                    ),
                )
            ),
            SettingGroup(
                label = "Info",
                items = listOf(
                    SettingObj(
                        title = "About",
                        description = "App version: ${BuildConfig.VERSION_NAME}",
                        icon = Icons.Outlined.Info,
                        destination = null
                    )
                )
            )
        )

        SettingContainer(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            items = items,
            navController = navController
        )
    }
}

@Composable
fun SettingCard(
    modifier: Modifier = Modifier,
    label: String? = null,
    settingObj: List<SettingObj>,
    navController: NavHostController
) {
    Column(Modifier.fillMaxWidth()) {
        if (label != null) LabelText(text = label)

        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            settingObj.forEach { setting ->
                SettingItem(
                    setting = setting,
                    onClick = {
                        if (setting.destination != null) {
                            navController.navigate(setting.destination)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SettingContainer(
    modifier: Modifier = Modifier,
    items: List<SettingGroup>,
    navController: NavHostController
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEach { setting ->
            SettingCard(
                modifier = Modifier.fillMaxWidth(),
                label = setting.label,
                settingObj = setting.items,
                navController = navController
            )
        }
    }
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    setting: SettingObj,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = setting.destination != null,
                onClick = { onClick() }
            ),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = setting.title
                )
                Text(
                    text = setting.description,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.56f)
                )
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = setting.icon,
                    contentDescription = "Add",
                    modifier = Modifier
                )
            }
        }
    }
}

data class SettingObj(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val destination: Destination?
)

data class SettingGroup(
    val label: String,
    val items: List<SettingObj>
)