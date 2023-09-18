package com.gurpgork.countthis.feature.counterdetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurpgork.countthis.core.designsystem.component.AnimatingFabContent
import com.gurpgork.countthis.core.designsystem.component.CtAppBarState
import com.gurpgork.countthis.core.designsystem.component.FunctionalityNotAvailablePopup
import com.gurpgork.countthis.core.designsystem.component.bodyWidth
import com.gurpgork.countthis.core.designsystem.component.pagerTabIndicatorOffset
import com.gurpgork.countthis.core.designsystem.icon.CtIcons
import com.gurpgork.countthis.core.designsystem.theme.CtTheme
import kotlinx.coroutines.launch

@Composable
internal fun CounterDetailsRoute(
    navigateUp: () -> Unit,
    openEditCounter: (counterId: Long) -> Unit,
    openCounterDetails: (counterId: Long) -> Unit,
    onComposing: (CtAppBarState) -> Unit,
) {
    CounterDetails(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
        openEditCounter = openEditCounter,
        openCounterDetails = openCounterDetails,
        onComposing = onComposing,
    )
}

@Composable
internal fun CounterDetails(
    viewModel: CounterDetailsViewModel,
    navigateUp: () -> Unit,
    openEditCounter: (counterId: Long) -> Unit,
    openCounterDetails: (counterId: Long) -> Unit,
    onComposing: (CtAppBarState) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    CounterDetails(
        viewState = viewState,
        navigateUp = navigateUp,
        openEditCounter = openEditCounter,
        onDeleteCounter = {id -> viewModel.deleteCounter(id)},
        openCounterDetails = openCounterDetails,
        onComposing = onComposing,
    )
}

@OptIn(
    ExperimentalFoundationApi::class,
)
@Composable
internal fun CounterDetails(
    viewState: CounterDetailsViewState,
    navigateUp: () -> Unit,
    openEditCounter: (counterId: Long) -> Unit,
    onDeleteCounter: (counterId: Long) -> Unit,
    openCounterDetails: (counterId: Long) -> Unit, // used to open another counter
    onComposing: (CtAppBarState) -> Unit,
) {
    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }

    //TODO only make history and details if there are elements in each respective list
    val pages = listOf("STATS", "DETAILS", "HISTORY")
    val pagerState = rememberPagerState(pageCount = { pages.size })

    LaunchedEffect(key1 = true) {
        val title = viewState.counterInfo?.counter?.name ?: "INVALID COUNTER"
        onComposing(
            CtAppBarState(title = title,
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_up)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewState.counterInfo?.let {
                            openEditCounter(it.counter.id)
                        }
                    }) {
                        Icon(
                            imageVector = CtIcons.Edit, contentDescription = "edit"
                        )
                    }
                    IconButton(onClick = { //TODO are you sure you want to delete or undo on snackbar
                        viewState.counterInfo?.let {
                            onDeleteCounter(it.counter.id)
                        }
                    }) {
                        Icon(
                            imageVector = CtIcons.Delete, contentDescription = "delete"
                        )
                    }
                })
        )
    }
    Column {
        Tabs(
            pagerState = pagerState,
            tabs = pages,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            contentColor = LocalContentColor.current
        )

        CounterDetailsPager(
            pages = pages,
            viewState = viewState,
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxHeight()
                .bodyWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CounterDetailsPager(
    pages: List<String>,
    viewState: CounterDetailsViewState,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
//    padding: PaddingValues
) {
    var userScrollEnabled by remember { mutableStateOf(true) }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
//        contentPadding = padding,
        verticalAlignment = Alignment.Top,
        userScrollEnabled = userScrollEnabled
    ) { pageIndex ->
        when (pageIndex) {
            0 -> viewState.counterInfo?.let {
                StatsTab(
                    it.counter,
                    viewState.counterInfo.increments,
                    viewState.counterInfo.hasLocations,
                    onMapMoved = { movingMap ->
                        // only update userScrollEnabled when a new val comes in
                        if (movingMap == userScrollEnabled)
                            userScrollEnabled = !movingMap
                    }
                )
            }

            1 -> viewState.counterInfo?.let { DetailsTab(it.increments) }
            2 -> viewState.counterInfo?.let { HistoryTab(it.history) }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Tabs(
    tabs: List<String>,
    pagerState: PagerState,
    modifier: Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
) {
    val scope = rememberCoroutineScope()

    TabRow(
        modifier = modifier,
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        },
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        tabs.forEachIndexed { index, title ->
//            val selected = pagerState.currentPage == index
            Tab(
                text = { Text(text = title) },
//                selectedContentColor(Color.White)
//                unselectedContentColor(Color(0xff1E76DA)),
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@Composable
private fun CounterError() {
    Text(stringResource(R.string.counter_error))
}

@Composable
private fun CounterFab(
    extended: Boolean,
    modifier: Modifier = Modifier,
    onFabClicked: () -> Unit = { }
) {
    FloatingActionButton(
        onClick = onFabClicked,
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
                    imageVector = Icons.Outlined.Edit, //Icons.Outlined.Chat
                    contentDescription = stringResource(
                        R.string.edit_counter
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        id = R.string.edit_counter
                    ),
                )
            },
            extended = extended

        )
    }
}

@Preview
@Composable
fun CounterFabPreview() {
    CtTheme {
        CounterFab(extended = true)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun TabsPreview() {
    val tabs = listOf(
        "STATS",
        "DETAILS",
        "HISTORY"
    )
//    val pagerState = rememberPagerState(
//        initialPage = 0,
//        initialPageOffsetFraction = 0f
//    ) {
//        // provide pageCount
//    }
//    val padding
//    Tabs(tabs = tabs, pagerState = pagerState, padding = padding)
}
