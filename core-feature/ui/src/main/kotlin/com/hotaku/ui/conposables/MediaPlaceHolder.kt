package com.hotaku.ui.conposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MediaPlaceHolder(modifier: Modifier = Modifier) = MediaPlaceHolderImpl(modifier = modifier)

@Composable
private fun MediaPlaceHolderImpl(modifier: Modifier = Modifier) =
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.surfaceDim),
    )
