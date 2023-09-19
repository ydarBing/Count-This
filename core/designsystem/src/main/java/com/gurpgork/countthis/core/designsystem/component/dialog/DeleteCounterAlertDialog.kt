package com.gurpgork.countthis.core.designsystem.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gurpgork.countthis.core.designsystem.R

@Composable
fun DeleteCounterAlertDialog(
    counterName: String, onConfirm: () -> Unit, onDismiss: () -> Unit
) {
    CountThisAlertDialog(
        title = stringResource(R.string.dialog_title_delete_counter),
        message = stringResource(R.string.dialog_message_delete_counter, counterName),
        confirmText = stringResource(R.string.dialog_title_confirm_delete_counter),
        onConfirm = { onConfirm() },
        dismissText = stringResource(R.string.dialog_dismiss),
        onDismissRequest = { onDismiss() },
    )
}