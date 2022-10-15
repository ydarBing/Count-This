package com.gurpgork.countthis.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

object AppBarAlphas {
    @Composable
    fun translucentBarAlpha(): Float = when {
        // We use a more opaque alpha in light theme
        MaterialTheme.colors.isLight -> 0.98f
        else -> 0.93f
    }
}