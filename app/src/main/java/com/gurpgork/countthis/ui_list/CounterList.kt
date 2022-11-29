package com.gurpgork.countthis.ui_list

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
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
import com.gurpgork.countthis.R
import com.gurpgork.countthis.components.AnimatingFabContent
import com.gurpgork.countthis.compose.*
import com.gurpgork.countthis.compose.dialog.AddTimeDialog
import com.gurpgork.countthis.compose.dialog.AddTimeInformation
import com.gurpgork.countthis.data.ListItemContextMenuOption
import com.gurpgork.countthis.data.resultentities.CounterWithIncrementInfo
import com.gurpgork.countthis.location.Location
import com.gurpgork.countthis.util.SortOption
import java.time.Instant

@Composable
fun CounterList(
    createNewCounter: () -> Unit,
    editCounter: (counterId: Long, wasTrackingLocation: Boolean) -> Unit,
    openCounter: (counterId: Long) -> Unit,
    openUser: () -> Unit,
) {
    CounterList(
        viewModel = hiltViewModel(),
        createNewCounter = createNewCounter,
        editCounter = editCounter,
        openCounter = openCounter,
        openUser = openUser,
    )
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun CounterList(
    viewModel: CounterListViewModel,
    createNewCounter: () -> Unit,
    editCounter: (counterId: Long, wasTrackingLocation: Boolean) -> Unit,
    openCounter: (counterId: Long) -> Unit,
    openUser: () -> Unit,
) {
    //TODO what's better, collecting with or without lifecycle
    val viewState by viewModel.state.collectAsStateWithLifecycle()
//    val viewState by viewModel.state.collectAsState()

    val pagingItems = viewModel.pagedList.collectAsLazyPagingItems()
    val pickerLocationState by viewModel.pickerLocation.collectAsState()

    CounterList(
        viewState = viewState,
        list = pagingItems,
        openUser = openUser,
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
        pickerLocation = LatLng(
            pickerLocationState.latitude,
            pickerLocationState.longitude
        )//LatLng(viewModel.pickerLocation.value.latitude, viewModel.pickerLocation.value.longitude)
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class,
)
@Composable
internal fun CounterList(
    viewState: CounterListViewState,
    list: LazyPagingItems<CounterWithIncrementInfo>,
    createNewCounter: () -> Unit,
    editCounter: (counterId: Long, wasTrackingLocation: Boolean) -> Unit,
    openCounter: (counterId: Long) -> Unit,
    onContextMenuOptionSelected: (counterId: Long, ListItemContextMenuOption, addTimeInfo: AddTimeInformation?) -> Unit,
    onIncrementCounter: (Long, Location?) -> Unit,
    onDecrementCounter: (Long, Location?) -> Unit,
    onSortSelected: (SortOption) -> Unit,
    openUser: () -> Unit,
    onMessageShown: (id: Long) -> Unit,
    pickerLocation: LatLng
) {
    val scaffoldState = rememberScaffoldState()
//    val scaffoldState = rememberBottomSheetScaffoldState()

    var searchQuery by remember { mutableStateOf(TextFieldValue(viewState.locationPickerAddressQuery)) }

    viewState.message?.let { message ->
        LaunchedEffect(message) {
            scaffoldState.snackbarHostState.showSnackbar(message.message)
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
//        scaffoldState = scaffoldState,
        //sheetShape = BottomSheetShape,
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        topBar = {
            CounterListAppBar(modifier = Modifier.fillMaxWidth())
        },
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
        snackbarHost = { snackbarHostState ->
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
                        SwipeToDismissCounterRow(
                            mostRecentLocation = viewState.mostRecentLocation,
                            availableContextMenuOptions = viewState.availableContextMenuOptions,
                            modifier = Modifier.animateItemPlacement(),
                            onCounterClick = openCounter,
                            editCounter = editCounter,
                            onIncrementCounter = onIncrementCounter,
                            onDecrementCounter = onDecrementCounter,
                            onContextMenuOptionSelected = onContextMenuOptionSelected,
                            counter = counter,
                            locationPermissionsState = locationPermissionsState,
                            pickerLocation = pickerLocation,
//                            onShowContextMenu = { }
                        )
                    }
                }
            }
        }
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun SwipeToDismissCounterRow(
    mostRecentLocation: Location?,
    availableContextMenuOptions: List<ListItemContextMenuOption>,
    modifier: Modifier,
    onCounterClick: (Long) -> Unit,
    editCounter: (counterId: Long, wasTrackingLocation: Boolean) -> Unit,
    onIncrementCounter: (Long, Location?) -> Unit,
    onDecrementCounter: (Long, Location?) -> Unit,
    onContextMenuOptionSelected: (counterId: Long, ListItemContextMenuOption, addTimeInfo: AddTimeInformation?) -> Unit,
    counter: CounterWithIncrementInfo,
    locationPermissionsState: MultiplePermissionsState,
    pickerLocation: LatLng,
//    onShowContextMenu: (Offset) -> Unit // TODO should this be propagated up?
) {
    //val dismissState = rememberDismissState(initialValue = DismissValue.Default)
    var showContextMenu by remember { mutableStateOf(false) }
//    var showDialog by remember { mutableStateOf(false) }
    var menuOptionSelected by remember { mutableStateOf(ListItemContextMenuOption.NONE) }

    var contextMenuOffset by remember { mutableStateOf(Offset(0F, 0F)) }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (counter.counter.track_location == true &&
                !locationPermissionsState.permissions.first().status.isGranted &&
                !locationPermissionsState.permissions.last().status.isGranted
            ) { //&& !viewState.locationPermissionGranted){
                // location permission was deactivated when away from the app
                // as permission needs to be granted when adding a counter that tracks location
                locationPermissionsState.launchMultiplePermissionRequest()
            }
            when (it) {
                DismissValue.DismissedToStart -> {
                    onDecrementCounter(counter.counter.id, mostRecentLocation)
                }
                DismissValue.DismissedToEnd -> {
                    onIncrementCounter(counter.counter.id, mostRecentLocation)
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
        }
    ) {
        Card(
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            //onClick = { onCounterClick(counter.id) },
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
//                            onShowContextMenu(it)
                            contextMenuOffset = it
                            showContextMenu = true
                        },
                        onTap = { onCounterClick(counter.counter.id) }
                    )
                }
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            elevation = animateDpAsState(targetValue = if (dismissState.dismissDirection != null) 4.dp else 1.dp).value
        ) {
            CounterRow(counter)

            if (showContextMenu) {
                LongPressContextMenu(
                    availableContextMenuOptions = availableContextMenuOptions,
                    onContextMenuOptionSelected = { option -> menuOptionSelected = option },
                    offset = contextMenuOffset,
                    onDismiss = { showContextMenu = false }
                )
            }

            when (menuOptionSelected) {
                ListItemContextMenuOption.NONE -> Unit // don't do nothing
                ListItemContextMenuOption.EDIT ->
                    editCounter(counter.counter.id, counter.counter.track_location ?: false)
                ListItemContextMenuOption.MOVE -> TODO()
                ListItemContextMenuOption.ADD_TIME ->
                    OpenAddTimeDialog(
                        counterName = counter.counter.name,
                        trackingLocation = counter.counter.track_location ?: false,
                        initialLocation = pickerLocation,
                        onConfirm = { addTimeInfo ->
                            onContextMenuOptionSelected(
                                counter.counter.id,
                                menuOptionSelected,
                                addTimeInfo
                            )
                            menuOptionSelected = ListItemContextMenuOption.NONE
                        },
                        onDismiss = { menuOptionSelected = ListItemContextMenuOption.NONE }
                    )
                ListItemContextMenuOption.RESET ->
                    OpenResetAlertDialog(
                        counterName = counter.counter.name,
                        onConfirm = {
                            onContextMenuOptionSelected(
                                counter.counter.id,
                                menuOptionSelected,
                                null
                            )
                            menuOptionSelected = ListItemContextMenuOption.NONE
                        },
                        onDismiss = { menuOptionSelected = ListItemContextMenuOption.NONE }
                    )
                ListItemContextMenuOption.DELETE ->
                    OpenDeleteAlertDialog(
                        counterName = counter.counter.name,
                        onConfirm = {
                            onContextMenuOptionSelected(
                                counter.counter.id,
                                menuOptionSelected,
                                null
                            )
                            menuOptionSelected = ListItemContextMenuOption.NONE
                        },
                        onDismiss = { menuOptionSelected = ListItemContextMenuOption.NONE }
                    )
            }
        }
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
        contextMenuOptions = availableContextMenuOptions,
//        showMenu = true,//showMenu,
        offset = offset,
        onDismiss = { onDismiss() },
        onOptionSelected = { onContextMenuOptionSelected(it) },
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
    counterName: String,
    trackingLocation: Boolean,
    initialLocation: LatLng,
    onConfirm: (AddTimeInformation) -> Unit,
    onDismiss: () -> Unit
) {
    var openLocationDialog by remember { mutableStateOf(false) }
    val addressQuery by remember { mutableStateOf("") }

    AddTimeDialog(
        onDismissRequest = { onDismiss() },
        onConfirmNewIncrement = { newTimeInformation -> onConfirm(newTimeInformation) },
        trackingLocation = trackingLocation,
        onLocationButtonClick = {
            openLocationDialog = true
        }
    )

    if (openLocationDialog) {
        AddLocationDialog(
            initialLocation = initialLocation,
            onSearchQueryChanged = { },
            onDismissRequest = { openLocationDialog = false },
            addressQuery = addressQuery
        )
    }
}

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
                    .wrapContentWidth(Alignment.Start),
                text = counter.counter.name,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
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
                        .formatShortRelativeTime(counter.counter.creation_date_time!!)

            )
        }
    }

}

@Composable
fun AddLocationDialog(
//    modifier: Modifier,
    initialLocation: LatLng,
    onSearchQueryChanged: (query: String) -> Unit,
    onDismissRequest: () -> Unit,
    addressQuery: String
) {
    //var searchQuery by remember { mutableStateOf(TextFieldValue(addressQuery)) }
//    var text by remember { viewModel.addressText }
    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            Column(
                modifier = Modifier.fillMaxSize(0.9f),
                verticalArrangement = Arrangement.Center
            ) {
                Title(addressQuery, onSearchQueryChanged)
                //TODO figure out isEnabled
                Body(initialLocation, true)
            }
        },
    )
}

@Composable
private fun Title(
    addressQuery: String,
    onSearchQueryChanged: (query: String) -> Unit,
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue(addressQuery)) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Layout.bodyMargin),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { value ->
                searchQuery = value
                onSearchQueryChanged(value.text)
            },
//            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
//                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Save")
            }

        }
    }


}


@Composable
private fun Body(
    initialLocation: LatLng,
    isEnabled: Boolean,
//    mapView: MapView,
) {
//    val userLocation = viewModel.getUserLocation()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 10f)
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (cameraPositionState.isMoving) {
            Log.d(
                "Camera movement",
                "Map camera started moving due to ${cameraPositionState.cameraMoveStartedReason.name}"
            )
        }
    }
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true,
                maxZoomPreference = 10f,
                minZoomPreference = 5f
            )
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                mapToolbarEnabled = false,
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true
            )
        )
    }

    Box(modifier = Modifier.height(500.dp)) {
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


@Composable
private fun CounterListAppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        backgroundColor =
//        MaterialTheme.colorScheme.primaryContainer
        MaterialTheme.colorScheme.surface.copy(alpha = AppBarAlphas.translucentBarAlpha()),
        contentColor = MaterialTheme.colorScheme.onSurface,
        contentPadding = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
            .asPaddingValues(),
        modifier = modifier,
        title = { Text(text = stringResource(R.string.counter_list_title)) },
        actions = {
            IconButton(
                onClick = { /* TODO: navigate to settings */ }
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
