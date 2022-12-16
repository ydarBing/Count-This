package com.gurpgork.countthis.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.gurpgork.countthis.R
import com.gurpgork.countthis.data.ListItemContextMenuOption

@Composable
fun ListContextMenu(
    contextMenuOptions: List<ListItemContextMenuOption>,
    offset: Offset,
    onDismiss: () -> Unit,
    onOptionSelected: (ListItemContextMenuOption) -> Unit,
    modifier: Modifier = Modifier,
//    showMenu: Boolean,
) {
    Box(modifier = modifier) {
        //var menuOpen by remember { mutableStateOf(false)}

        DropdownMenu(
            //TODO this offset it not working as intended, scaling needed?
//            offset = DpOffset(offset.x.dp, (-10).dp),
            expanded = true,//showMenu,
            onDismissRequest = onDismiss,//{ onDismiss() }
        ) {
            val array: List<String> = stringArrayResource(id = R.array.context_menu_list_options).asList()

            for(option in contextMenuOptions){
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected.invoke(option)
                        onDismiss.invoke()
                    },
                    text = {
                        Text(
                            text = when(option){
                                ListItemContextMenuOption.MOVE -> stringResource(R.string.context_menu_List_move)
                                ListItemContextMenuOption.ADD_TIME -> stringResource(R.string.context_menu_List_add_time)
                                ListItemContextMenuOption.EDIT -> stringResource(R.string.context_menu_List_edit)
                                ListItemContextMenuOption.RESET -> stringResource(R.string.context_menu_List_reset)
                                ListItemContextMenuOption.DELETE -> stringResource(R.string.context_menu_List_delete)
                                ListItemContextMenuOption.NONE -> "THIS SHOULDN'T BE AN OPTION!!"
                            })
                    }
                )
            }

        }
    }
}