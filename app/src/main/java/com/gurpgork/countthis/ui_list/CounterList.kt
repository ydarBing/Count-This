package com.gurpgork.countthis.ui_list

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.gurpgork.countthis.R
import com.gurpgork.countthis.components.AnimatingFabContent
import com.gurpgork.countthis.compose.*
import com.gurpgork.countthis.data.ListItemContextMenuOption
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.data.resultentities.CounterWithIncrementInfo
import com.gurpgork.countthis.location.Location
import com.gurpgork.countthis.util.SortOption
import java.time.Instant


@Composable
fun CounterList(
    createNewCounter: () -> Unit,
    openCounter: (counterId: Long) -> Unit,
    openUser: () -> Unit,
) {
    CounterList(
        viewModel = hiltViewModel(),
        createNewCounter = createNewCounter,
        openCounter = openCounter,
        openUser = openUser,
    )
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun CounterList(
    viewModel: CounterListViewModel,
    createNewCounter: () -> Unit,
    openCounter: (counterId: Long) -> Unit,
    openUser: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val pagingItems = rememberFlowWithLifecycle(viewModel.pagedList)
        .collectAsLazyPagingItems()


    CounterList(
        viewState = viewState,
        list = pagingItems,
        openUser = openUser,
        openCounter = openCounter,
        onMessageShown = { viewModel.clearMessage(it) },
        onContextMenuOptionSelected = { id, option ->
            viewModel.handleContextMenuOptionSelected(
                id,
                option
            )
        },
        createNewCounter = createNewCounter,
        onIncrementCounter = { id, location -> viewModel.incrementCounter(id, location) },
        onDecrementCounter = { id, location -> viewModel.decrementCounter(id, location) },
        onSortSelected = { viewModel.setSort(it) }
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)
@Composable
internal fun CounterList(
    viewState: CounterListViewState,
    list: LazyPagingItems<CounterWithIncrementInfo>,
    createNewCounter: () -> Unit,
    openCounter: (counterId: Long) -> Unit,
    onContextMenuOptionSelected: (counterId: Long, ListItemContextMenuOption) -> Unit,
    onIncrementCounter: (Long, Location?) -> Unit,
    onDecrementCounter: (Long, Location?) -> Unit,
    onSortSelected: (SortOption) -> Unit,
    openUser: () -> Unit,
    onMessageShown: (id: Long) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

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
        scaffoldState = scaffoldState,
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
        },
        content = { contentPadding ->
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
                                viewState = viewState,
                                onCounterClick = openCounter,
                                onIncrementCounter = onIncrementCounter,
                                onDecrementCounter = onDecrementCounter,
                                onContextMenuOptionSelected = onContextMenuOptionSelected,
                                counter = counter,
                                locationPermissionsState = locationPermissionsState
                            )
                        }
                    }
                }
            }
        }
    )

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
    viewState: CounterListViewState,
    onCounterClick: (Long) -> Unit,
    onIncrementCounter: (Long, Location?) -> Unit,
    onDecrementCounter: (Long, Location?) -> Unit,
    onContextMenuOptionSelected: (counterId: Long, ListItemContextMenuOption) -> Unit,
    counter: CounterWithIncrementInfo,
    locationPermissionsState: MultiplePermissionsState
) {
    //val dismissState = rememberDismissState(initialValue = DismissValue.Default)
    var showContextMenu by remember { mutableStateOf(false) }

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
                    // TODO this is where we decrement counter
                    onDecrementCounter(counter.counter.id, viewState.mostRecentLocation)
                }
                DismissValue.DismissedToEnd -> {
                    // TODO this is where we increment counter
                    onIncrementCounter(counter.counter.id, viewState.mostRecentLocation)
                }
                DismissValue.Default -> {

                }
            }
            // return false because we don't actually want to remove any of these swipes
            false
        }
    )
    SwipeToDismiss(
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
                            // show context menu
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

            LongPressContextMenu(
                viewState = viewState,
                counter = counter.counter,
                onContextMenuOptionSelected = onContextMenuOptionSelected,
                showMenu = showContextMenu,
                onDismiss = {
                    showContextMenu = false
                })
        }
    }
}


@Composable
private fun LongPressContextMenu(
    viewState: CounterListViewState,
    showMenu: Boolean,
    counter: CounterEntity,
    onContextMenuOptionSelected: (counterId: Long, ListItemContextMenuOption) -> Unit,
    onDismiss: () -> Unit
) {
    var openDeleteDialog by remember { mutableStateOf(false) }
    var openResetDialog by remember { mutableStateOf(false) }

    ListContextMenu(
        contextMenuOptions = viewState.availableContextMenuOptions,
        showMenu = showMenu,
        onDismiss = { onDismiss() },
        onOptionSelected = {
            when (it) {
                ListItemContextMenuOption.ADD_TIME -> {
                    onContextMenuOptionSelected(counter.id, it)
                    onDismiss()
                }
                ListItemContextMenuOption.MOVE -> {
                    // Modifier.animateItemPlacement() ???
                    onContextMenuOptionSelected(counter.id, it)
                    onDismiss()
                }
                ListItemContextMenuOption.EDIT -> {
                    // TODO navigate to edit route
                    onContextMenuOptionSelected(counter.id, it)
                    onDismiss()
                }
                ListItemContextMenuOption.RESET -> {
                    //onDismiss()
                    openResetDialog = true
                }
                ListItemContextMenuOption.DELETE -> {
                    //onDismiss()
                    openDeleteDialog = true
                }
            }
        }
    )

    if (openResetDialog) {
        OpenResetDialog(
            counterName = counter.name,
            onConfirm = {
                onContextMenuOptionSelected(counter.id, ListItemContextMenuOption.RESET)
                openResetDialog = false
            },
            onDismiss = { openResetDialog = false }
        )
    } else if (openDeleteDialog) {
        OpenDeleteDialog(
            counterName = counter.name,
            onConfirm = {
                onContextMenuOptionSelected(counter.id, ListItemContextMenuOption.DELETE)
                openDeleteDialog = false
            },
            onDismiss = { openDeleteDialog = false }
        )
    }
}

@Composable
private fun OpenResetDialog(
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
private fun OpenDeleteDialog(
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

//@Composable
//private fun LongPressContextMenu() {
//
//    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
//    if (functionalityNotAvailablePopupShown) {
//        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
//    }
//
//    var expandedDropdownMenu by remember { mutableStateOf(false) }
//    DropdownMenu(
//        expanded = expandedDropdownMenu,
//        onDismissRequest = { expandedDropdownMenu = false }) {
//
//        enumValues<ListItemContextMenuOption>().forEachIndexed() { index, string ->
//            DropdownMenuItem(onClick = {
//                when (index) {
//                    ListItemContextMenuOption.MOVE.ordinal -> {
//                        functionalityNotAvailablePopupShown = true
//                    }
//                    ListItemContextMenuOption.ADD_TIME.ordinal -> {
//                        functionalityNotAvailablePopupShown = true
//                    }
//                    ListItemContextMenuOption.EDIT.ordinal -> {
//                        functionalityNotAvailablePopupShown = true
//                    }
//                    ListItemContextMenuOption.RESET.ordinal -> {
//                        functionalityNotAvailablePopupShown = true
//                    }
//                    ListItemContextMenuOption.DELETE.ordinal -> {
//                        functionalityNotAvailablePopupShown = true
//                    }
//                }
//            }) {
//
//            }
//
//        }
//    }
//}

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

            if (counter.incrementsTodaySum > 0) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                        .wrapContentWidth(Alignment.Start),
                    text = "Today: ${counter.incrementsTodaySum}"
                )
            }
            //if (counter.mostRecentIncrementInstant != null)//counter.countOfIncrementsToday > 0) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
                    .wrapContentWidth(Alignment.End),
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
        MaterialTheme.colorScheme.surface.copy(alpha = AppBarAlphas.translucentBarAlpha())
       ,
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
