package ru.nifontbus.core_ui.component

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush


@Composable
fun surfaceBrush() = Brush.linearGradient(
    colors = listOf(
        MaterialTheme.colors.surface,
        MaterialTheme.colors.onError
    )
)