package com.arijit4.nothing.notes.ui.screen.shared.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LabelText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = Modifier.padding(bottom = 4.dp),
        text = text,
        color = Color(0xffC6102E),
        style = MaterialTheme.typography.labelLarge
    )
}