package com.gurpgork.countthis.compose

import androidx.compose.material.*
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Wrapper around [Snackbar] to make it swipe-dismissable,
 * using [SwipeToDismiss].
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)
@Composable
fun SwipeDismissSnackbar(
    data: SnackbarData,
    onDismiss: (() -> Unit)? = null,
    snackbar: @Composable (SnackbarData) -> Unit = { Snackbar(it) }
) {
    val dismissState = rememberDismissState {
        if (it != DismissValue.Default) {
            // First dismiss the snackbar
            data.dismiss()
            // Then invoke the callback
            onDismiss?.invoke()
        }
        true
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = {},
        dismissContent = { snackbar(data) }
    )
}

@Composable
fun SwipeDismissSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { hostState.currentSnackbarData?.dismiss() },
    snackbar: @Composable (SnackbarData) -> Unit = { data ->
        SwipeDismissSnackbar(
            onDismiss = onDismiss,
            data = data
        )
    }
) {
    SnackbarHost(
        hostState = hostState,
        snackbar = snackbar,
        modifier = modifier
    )
}
