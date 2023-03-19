package com.gurpgork.countthis.ui_counter_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.gurpgork.countthis.FunctionalityNotAvailablePopup
import com.gurpgork.countthis.R
import com.gurpgork.countthis.components.AnimatingFabContent
import com.gurpgork.countthis.components.pagerTabIndicatorOffset
import com.gurpgork.countthis.compose.AppBarAlphas
import com.gurpgork.countthis.compose.TopAppBarWithBottomContent
import com.gurpgork.countthis.compose.bodyWidth
import com.gurpgork.countthis.theme.CountThisTheme
import kotlinx.coroutines.launch

@Composable
fun CounterDetails(
    navigateUp: () -> Unit,
    openEditCounter: (counterId: Long, wasTrackingLocation: Boolean) -> Unit,
    openCounterDetails: (counterId: Long) -> Unit,
) {
    CounterDetails(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
        openEditCounter = openEditCounter,
        openCounterDetails = openCounterDetails,
    )
}


@Composable
internal fun CounterDetails(
    viewModel: CounterDetailsViewModel,
    navigateUp: () -> Unit,
    openEditCounter: (counterId: Long, wasTrackingLocation: Boolean) -> Unit,
    openCounterDetails: (counterId: Long) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
//    val viewState by viewModel.state.collectAsState()

    CounterDetails(
        viewState = viewState,
        navigateUp = navigateUp,
        openEditCounter = openEditCounter,
        openCounterDetails = openCounterDetails
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPagerApi::class
)
@Composable
internal fun CounterDetails(
    viewState: CounterDetailsViewState,
    navigateUp: () -> Unit,
    openEditCounter: (counterId: Long, wasTrackingLocation: Boolean) -> Unit,
    openCounterDetails: (counterId: Long) -> Unit, // used to open another counter
) {
    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }

    val pages = listOf("STATS", "DETAILS", "HISTORY")
    val pagerState = rememberPagerState()


    Scaffold(

        topBar = {
            TopAppBarWithBottomContent(
                title = { viewState.counterInfo?.counter?.name?.let { Text(text = it) } },
                contentPadding = WindowInsets.systemBars
                    .only(WindowInsetsSides.Horizontal)// + WindowInsetsSides.Top) // TODO why is top bad
                    .asPaddingValues(),
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        androidx.compose.material.Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_up)
                        )
                    }
                },
                actions = {
                    // More icon
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .clickable(onClick = { functionalityNotAvailablePopupShown = true })
                            .padding(horizontal = 12.dp, vertical = 16.dp)
                            .height(24.dp),
                        contentDescription = stringResource(id = R.string.more_options)
                    )
                },
                backgroundColor = MaterialTheme.colorScheme.surface.copy(
                    alpha = AppBarAlphas.translucentBarAlpha()
                ),
                bottomContent = {
                    Tabs(
                        pagerState = pagerState,
                        tabs = pages,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color.Transparent,
                        contentColor = LocalContentColor.current
                    )
                }
            )
        },
        floatingActionButton = {
            CounterFab(
                extended = true, // scrollstate.value == 0
                onFabClicked = {
                    viewState.counterInfo?.let {
                        openEditCounter(it.counter.id, it.counter.track_location ?: false) }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        CounterDetailsPager(
            pages = pages,
            viewState = viewState,
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxHeight()
                .bodyWidth()
                .background(MaterialTheme.colorScheme.primaryContainer),
            padding = padding
        )
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun CounterDetailsPager(
    pages: List<String>,
    viewState: CounterDetailsViewState,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    padding: PaddingValues
) {
    HorizontalPager(
        count = pages.size,
        state = pagerState,
        modifier = modifier,
        contentPadding = padding,
        verticalAlignment = Alignment.Top
    ) { pageIndex ->
        when (pageIndex) {
            0 -> viewState.counterInfo?.let {
                StatsTab(
                    it.counter,
                    viewState.counterInfo.increments,
                    viewState.counterInfo.hasLocations
                )
            }
            1 -> viewState.counterInfo?.let { DetailsTab(it.increments) }
            2 -> viewState.counterInfo?.let { HistoryTab(it.history) }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
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
    CountThisTheme {
        CounterFab(extended = true)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun TabsPreview() {
    val tabs = listOf(
        "STATS",
        "DETAILS",
        "HISTORY"
    )
    val pagerState = rememberPagerState()
//    val padding
//    Tabs(tabs = tabs, pagerState = pagerState, padding = padding)
}
