package com.gurpgork.countthis.feature.allcounters

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.gurpgork.countthis.core.data.model.CounterWithIncrementInfo
import com.gurpgork.countthis.core.designsystem.component.AnimatingFabContent
import com.gurpgork.countthis.core.designsystem.component.CtAppBarState
import com.gurpgork.countthis.core.designsystem.component.Layout
import com.gurpgork.countthis.core.designsystem.component.ListContextMenu
import com.gurpgork.countthis.core.designsystem.component.ListItemContextMenuOption
import com.gurpgork.countthis.core.designsystem.component.SwipeDismissSnackbarHost
import com.gurpgork.countthis.core.designsystem.component.conditional
import com.gurpgork.countthis.core.designsystem.component.dialog.AddTimeDialog
import com.gurpgork.countthis.core.designsystem.component.dialog.AddTimeInformation
import com.gurpgork.countthis.core.designsystem.component.dialog.DeleteCounterAlertDialog
import com.gurpgork.countthis.core.designsystem.component.dialog.ResetCounterAlertDialog
import com.gurpgork.countthis.core.designsystem.component.reorderablelazylist.ReorderableItem
import com.gurpgork.countthis.core.designsystem.component.reorderablelazylist.detectReorderAfterLongPress
import com.gurpgork.countthis.core.designsystem.component.reorderablelazylist.rememberReorderableLazyListState
import com.gurpgork.countthis.core.designsystem.component.reorderablelazylist.reorderable
import com.gurpgork.countthis.core.designsystem.icon.CtIcons
import com.gurpgork.countthis.core.model.data.CREATE_COUNTER_ID
import com.gurpgork.countthis.core.model.data.CtLocation
import com.gurpgork.countthis.core.model.data.INVALID_LIST_INDEX
import com.gurpgork.countthis.core.model.data.SortOption
import com.gurpgork.countthis.core.ui.LocalCountThisDateFormatter

@Composable
internal fun AllCountersRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    addEditCounter: (counterId: Long, listIndex: Int) -> Unit,
    openCounter: (counterId: Long) -> Unit,
    openUser: () -> Unit,
    onSettingsClicked: () -> Unit,
    onComposing: (CtAppBarState) -> Unit,
) {
    AllCounters(
        viewModel = hiltViewModel(),
        addEditCounter = addEditCounter,
        openCounter = openCounter,
        openUser = openUser,
        onComposing = onComposing,
        onSettingsClicked = onSettingsClicked,
    )

}

@Composable
internal fun AllCounters(
    viewModel: CounterListViewModel,
    addEditCounter: (counterId: Long, listIndex: Int) -> Unit,
    openCounter: (counterId: Long) -> Unit,
    openUser: () -> Unit,
    onSettingsClicked: () -> Unit,
    onComposing: (CtAppBarState) -> Unit,
) {
    val context = LocalContext.current

    val viewState by viewModel.state.collectAsStateWithLifecycle()

    val initialLatLng = if (viewState.mostRecentLocation != null) LatLng(
        viewState.mostRecentLocation!!.latitude, viewState.mostRecentLocation!!.longitude
    )
    else LatLng(
        viewModel.getDefaultLocation().latitude, viewModel.getDefaultLocation().longitude
    )

    AllCountersScreen(
        viewState = viewState,
        openUser = openUser,
        openCounter = openCounter,
        addEditCounter = addEditCounter,
        onMessageShown = { viewModel.clearMessage(it) },
        onContextMenuOptionSelected = { id, listIndex, option, addTimeInfo ->
            viewModel.handleContextMenuOptionSelected(
                id, listIndex, option, addTimeInfo
            )
        },
        onIncrementCounter = { id, location -> viewModel.incrementCounter(id, location) },
        onDecrementCounter = { id, location -> viewModel.decrementCounter(id, location) },
        onSortSelected = { viewModel.setSort(it) },
        onSortOrderClicked = { viewModel.toggleSortAsc() },
        pickerInitialLatLng = initialLatLng,
        pickerLocationAddress = viewState.locationPickerAddressQuery,
        locationQueryChanged = { newAddr -> viewModel.onTextChanged(context, newAddr) },
        onLocationPickerMoved = { latLng -> viewModel.updatePickerLocation(context, latLng) },
        onListReorder = viewModel::reorderList,
        onSettingsClicked = onSettingsClicked,
        onComposing = onComposing,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class,
)
@Composable
internal fun AllCountersScreen(
    viewState: AllCountersViewState,
    addEditCounter: (counterId: Long, listIndex: Int) -> Unit,
    openCounter: (counterId: Long) -> Unit,
    onContextMenuOptionSelected: (counterId: Long, listIndex: Int, ListItemContextMenuOption, addTimeInfo: AddTimeInformation?) -> Unit,
    onIncrementCounter: (Long, CtLocation?) -> Unit,
    onDecrementCounter: (Long, CtLocation?) -> Unit,
    onSortSelected: (SortOption) -> Unit,
    onSortOrderClicked: () -> Unit,
    openUser: () -> Unit,
    pickerInitialLatLng: LatLng,
    onMessageShown: (id: Long) -> Unit,
    pickerLocationAddress: String,
    locationQueryChanged: (String) -> Unit,
    onLocationPickerMoved: (LatLng) -> Unit,
    onListReorder: (Long, Int, Long, Int) -> Unit,
    onSettingsClicked: () -> Unit,
    onComposing: (CtAppBarState) -> Unit,
) {
    var menuOptionSelected by remember { mutableStateOf(ListItemContextMenuOption.NONE) }
    var counterContextMenuId by remember { mutableLongStateOf(0L) }
    var counterContextMenuListIndex by remember { mutableIntStateOf(INVALID_LIST_INDEX) }
    var counterContextMenuName by remember { mutableStateOf("") }
    var counterContextMenuTrackingLocation by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    var showSorts by remember { mutableStateOf(false) }
    var inMoveState by remember { mutableStateOf(false) }

    val state = rememberReorderableLazyListState(
        onDragEnd = { from, to ->
            Log.d("DRAG_END", "$from -> $to")
            // should now update database as we have finished dragging item
        },
        onMove = { from, to ->
            // still dragging but swapping indices
            Log.d(
                "MOVE",
                "From as Long: ${from.key as Long} keys: ${from.key} -> ${to.key} indices: $from -> $to"
            )
            onListReorder(from.key as Long, from.index, to.key as Long, to.index)
        })

    val dismissSnackbarState = rememberDismissState(
        confirmValueChange = { value ->
            if (value != DismissValue.Default) {
                snackbarHostState.currentSnackbarData?.dismiss()
                true
            } else {
                false
            }
        },
    )
    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            onMessageShown(message.id)
        }
    }

    LaunchedEffect(key1 = inMoveState) {
        onComposing(
            CtAppBarState(title = "Count This", navigationIcon = {
                //TODO functionality not in to revert reordering, so currently not showing confusing icons
//                if (inMoveState) {
//                    IconButton(onClick = { inMoveState = false }) {
//                        Icon(
//                            imageVector = CtIcons.Close,
//                            contentDescription = "Close/Cancel Reordering List"
//                        )
//                    }
//                }
            }, actions = {
                if (inMoveState) {
                    IconButton(onClick = { inMoveState = false }) {
                        Icon(
                            imageVector = CtIcons.Save, contentDescription = "Save Reordered List"
                        )
                    }
                } else {
                    IconButton(onClick = { showSorts = !showSorts }) {
                        Icon(
                            imageVector = CtIcons.Sort, contentDescription = "Sort Counters Icon"
                        )
                    }
                    IconButton(onClick = { onSettingsClicked() }) {
                        Icon(
                            imageVector = CtIcons.Settings,
                            contentDescription = "Settings Icon",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            })
        )
    }

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )


    Scaffold(floatingActionButton = {
        val extended by remember {
            derivedStateOf {
                state.listState.firstVisibleItemIndex > 0
            }
        }
        CounterListFab(
            extended = { extended },
            onClick = {
                if (inMoveState) {
                    inMoveState = false
                } else {
                    addEditCounter(CREATE_COUNTER_ID, viewState.countersWithInfo.size)
                }
            },
            icon = if (inMoveState) CtIcons.Save else CtIcons.Add,
            textDescription = if (inMoveState) stringResource(id = R.string.save) else stringResource(
                id = R.string.add
            ),
        )
    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) { data ->
            SwipeToDismiss(
                state = dismissSnackbarState,
                background = {},
                dismissContent = { Snackbar(snackbarData = data) },
                modifier = Modifier
                    .padding(horizontal = Layout.bodyMargin)
                    .fillMaxWidth()
            )
        }
        SwipeDismissSnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .padding(horizontal = Layout.bodyMargin)
                .fillMaxWidth()
        )
    }) { contentPadding ->
        Column(modifier = Modifier.consumeWindowInsets(contentPadding)) {
            AnimatedVisibility(visible = showSorts && !inMoveState) {
                SortOptions(
                    currentSort = viewState.sort,
                    sortAsc = viewState.sortAsc,
                    onSortOrderClicked = onSortOrderClicked,
                    onSortSelected = { sort, pressedAgain ->
                        if (pressedAgain) onSortOrderClicked()
                        else onSortSelected(sort)
                    },
                )
            }

            LazyColumn(
                state = state.listState,//listState,
                modifier = Modifier
                    .fillMaxSize()
                    .reorderable(state)
                    .detectReorderAfterLongPress(state)
                    .testTag(CounterListTestTag),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                reverseLayout = viewState.sortAsc,
            ) {
                items(
                    items = viewState.countersWithInfo,
                    key = { it.counter.id },
                    contentType = { "counterListItem" },
                ) { counter ->
                    ReorderableItem(
                        reorderableState = state,
                        key = counter.counter.id
                    ) { isDragging ->
                        val elevation = animateDpAsState(
                            if (isDragging) 16.dp else 0.dp, label = "ReorderElevation"
                        )

                        CounterRow(modifier = Modifier
                            .animateItemPlacement()
                            .shadow(elevation.value),
                            inMoveState = inMoveState,
                            useButtonIncrements = viewState.useButtonIncrements,
                            onCounterClick = { openCounter(counter.counter.id) },
                            onIncrementCounter = {
                                onIncrementCounter(
                                    counter.counter.id, viewState.mostRecentLocation
                                )
                            },
                            onDecrementCounter = {
                                onDecrementCounter(
                                    counter.counter.id, viewState.mostRecentLocation
                                )
                            },
                            counter = counter,
                            locationPermissionsState = locationPermissionsState,
                            contextMenuOptions = viewState.availableContextMenuOptions,
                            onContextMenuOptionSelected = { option ->
                                menuOptionSelected = option
                                counterContextMenuId = counter.counter.id
                                counterContextMenuListIndex = counter.counter.listIndex
                                counterContextMenuName = counter.counter.name
                                counterContextMenuTrackingLocation = counter.counter.trackLocation
                            })
                    }
                }
            }
        }
    }

    when (menuOptionSelected) {
        ListItemContextMenuOption.NONE -> Unit // don't do anything
        ListItemContextMenuOption.EDIT -> {
            addEditCounter(counterContextMenuId, counterContextMenuListIndex)
            menuOptionSelected = ListItemContextMenuOption.NONE
        }

        ListItemContextMenuOption.MOVE -> {
            // immediately show how user custom sorted them so they can adjust to their liking
            onSortSelected(SortOption.USER_SORTED)
            inMoveState = true
            menuOptionSelected = ListItemContextMenuOption.NONE
        }

        ListItemContextMenuOption.ADD_TIME -> OpenAddTimeDialog(counterName = counterContextMenuName,
            trackingLocation = counterContextMenuTrackingLocation,
            pickerInitialLatLng = pickerInitialLatLng,
//                pickerCurrentLocation = pickerLocation,
            pickerLocationAddress = pickerLocationAddress,
            locationQueryChanged = locationQueryChanged,
            onLocationChanged = onLocationPickerMoved,
            onConfirm = { addTimeInfo ->
                onContextMenuOptionSelected(
                    counterContextMenuId,
                    counterContextMenuListIndex,
                    menuOptionSelected,
                    addTimeInfo
                )
                menuOptionSelected = ListItemContextMenuOption.NONE
            },
            onDismiss = { menuOptionSelected = ListItemContextMenuOption.NONE })

        ListItemContextMenuOption.RESET -> ResetCounterAlertDialog(counterName = counterContextMenuName,
            onConfirm = {
                onContextMenuOptionSelected(
                    counterContextMenuId, counterContextMenuListIndex, menuOptionSelected, null
                )
                menuOptionSelected = ListItemContextMenuOption.NONE
            },
            onDismiss = { menuOptionSelected = ListItemContextMenuOption.NONE })

        ListItemContextMenuOption.DELETE -> DeleteCounterAlertDialog(counterName = counterContextMenuName,
            onConfirm = {
                onContextMenuOptionSelected(
                    counterContextMenuId, counterContextMenuListIndex, menuOptionSelected, null
                )
                menuOptionSelected = ListItemContextMenuOption.NONE
            },
            onDismiss = { menuOptionSelected = ListItemContextMenuOption.NONE })
    }
}

const val CounterListTestTag = "CounterListTestTag"


@Composable
fun SortOptions(
    currentSort: SortOption,
    sortAsc: Boolean,
    onSortSelected: (SortOption, Boolean) -> Unit,
    onSortOrderClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TextButton(
            onClick = {
                onSortSelected(SortOption.ALPHABETICAL, currentSort == SortOption.ALPHABETICAL)
            }, colors = ButtonColors(
                containerColor = if (currentSort == SortOption.ALPHABETICAL) MaterialTheme.colorScheme.surfaceVariant
                else MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = Color.DarkGray,
                disabledContentColor = Color.Gray,
            )
        ) {
            Text(
                "Name" + if (currentSort == SortOption.ALPHABETICAL) {
                    ""
//                    if (sortAsc) {
//                        " (A - Z)"
//                    } else {
//                        " (Z - A)"
//                    }
                } else {
                    ""
                },
                Modifier
                    .fillMaxHeight()
                    .padding(4.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
        }
        TextButton(
            onClick = {
                onSortSelected(SortOption.DATE_ADDED, currentSort == SortOption.DATE_ADDED)
            }, colors = ButtonColors(
                containerColor = if (currentSort == SortOption.DATE_ADDED) MaterialTheme.colorScheme.surfaceVariant
                else MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = Color.DarkGray,
                disabledContentColor = Color.Gray,
            )
        ) {
            Text(
                "Created" + if (currentSort == SortOption.DATE_ADDED) {
                    ""
//                    if (!sortAsc) {
//                        " (Newest)"
//                    } else {
//                        " (Oldest)"
//                    }
                } else {
                    ""
                },
                Modifier
                    .fillMaxHeight()
                    .padding(4.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
        }
        TextButton(
            onClick = {
                onSortSelected(SortOption.LAST_UPDATED, currentSort == SortOption.LAST_UPDATED)
            }, colors = ButtonColors(
                containerColor = if (currentSort == SortOption.LAST_UPDATED) MaterialTheme.colorScheme.surfaceVariant
                else MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = Color.DarkGray,
                disabledContentColor = Color.Gray,
            )
        ) {
            Text(
                "Incremented" + if (currentSort == SortOption.LAST_UPDATED) {
                    ""
//                    if (!sortAsc) {
//                        " (Newest)"
//                    } else {
//                        " (Oldest)"
//                    }
                } else {
                    ""
                },
                Modifier
                    .fillMaxHeight()
                    .padding(4.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
        }
        TextButton(
            onClick = {
                onSortSelected(SortOption.USER_SORTED, false)
            }, colors = ButtonColors(
                containerColor = if (currentSort == SortOption.USER_SORTED) MaterialTheme.colorScheme.surfaceVariant
                else MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = Color.DarkGray,
                disabledContentColor = Color.Gray,
            )
        ) {
            Text(
                "Custom",
                Modifier
                    .fillMaxHeight()
                    .padding(4.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
        }


        // USER_SORTED cannot be reversed in order
        if (currentSort != SortOption.USER_SORTED) {
            VerticalDivider()
            IconButton(
                onClick = { onSortOrderClicked() },
            ) {
                val imageVector = if (currentSort == SortOption.ALPHABETICAL) {
                    CtIcons.SortAplha
                } else {
                    if (!sortAsc) CtIcons.ArrowUp else CtIcons.ArrowDown
                }
                Icon(
                    imageVector = imageVector, contentDescription = "Sort Asc or Desc"
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CounterRow(
    modifier: Modifier,
    onCounterClick: () -> Unit,
    onIncrementCounter: () -> Unit,
    onDecrementCounter: () -> Unit,
    counter: CounterWithIncrementInfo,
    inMoveState: Boolean,
    useButtonIncrements: Boolean,
    locationPermissionsState: MultiplePermissionsState,
    contextMenuOptions: List<ListItemContextMenuOption>,
    // TODO  create sealed class for these variables...?
    onContextMenuOptionSelected: (ListItemContextMenuOption) -> Unit
) {
//    if (useButtonIncrements) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        if (useButtonIncrements) {
            CounterRowCard(
                modifier = Modifier.weight(1f),
                contextMenuOptions = contextMenuOptions,
                onCounterClick = onCounterClick,
                counter = counter,
                onContextMenuOptionSelected = onContextMenuOptionSelected,
                inMoveState = inMoveState,
            )
        } else {
            SwipeToDismissCounterRow(
                counter = counter,
                locationPermissionsState = locationPermissionsState,
                onDecrementCounter = onDecrementCounter,
                onIncrementCounter = onIncrementCounter,
                modifier = Modifier.weight(1f),
                onCounterClick = onCounterClick,
                contextMenuOptions = contextMenuOptions,
                onContextMenuOptionSelected = onContextMenuOptionSelected,
                inMoveState = inMoveState,
            )
        }
        if (inMoveState) {
            Icon(
                imageVector = CtIcons.DragHandle,
                contentDescription = "Drag Icon",//"Reorder Icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        } else if (useButtonIncrements) {
            IncrementButtons(
                modifier = modifier.fillMaxHeight(),
                onIncrementCounter = onIncrementCounter,
                onDecrementCounter = onDecrementCounter,
                locationPermissionsState = locationPermissionsState,
            )

        }
//            CounterRowCard(
//                modifier = Modifier.weight(1f),
//                contextMenuOptions = contextMenuOptions,
//                onCounterClick = onCounterClick,
//                counter = counter,
//                onContextMenuOptionSelected = onContextMenuOptionSelected,
//                inMoveState = inMoveState,
//            )
//            if (!inMoveState) {
//                IncrementButtons(
//                    modifier = modifier.fillMaxHeight(),
//                    onIncrementCounter = onIncrementCounter,
//                    onDecrementCounter = onDecrementCounter,
//                    locationPermissionsState = locationPermissionsState,
//                )
//            } else {
//                Icon(
//                    imageVector = CtIcons.DragHandle,
//                    contentDescription = "Drag Icon",//"Reorder Icon",
//                    tint = MaterialTheme.colorScheme.onBackground
//                )
//            }
    }

//    } else {
//        SwipeToDismissCounterRow(
//            counter = counter,
//            locationPermissionsState = locationPermissionsState,
//            onDecrementCounter = onDecrementCounter,
//            onIncrementCounter = onIncrementCounter,
//            modifier = modifier,
//            onCounterClick = onCounterClick,
//            contextMenuOptions = contextMenuOptions,
//            onContextMenuOptionSelected = onContextMenuOptionSelected,
//            inMoveState = inMoveState,
//        )
//    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun IncrementButtons(
    modifier: Modifier = Modifier,
    onIncrementCounter: () -> Unit,
    onDecrementCounter: () -> Unit,
    locationPermissionsState: MultiplePermissionsState
) {
    Row(
        modifier = modifier,
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // TODO if trying to click without permissions do stuff
        IconButton(
            modifier = modifier.fillMaxHeight(),
            onClick = onDecrementCounter,
//            colors = IconButtonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer,)
        ) {
            Icon(
                imageVector = CtIcons.Remove,
                contentDescription = "Decrement Icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(
            modifier = modifier.fillMaxHeight(), onClick = onIncrementCounter
        ) {
            Icon(
                imageVector = CtIcons.Add,
                contentDescription = "Increment Icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }

}

@Composable
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
private fun SwipeToDismissCounterRow(
    counter: CounterWithIncrementInfo,
    locationPermissionsState: MultiplePermissionsState,
    onDecrementCounter: () -> Unit,
    onIncrementCounter: () -> Unit,
    modifier: Modifier,
    onCounterClick: () -> Unit,
    contextMenuOptions: List<ListItemContextMenuOption>,
    onContextMenuOptionSelected: (ListItemContextMenuOption) -> Unit,
    inMoveState: Boolean,
) {
    val dismissState =
        rememberDismissState(initialValue = DismissValue.Default, confirmValueChange = {
            if (counter.counter.trackLocation && !locationPermissionsState.permissions.first().status.isGranted && !locationPermissionsState.permissions.last().status.isGranted) { //&& !viewState.locationPermissionGranted){
                // location permission was deactivated when away from the app
                // as permission needs to be granted when adding a counter that tracks location
                locationPermissionsState.launchMultiplePermissionRequest()
            }
            when (it) {
                DismissValue.DismissedToStart -> {
                    onDecrementCounter()
                }

                DismissValue.DismissedToEnd -> {
                    onIncrementCounter()
                }

                DismissValue.Default -> {

                }
            }
            // return false because we don't actually want to remove any of these swipes
            false
        })

    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
        //dismissThresholds = { FractionalThreshold(.2f) },
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss

            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    DismissValue.Default -> MaterialTheme.colorScheme.secondaryContainer
                    DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.tertiaryContainer
                    DismissValue.DismissedToStart -> MaterialTheme.colorScheme.errorContainer
                }, label = "Swipe to Increment Color"
            )
            val iconColor = when (direction) {
                DismissDirection.StartToEnd -> MaterialTheme.colorScheme.onTertiaryContainer
                DismissDirection.EndToStart -> MaterialTheme.colorScheme.onErrorContainer
            }

            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Add
                DismissDirection.EndToStart -> Icons.Default.Remove
            }

            val scale by animateFloatAsState(
                targetValue = if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f,
                label = "Increment Icon Scale"
            )

            val alignment = when (direction) {
                DismissDirection.EndToStart -> Alignment.CenterEnd
                DismissDirection.StartToEnd -> Alignment.CenterStart
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(start = 12.dp, end = 12.dp), contentAlignment = alignment
            ) {
                Icon(icon, contentDescription = "Icon", modifier = Modifier.scale(scale), iconColor)
            }
        },
        dismissContent = {
            CounterRowCard(
                modifier = modifier,
                onCounterClick = onCounterClick,
                counter = counter,
                dismissState = dismissState,
                contextMenuOptions = contextMenuOptions,
                onContextMenuOptionSelected = onContextMenuOptionSelected,
                inMoveState = inMoveState,
            )
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CounterRowCard(
    modifier: Modifier = Modifier,
    inMoveState: Boolean,
    onCounterClick: () -> Unit,
    counter: CounterWithIncrementInfo,
    contextMenuOptions: List<ListItemContextMenuOption>,
    onContextMenuOptionSelected: (ListItemContextMenuOption) -> Unit,
    dismissState: DismissState? = null,
) {
    var showContextMenuInRow by remember { mutableStateOf(false) }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current
    val interactionSource = remember { MutableInteractionSource() }

    Card(colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ),
        modifier = modifier
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
            .conditional(!inMoveState) {
                pointerInput(Unit) {
                    detectTapGestures(onLongPress = { offset ->
                        showContextMenuInRow = true
                        pressOffset = DpOffset(offset.x.toDp(), offset.y.toDp())
                    },
                        // show ripple effect
                        onPress = {
                            val press = PressInteraction.Press(it)
                            interactionSource.emit(press)
                            tryAwaitRelease()
                            interactionSource.emit(PressInteraction.Release(press))
                        }, onTap = { onCounterClick.invoke() })
                }
            }
            .indication(interactionSource, LocalIndication.current)
            .height(IntrinsicSize.Min),
        elevation = CardDefaults.cardElevation(
            defaultElevation = animateDpAsState(
                targetValue = if (dismissState?.dismissDirection != null) 4.dp else 1.dp,
                label = "Card Swipe Animation"
            ).value
        )) {
        CounterRow(counter)

        LongPressContextMenu(
            showMenu = showContextMenuInRow,
            availableContextMenuOptions = contextMenuOptions,
            dpOffset = pressOffset.copy(
                y = pressOffset.y - itemHeight // menu is anchored to bottom but press offset is measured from top
                //TODO if menu displays above tap, this offset is wrong
            ),
            onContextMenuOptionSelected = onContextMenuOptionSelected,
            onDismiss = {
                showContextMenuInRow = false
            },
        )
    }
}

@Composable
private fun LongPressContextMenu(
    availableContextMenuOptions: List<ListItemContextMenuOption>,
    showMenu: Boolean,
    dpOffset: DpOffset,
    onContextMenuOptionSelected: (ListItemContextMenuOption) -> Unit,
    onDismiss: () -> Unit,
) {
    ListContextMenu(
        contextMenuOptions = availableContextMenuOptions,
        dpOffset = dpOffset,
        onDismiss = onDismiss,
        onOptionSelected = onContextMenuOptionSelected,
        showMenu = showMenu,
    )
}


@Composable
private fun OpenAddTimeDialog(
    counterName: String?,
    trackingLocation: Boolean,
    pickerInitialLatLng: LatLng,
//    pickerCurrentLocation: LatLng,
    locationQueryChanged: (String) -> Unit,
    pickerLocationAddress: String,
    onLocationChanged: (LatLng) -> Unit,
    onConfirm: (AddTimeInformation) -> Unit,
    onDismiss: () -> Unit
) {
    var openLocationDialog by remember { mutableStateOf(false) }
//    var locationQuery by remember { mutableStateOf("") }
    var isMapEditable by remember { mutableStateOf(true) }

    AddTimeDialog(
        onDismissRequest = onDismiss,
        onConfirmNewIncrement = onConfirm,//{ newTimeInformation -> onConfirm(newTimeInformation) },
        trackingLocation = trackingLocation,
        onLocationButtonClick = {
            openLocationDialog = true
        },
        confirmText = stringResource(R.string.dialog_add_time_confirm_button_text),
        dismissText = stringResource(R.string.dialog_add_time_dismiss_button_text),
    )

    if (openLocationDialog) {
        AddLocationDialog(
            onDismissRequest = { openLocationDialog = false },
            pickerInitialLatLng = pickerInitialLatLng,
//            pickerCurrentLocation = pickerCurrentLocation,
            onLocationQueryChanged = {
                if (!isMapEditable) locationQueryChanged(it)
            },
            onCameraMoved = onLocationChanged,
            mapEditOptionButton = { isMapEditable = !isMapEditable },
            isMapEditable = isMapEditable,
            locationQuery = pickerLocationAddress
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CounterRow(counter: CounterWithIncrementInfo) {
    Column(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(top = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(top = 10.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.Start)
                    .basicMarquee(),
                text = counter.counter.name,
                softWrap = false,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
                    .wrapContentWidth(Alignment.End),
                text = counter.counter.count.toString()
            )
        }


        if (counter.counter.goal > 0) {
            LinearProgressIndicator(
                progress = (counter.counter.count.toFloat() / counter.counter.goal),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
        } else {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
        }


        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(bottom = 10.dp)
        ) {

            if (counter.incrementsTodaySum != 0L) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                        .wrapContentWidth(Alignment.Start),
                    maxLines = 1,
                    text = "Today: ${counter.incrementsTodaySum}"
                )
            }
            //if (counter.mostRecentIncrementInstant != null)//counter.countOfIncrementsToday > 0) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
                    .wrapContentWidth(Alignment.End),
                maxLines = 1,
                text = if (counter.mostRecentIncrementDate != null) {
                    LocalCountThisDateFormatter.current.formatShortRelativeTime(counter.mostRecentIncrementDate!!)

                } else LocalCountThisDateFormatter.current.formatShortRelativeTime(counter.counter.creationDate)

            )
        }
    }

}

@Composable
fun AddLocationDialog(
    pickerInitialLatLng: LatLng,
//    pickerCurrentLocation: LatLng,
    onLocationQueryChanged: (query: String) -> Unit,
    onCameraMoved: (LatLng) -> Unit,
    onDismissRequest: () -> Unit,
    locationQuery: String,
    mapEditOptionButton: () -> Unit,
    isMapEditable: Boolean,

    ) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(0.9f), verticalArrangement = Arrangement.Center
        ) {
//            Title(
//                isMapEditable = isMapEditable,
//                onButtonClicked = mapEditOptionButton,
//                locationQuery = locationQuery,
//                onLocationQueryChanged = onLocationQueryChanged
//            )
            Body(pickerInitialLatLng, /*pickerCurrentLocation,*/ isMapEditable, onCameraMoved)
            LocationConfirmationButton(
                // TODO actual save location as opposed to always saving it when camera moves
                onConfirmClicked = onDismissRequest
            )
        }
    }
}

@Composable
private fun LocationConfirmationButton(
    onConfirmClicked: () -> Unit,
) {
    Button(
        onClick = onConfirmClicked, modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.dialog_add_location_confirm_button_text))
    }
}


@Composable
private fun Body(
    pickerInitialLatLng: LatLng,//Location,
//    pickerCurrentLocation: LatLng,
    isEnabled: Boolean,
    cameraPositionUpdated: (LatLng) -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(pickerInitialLatLng, 6f)
    }
//    cameraPositionState.move(CameraUpdateFactory.newLatLng(pickerCurrentLocation))

    LaunchedEffect(cameraPositionState.isMoving) {
        if (cameraPositionState.isMoving) {

            Log.d(
                "Camera movement",
                "Map camera started moving due to ${cameraPositionState.cameraMoveStartedReason.name}"
            )
        } else {
            // this is where we want to update location query text
            Log.d(
                "Camera stopped moving",
                "Map camera stopped moving due to ${cameraPositionState.cameraMoveStartedReason.name}"
            )
            cameraPositionUpdated(cameraPositionState.position.target)
        }
    }
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true, maxZoomPreference = 20f, minZoomPreference = 2f
            )
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                mapToolbarEnabled = false,
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true,
                rotationGesturesEnabled = true,
                tiltGesturesEnabled = true,
            )
        )
    }

    mapUiSettings = mapUiSettings.copy(
        scrollGesturesEnabled = isEnabled,
        scrollGesturesEnabledDuringRotateOrZoom = isEnabled,
    )


    Box(
        modifier = Modifier.height(500.dp)
//            .clip(RoundedCornerShape(10.dp))
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            properties = mapProperties,
        )

        MapPinOverlay()
    }
}

@Composable
fun MapPinOverlay() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                modifier = Modifier.size(50.dp),
                bitmap = ImageBitmap.imageResource(id = R.drawable.pin).asAndroidBitmap()
                    .asImageBitmap(),
                contentDescription = "Pin Image"
            )
        }
        Box(modifier = Modifier.weight(1f))
    }
}


@Composable
private fun CounterListFab(
    onClick: () -> Unit,
    extended: () -> Boolean,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    textDescription: String,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .padding(16.dp)
            .navigationBarsPadding()
            .height(48.dp)
            .widthIn(min = 48.dp),
        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        AnimatingFabContent(icon = {
            Icon(
                imageVector = icon, contentDescription = textDescription
            )
        }, text = {
            Text(
                text = textDescription,
            )
        }, extended = extended()
        )
    }
}


private val JumpToBottomThreshold = 56.dp

private fun ScrollState.atBottom(): Boolean = value == 0
