package com.gurpgork.countthis.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.gurpgork.countthis.R
import com.gurpgork.countthis.data.ListItemContextMenuOption

@Composable
fun ListContextMenu(
    contextMenuOptions: List<ListItemContextMenuOption>,
    showMenu: Boolean,
    onDismiss: () -> Unit,
    onOptionSelected: (ListItemContextMenuOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        //var menuOpen by remember { mutableStateOf(false)}
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { onDismiss()/*menuOpen = false*/ }
        ) {
            val array: List<String> = stringArrayResource(id = R.array.context_menu_list_options).asList()

            for(option in contextMenuOptions){
                DropdownMenuItem(
                    onClick = {
                        onDismiss()
                        onOptionSelected(option)
                    }
                ) {
                    Text(
                        text = when(option){
                            ListItemContextMenuOption.MOVE -> stringResource(R.string.context_menu_List_move)
                            ListItemContextMenuOption.ADD_TIME -> stringResource(R.string.context_menu_List_add_time)
                            ListItemContextMenuOption.EDIT -> stringResource(R.string.context_menu_List_edit)
                            ListItemContextMenuOption.RESET -> stringResource(R.string.context_menu_List_reset)
                            ListItemContextMenuOption.DELETE -> stringResource(R.string.context_menu_List_delete)
                        })
                }
            }

        }
    }
}