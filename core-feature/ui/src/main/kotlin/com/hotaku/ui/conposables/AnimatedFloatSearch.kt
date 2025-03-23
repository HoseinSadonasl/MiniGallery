package com.hotaku.ui.conposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import com.hotaku.core_feature.ui.R

@Composable
fun BoxScope.AnimatedFloatSearch(
    modifier: Modifier = Modifier,
    visible: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
) {
    AnimatedFloatSearchImpl(
        modifier = modifier,
        visible = visible,
        value = value,
        onValueChange = onValueChange,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.AnimatedFloatSearchImpl(
    modifier: Modifier = Modifier,
    visible: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            0f to MaterialTheme.colorScheme.surface.copy(alpha = 1f),
                            1f to MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                        ),
                    )
                    .then(modifier),
            contentAlignment = Alignment.Center,
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                hint = stringResource(id = R.string.snimated_search_hint),
                value = value,
                onValueChange = onValueChange,
                endIcon = Icons.Outlined.Search,
            )
        }
    }
}
