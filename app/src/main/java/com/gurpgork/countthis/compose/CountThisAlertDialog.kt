package com.gurpgork.countthis.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun CountThisAlertDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    dismissText: String,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            /*OutlinedButton*/TextButton(onClick = { onConfirm() }) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            /*OutlinedButton*/TextButton(onClick = { onDismissRequest() }) {
                Text(text = dismissText)
            }
        },
        onDismissRequest = onDismissRequest,
    )
//    AlertDialog(
//        title = { Text(text = title) },
//        text = { Text(text = message) },
//        confirmButton = {
//            OutlinedButton(onClick = { onConfirm() }) {
//                Text(text = confirmText)
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = { onDismissRequest() }) {
//                Text(text = dismissText)
//            }
//        },
//        onDismissRequest = onDismissRequest
//    )
}