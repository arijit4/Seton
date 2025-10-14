package com.arijit4.nothing.notes.ui.screen.shared.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun CustomScaffold(
    modifier: Modifier = Modifier,
    title: String? = null,
    navController: NavHostController? = null,
    topBar: @Composable () -> Unit = {
        if (title != null) CustomTopBar(title = title, navController = navController)
    },
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar,
        floatingActionButton = floatingActionButton
    ) { innerPadding ->
        content(innerPadding)
    }
}