package com.gurpgork.countthis.feature.allcounters

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
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
import com.gurpgork.countthis.core.designsystem.component.Layout
import com.gurpgork.countthis.core.designsystem.component.ListContextMenu
import com.gurpgork.countthis.core.designsystem.component.ListItemContextMenuOption
import com.gurpgork.countthis.core.designsystem.component.SwipeDismissSnackbarHost
import com.gurpgork.countthis.core.designsystem.component.dialog.AddTimeDialog
import com.gurpgork.countthis.core.designsystem.component.dialog.AddTimeInformation
import com.gurpgork.countthis.core.designsystem.component.dialog.CountThisAlertDialog
import com.gurpgork.countthis.core.model.data.CtLocation
import com.gurpgork.countthis.core.model.data.SortOption
import com.gurpgork.countthis.core.ui.LocalCountThisDateFormatter
import java.time.Instant

@Composable
internal fun AllCountersRoute(
    createNewCounter: () -> Unit,
    editCounter: (counterId: Long, wasTrackingLocation: Boolean) -> Unit,
    openCounter: (counterId: Long) -> Unit,
    openUser: () -> Unit,
){
    AllCounters(
        viewModel = hiltViewModel(),
        createNewCounter = createNewCounter,
        editCounter = editCounter,
        openCounter = openCounter,
        openUser = openUser,
//        openSettings = openSettings
    )
}

@Composable
internal fun AllCounters(
    viewModel: CounterListViewModel,
    createNewCounter: () -> Unit,
    editCounter: (counterId: Long, wasTrackingLocation: Boolean) -> Unit,
    openCounter: (counterId: Long) -> Unit,
    openUser: () -> Unit,
//    openSettings: () -> Unit
) {
    val context = LocalContext.current
    //TODO what's better, collecting with or without lifecycle
    val viewState by viewModel.state.collectAsStateWithLifecycle()
//    val viewState by viewModel.state.collectAsState()
    val pagingItems = viewModel.pagedList.collectAsLazyPagingItems()
//    val pickerLocationState by viewModel.pickerLocation.collectAsState()
    val initialLatLng =
        if (viewState.mostRecentLocation != null)
            LatLng(
                viewState.mostRecentLocation!!.latitude,
                viewState.mostRecentLocation!!.longitude
            )
        else
            LatLng(
                viewModel.getDefaultLocation().latitude,
                viewModel.getDefaultLocation().longitude
            )

    AllCountersScreen(
        viewState = viewState,
        list = pagingItems,
        openUser = openUser,
//        openSettings = openSettings,
        openCounter = openCounter,
        editCounter = editCounter,
        onMessageShown = { viewModel.clearMessage(it) },
        onContextMenuOptionSelected = { id, option, addTimeInfo ->
            viewModel.handleContextMenuOptionSelected(
                id,
                option,
                addTimeInfo
            )
        },
        createNewCounter = createNewCounter,
        onIncrementCounter = { id, location -> viewModel.incrementCounter(id, location) },
        onDecrementCounter = { id, location -> viewModel.decrementCounter(id, location) },
        onSortSelected = { viewModel.setSort(it) },
        pickerInitialLatLng = initialLatLng,
        pickerLocationAddress = viewState.locationPickerAddressQuery,
//        pickerLocation = LatLng(
//            pickerLocationState.latitude,
//            pickerLocationState.longitude
//        ),
        locationQueryChanged = { newAddr -> viewModel.onTextChanged(context, newAddr) },
        onLocationPickerMoved = { latLng -> viewModel.updatePickerLocation(context, latLng) },
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
    list: LazyPagingItems<CounterWithIncrementInfo>,
    createNewCounter: () -> Unit,
    editCounter: (counterId: Long, wasTrackingLocation: Boolean) -> Unit,
    openCounter: (counterId: Long) -> Unit,
    onContextMenuOptionSelected: (counterId: Long, ListItemContextMenuOption, addTimeInfo: AddTimeInformation?) -> Unit,
    onIncrementCounter: (Long, CtLocation?) -> Unit,
    onDecrementCounter: (Long, CtLocation?) -> Unit,
    onSortSelected: (SortOption) -> Unit,
    openUser: () -> Unit,
//    openSettings: () -> Unit,
    pickerInitialLatLng: LatLng,
    onMessageShown: (id: Long) -> Unit,
    pickerLocationAddress: String,
//    pickerLocation: LatLng,
    locationQueryChanged: (String) -> Unit,
    onLocationPickerMoved: (LatLng) -> Unit,
) {
    var menuOptionSelected by remember { mutableStateOf(ListItemContextMenuOption.NONE) }
    var counterContextMenuId by remember { mutableStateOf(0L) }
    var counterContextMenuName by remember { mutableStateOf("") }
    var counterContextMenuTrackingLocation by remember { mutableStateOf(false) }

    var selectedCounters by remember { mutableStateOf(listOf<Long>()) }

    val snackbarHostState = remember { SnackbarHostState() }

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

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val listState = rememberLazyListState()

    Scaffold(
        //sheetShape = BottomSheetShape,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        floatingActionButton = {
            val extended by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex > 0
                }
            }
            CounterListFab(
                extended = { extended },
                onClick = createNewCounter,
            )
        },
        snackbarHost = {
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
        }
    ) { contentPadding ->
        if (list.itemCount == 0) {
            ZeroCounters()
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .testTag(CounterListTestTag),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(0.dp),
                reverseLayout = false,
            ) {
                items(items = list, key = { it.counter.id }) { counter ->
                    if (counter != null) {
                        CounterRow(
                            modifier = Modifier.animateItemPlacement(),
//                            mostRecentCTLocation = viewState.mostRecentLocation,
                            useButtonIncrements = viewState.useButtonIncrements,
                            onCounterClick = { openCounter(counter.counter.id) },
                            onIncrementCounter = {
                                onIncrementCounter(
                                    counter.counter.id,
                                    viewState.mostRecentLocation
                                )
                            },
                            onDecrementCounter = {
                                onDecrementCounter(
                                    counter.counter.id,
                                    viewState.mostRecentLocation
                                )
                            },
                            counter = counter,
                            locationPermissionsState = locationPermissionsState,
                            contextMenuOptions = viewState.availableContextMenuOptions,
                            onContextMenuOptionSelected = { option ->
                                menuOptionSelected = option
                                counterContextMenuId = counter.counter.id
                                counterContextMenuName = counter.counter.name
                                counterContextMenuTrackingLocation =
                                    counter.counter.trackLocation
                            }
                        )
                    }
                }
            }
        }
    }

    // TODO Context menu probably has to move back inside the counter row to properly attach to parent
    // TODO or maybe we dont do that, we use the top bar like gmail... this is probably better choice
//    if (showContextMenu) {
//        LongPressContextMenu(
//            availableContextMenuOptions = viewState.availableContextMenuOptions,
//            offset = contextMenuOffset,
//            onContextMenuOptionSelected = { option -> menuOptionSelected = option },
//            onDismiss = { showContextMenu = false }
//        )
//    }

    when (menuOptionSelected) {
        ListItemContextMenuOption.NONE -> Unit // don't do anything
        ListItemContextMenuOption.EDIT -> {
            editCounter(counterContextMenuId, counterContextMenuTrackingLocation)
            menuOptionSelected = ListItemContextMenuOption.NONE
        }

        ListItemContextMenuOption.MOVE -> {
            TODO()
        }

        ListItemContextMenuOption.ADD_TIME ->
            OpenAddTimeDialog(
                counterName = counterContextMenuName,
                trackingLocation = counterContextMenuTrackingLocation,
                pickerInitialLatLng = pickerInitialLatLng,
//                pickerCurrentLocation = pickerLocation,
                pickerLocationAddress = pickerLocationAddress,
                locationQueryChanged = locationQueryChanged,
                onLocationChanged = onLocationPickerMoved,
                onConfirm = { addTimeInfo ->
                    onContextMenuOptionSelected(
                        counterContextMenuId,
                        menuOptionSelected,
                        addTimeInfo
                    )
                    menuOptionSelected = ListItemContextMenuOption.NONE
                },
                onDismiss = { menuOptionSelected = ListItemContextMenuOption.NONE }
            )

        ListItemContextMenuOption.RESET ->
            OpenResetAlertDialog(
                counterName = counterContextMenuName,
                onConfirm = {
                    onContextMenuOptionSelected(
                        counterContextMenuId,
                        menuOptionSelected,
                        null
                    )
                    menuOptionSelected = ListItemContextMenuOption.NONE
                },
                onDismiss = { menuOptionSelected = ListItemContextMenuOption.NONE }
            )

        ListItemContextMenuOption.DELETE ->
            OpenDeleteAlertDialog(
                counterName = counterContextMenuName,
                onConfirm = {
                    onContextMenuOptionSelected(
                        counterContextMenuId,
                        menuOptionSelected,
                        null
                    )
                    menuOptionSelected = ListItemContextMenuOption.NONE
                },
                onDismiss = { menuOptionSelected = ListItemContextMenuOption.NONE }
            )
    }
//    list.apply {
//        when{
//            loadState.refresh is LoadState.Loading -> {
//
//            }
//            loadState.append is LoadState.Loading -> {
//
//            }
//            loadState.refresh is LoadState.Error -> {
//
//            }
//            loadState.append is LoadState.Error -> {
//                val e = list.loadState.append as LoadState.Error
//                item{
//                    ErrorItem(
//                        message = e.error.localizedMessage!!,
//                        onClickRetry = { retry()}
//                    )
//                }
//            }
//        }
//    }
}

@Composable
fun ZeroCounters() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.list_empty),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ListError() {
    Text(stringResource(R.string.list_error))
}

const val CounterListTestTag = "CounterListTestTag"

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CounterRow(
//    mostRecentCTLocation: CTLocation?,
    modifier: Modifier,
    onCounterClick: () -> Unit, //(Long) -> Unit,
    onIncrementCounter: () -> Unit,
    onDecrementCounter: () -> Unit,
    counter: CounterWithIncrementInfo,
    useButtonIncrements: Boolean,
    locationPermissionsState: MultiplePermissionsState,
    contextMenuOptions: List<ListItemContextMenuOption>,
    // TODO  create sealed class for these variables...
    onContextMenuOptionSelected: (ListItemContextMenuOption) -> Unit
) {
    val showContextMenu by remember { mutableStateOf(false) }
///////////////////////////////////////////////////  TEST AREA  //////////////////////////////////
    val contextMenuOffset by remember { mutableStateOf(Offset(0F, 0F)) }
//////////////////////////////////////////////////////////////////////////////////////////////////

    if (useButtonIncrements) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            CounterRowCard(
                showContextMenu = showContextMenu,
                contextMenuOptions = contextMenuOptions,
                contextMenuOffset = contextMenuOffset,
                onCounterClick = onCounterClick,
                counter = counter,
                onContextMenuOptionSelected = onContextMenuOptionSelected
            )

            IncrementButtons(
                onIncrementCounter = onIncrementCounter,
                onDecrementCounter = onDecrementCounter,
                locationPermissionsState
            )
        }

    } else {
        SwipeToDismissCounterRow(
            counter = counter,
            locationPermissionsState = locationPermissionsState,
//            mostRecentCTLocation = mostRecentCTLocation,
            onDecrementCounter = onDecrementCounter,
            onIncrementCounter = onIncrementCounter,
            modifier = modifier,
            showContextMenu = showContextMenu,
            contextMenuOffset = contextMenuOffset,
            onCounterClick = onCounterClick,
            contextMenuOptions = contextMenuOptions,
            onContextMenuOptionSelected = onContextMenuOptionSelected
        )
    }


}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun IncrementButtons(
    onIncrementCounter: () -> Unit,
    onDecrementCounter: () -> Unit,
    locationPermissionsState: MultiplePermissionsState
) {
    // TODO if trying to click without permissions do stuff
    Button(onClick = onDecrementCounter) {
        Text(text = "-", fontWeight = FontWeight.Bold)
    }
    Button(onClick = onIncrementCounter) {
        Text(text = "+", fontWeight = FontWeight.Bold)
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
private fun SwipeToDismissCounterRow(
    counter: CounterWithIncrementInfo,
    locationPermissionsState: MultiplePermissionsState,
    onDecrementCounter: () -> Unit,
//    mostRecentCTLocation: CTLocation?,
    onIncrementCounter: () -> Unit,
    modifier: Modifier,
    showContextMenu: Boolean,
    contextMenuOffset: Offset,
    onCounterClick: () -> Unit, //(Long) -> Unit,
    contextMenuOptions: List<ListItemContextMenuOption>,
    onContextMenuOptionSelected: (ListItemContextMenuOption) -> Unit
) {
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (counter.counter.trackLocation == true &&
                !locationPermissionsState.permissions.first().status.isGranted &&
                !locationPermissionsState.permissions.last().status.isGranted
            ) { //&& !viewState.locationPermissionGranted){
                // location permission was deactivated when away from the app
                // as permission needs to be granted when adding a counter that tracks location
                locationPermissionsState.launchMultiplePermissionRequest()
            }
            when (it) {
                DismissValue.DismissedToStart -> {
                    onDecrementCounter()
//                    onDecrementCounter(counter.counter.id, mostRecentCTLocation)
                }

                DismissValue.DismissedToEnd -> {
                    onIncrementCounter()
//                    onIncrementCounter(counter.counter.id, mostRecentCTLocation)
                }

                DismissValue.Default -> {

                }
            }
            // return false because we don't actually want to remove any of these swipes
            false
        }
    )

    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
        //dismissThresholds = { FractionalThreshold(.2f) },
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss

            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToEnd -> Color.Green
                    DismissValue.DismissedToStart -> MaterialTheme.colorScheme.error
                }
            )

            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Add
                DismissDirection.EndToStart -> Icons.Default.Remove
            }
            val scale by animateFloatAsState(
                targetValue = if (dismissState.targetValue ==
                    DismissValue.Default
                ) 0.8f else 1.2f
            )

            val alignment = when (direction) {
                DismissDirection.EndToStart -> Alignment.CenterEnd
                DismissDirection.StartToEnd -> Alignment.CenterStart
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(start = 12.dp, end = 12.dp),
                contentAlignment = alignment
            )
            {
                Icon(icon, contentDescription = "Icon", modifier = Modifier.scale(scale))
            }
        },
        dismissContent = {
            CounterRowCard(
                showContextMenu = showContextMenu,
                contextMenuOffset = contextMenuOffset,
                onCounterClick = onCounterClick,
                counter = counter,
                dismissState = dismissState,
                contextMenuOptions = contextMenuOptions,
                onContextMenuOptionSelected = onContextMenuOptionSelected
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CounterRowCard(
    showContextMenu: Boolean,
    contextMenuOffset: Offset,
    onCounterClick: () -> Unit, //(Long) -> Unit,
    counter: CounterWithIncrementInfo,
    contextMenuOptions: List<ListItemContextMenuOption>,
    onContextMenuOptionSelected: (ListItemContextMenuOption) -> Unit,
    dismissState: DismissState? = null,
) {
    var showContextMenuInRow by remember { mutableStateOf(false) }
//    var showContextMenu1 = showContextMenu
    var contextMenuOffset1 = contextMenuOffset
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
//                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
//                    else
//                        MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset ->
//                        showContextMenu1 = true
                        showContextMenuInRow = true
                        contextMenuOffset1 = offset
                    },
                    onTap = { onCounterClick.invoke() }//{ onCounterClick(counter.counter.id) }
                )
            }
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        elevation = CardDefaults.cardElevation(
            defaultElevation = animateDpAsState(
                targetValue = if (dismissState?.dismissDirection != null) 4.dp else 1.dp
            ).value
        )
    ) {
        CounterRow(counter)

        if (showContextMenuInRow)//showContextMenu1)
            LongPressContextMenu(
                availableContextMenuOptions = contextMenuOptions,
                offset = contextMenuOffset1,
                onContextMenuOptionSelected = onContextMenuOptionSelected,
                onDismiss = {
//                    showContextMenu1 = false
                    showContextMenuInRow = false
                }
            )
    }
}

@Composable
private fun LongPressContextMenu(
    availableContextMenuOptions: List<ListItemContextMenuOption>,
//    showMenu: Boolean,
    offset: Offset,
//    counter: CounterEntity,
//    counterName: String,
    onContextMenuOptionSelected: (ListItemContextMenuOption) -> Unit,
    onDismiss: () -> Unit,
) {
    ListContextMenu(
//        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart),
        contextMenuOptions = availableContextMenuOptions,
//        showMenu = true,//showMenu,
        offset = offset,
        onDismiss = onDismiss,
        onOptionSelected = onContextMenuOptionSelected,
    )
}


@Composable
private fun OpenResetAlertDialog(
    counterName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CountThisAlertDialog(
        title = stringResource(R.string.dialog_title_reset_counter),
        message = stringResource(R.string.dialog_message_reset_counter, counterName),
        confirmText = stringResource(R.string.dialog_title_confirm_reset_counter),
        onConfirm = { onConfirm() },
        dismissText = stringResource(R.string.dialog_dismiss),
        onDismissRequest = { onDismiss() },
    )
}

@Composable
private fun OpenDeleteAlertDialog(
    counterName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
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
                if (!isMapEditable)
                    locationQueryChanged(it)
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
                text = counter.counter.count.toString()//counter.increments.count().toString()
            )
        }


        if (counter.counter.goal > 0) {
            LinearProgressIndicator(
                progress = (counter.counter.count.toFloat() / counter.counter.goal),
                color = Color.Blue,
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
                text = if (counter.mostRecentIncrementInstant != null)
                    LocalCountThisDateFormatter.current
                        .formatShortRelativeTime(
                            Instant.ofEpochMilli(counter.mostRecentIncrementInstant!!),
                            counter.mostRecentIncrementInstantOffset!!
                        )
                else
                    LocalCountThisDateFormatter.current
                        .formatShortRelativeTime(counter.counter.creationDateTime)

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
            modifier = Modifier.fillMaxSize(0.9f),
            verticalArrangement = Arrangement.Center
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
        onClick = onConfirmClicked,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.dialog_add_location_confirm_button_text))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Title(
    locationQuery: String,
    isMapEditable: Boolean,
    onLocationQueryChanged: (query: String) -> Unit,
    onButtonClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
//            .padding(horizontal = Layout.bodyMargin),
//        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            modifier = Modifier
                .weight(1f),
//                .padding(end = 80.dp),
            value = locationQuery,
            onValueChange = onLocationQueryChanged,
            enabled = !isMapEditable,
            maxLines = 1,
        )
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(10.dp),
//            horizontalAlignment = Alignment.End,
//        ) {
        Button(onClick = onButtonClicked) {
            Text(text = if (isMapEditable) "Edit" else "Save")
        }
//        }
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
                isMyLocationEnabled = true,
                maxZoomPreference = 20f,
                minZoomPreference = 2f
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
        modifier = Modifier
            .height(500.dp)
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
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
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
        AnimatingFabContent(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(
                        R.string.add
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        id = R.string.add
                    ),
                )
            },
            extended = extended()
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CounterListAppBar(
    modifier: Modifier = Modifier,
//    onClickSettingsIcon: () -> Unit
) {
    TopAppBar(
        colors = topAppBarColors(),
//        backgroundColor =
////        MaterialTheme.colorScheme.primaryContainer
//        MaterialTheme.colorScheme.surface.copy(alpha = AppBarAlphas.translucentBarAlpha()),
//        contentColor = MaterialTheme.colorScheme.onSurface,
//        contentPadding = WindowInsets.systemBars
//            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
//            .asPaddingValues(),
        modifier = modifier,
        title = { Text(text = stringResource(R.string.counter_list_title)) },
        actions = {
            IconButton(
                onClick = {},//onClickSettingsIcon
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.settings_title)
                )
            }
        },
//                navigationIcon = {
//                    IconButton(
//                        onClick = { /* "Open nav drawer" */ }
//                    ) {
//                        Icon(Icons.Filled.Menu, contentDescription = "Localized description")
//                    }
//                }
    )
}

private val JumpToBottomThreshold = 56.dp

private fun ScrollState.atBottom(): Boolean = value == 0
