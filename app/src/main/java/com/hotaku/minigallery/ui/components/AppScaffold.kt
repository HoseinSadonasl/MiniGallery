package com.hotaku.minigallery.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null,
    contentAlignment: Alignment = Alignment.TopStart,
    topAppBar: @Composable (() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit),
) {
    Scaffold(
        topBar = topAppBar?.let { { topAppBar() } } ?: {},
        snackbarHost = snackbarHostState?.let { { SnackbarHost(hostState = snackbarHostState) } } ?: {},
        floatingActionButton = floatingActionButton?.let { { floatingActionButton() } } ?: {}
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .then(modifier),
            contentAlignment = contentAlignment
        ) { content() }
    }
}