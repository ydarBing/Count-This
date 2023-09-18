package com.gurpgork.countthis.core.designsystem.component

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import com.gurpgork.countthis.core.designsystem.R

@Composable
fun ListContextMenu(
    contextMenuOptions: List<ListItemContextMenuOption>,
    dpOffset: DpOffset,
    onDismiss: () -> Unit,
    onOptionSelected: (ListItemContextMenuOption) -> Unit,
    showMenu: Boolean,
) {
    DropdownMenu(
        offset = dpOffset,
        expanded = showMenu,
        onDismissRequest = onDismiss,
    ) {
        val array: List<String> =
            stringArrayResource(id = R.array.context_menu_list_options).asList()

        contextMenuOptions.forEach { option ->
            DropdownMenuItem(
                onClick = {
                    onOptionSelected.invoke(option)
                    onDismiss.invoke()
                },
                text = {
                    Text(
                        text = when (option) {
                            ListItemContextMenuOption.MOVE -> stringResource(R.string.context_menu_List_move)
                            ListItemContextMenuOption.ADD_TIME -> stringResource(R.string.context_menu_List_add_time)
                            ListItemContextMenuOption.EDIT -> stringResource(R.string.context_menu_List_edit)
                            ListItemContextMenuOption.RESET -> stringResource(R.string.context_menu_List_reset)
                            ListItemContextMenuOption.DELETE -> stringResource(R.string.context_menu_List_delete)
                            ListItemContextMenuOption.NONE -> "THIS SHOULDN'T BE AN OPTION!!"
                        }
                    )
                }
            )
        }
    }
}