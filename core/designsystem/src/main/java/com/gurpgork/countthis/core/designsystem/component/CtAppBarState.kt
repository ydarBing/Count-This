package com.gurpgork.countthis.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

data class CtAppBarState(
    val title: String = "",
    val navigationIcon: (@Composable () -> Unit) = {},
    val actions: (@Composable RowScope.() -> Unit) = { }
)