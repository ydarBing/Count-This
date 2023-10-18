package com.gurpgork.countthis.feature.counterdetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurpgork.countthis.core.designsystem.component.CtAppBarState
import com.gurpgork.countthis.core.designsystem.component.bodyWidth
import com.gurpgork.countthis.core.designsystem.component.dialog.DeleteCounterAlertDialog
import com.gurpgork.countthis.core.designsystem.component.pagerTabIndicatorOffset
import com.gurpgork.countthis.core.designsystem.icon.CtIcons
import kotlinx.coroutines.launch

@Composable
internal fun CounterDetailsRoute(
    navigateUp: () -> Unit,
    openEditCounter: (counterId: Long, listIndex: Int) -> Unit,
    onComposing: (CtAppBarState) -> Unit,
) {
    CounterDetails(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
        openEditCounter = openEditCounter,
        onComposing = onComposing,
    )
}

@Composable
internal fun CounterDetails(
    viewModel: CounterDetailsViewModel,
    navigateUp: () -> Unit,
    openEditCounter: (counterId: Long, listIndex: Int) -> Unit,
    onComposing: (CtAppBarState) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    CounterDetails(
        viewState = viewState,
        navigateUp = navigateUp,
        openEditCounter = openEditCounter,
        onDeleteCounter = { id -> viewModel.deleteCounter(id) },
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
    openEditCounter: (counterId: Long, listIndex: Int) -> Unit,
    onDeleteCounter: (counterId: Long) -> Unit,
    onComposing: (CtAppBarState) -> Unit,
) {
    var wantsToDelete by remember { mutableStateOf(false) }

    //TODO only make history and details if there are elements in each respective list
    val pages = listOf("STATS", "DETAILS", "HISTORY")
    val pagerState = rememberPagerState(pageCount = { pages.size })

    LaunchedEffect(key1 = viewState.counterInfo) {
        val title = viewState.counterInfo?.counter?.name ?: ""
        onComposing(
            CtAppBarState(
                title = title,
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            CtIcons.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_up)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewState.counterInfo?.let {
                            openEditCounter(it.counter.id, it.counter.listIndex)
                        }
                    }) {
                        Icon(
                            imageVector = CtIcons.Edit, contentDescription = "edit"
                        )
                    }
                    IconButton(onClick = {
                        viewState.counterInfo?.let {
                            wantsToDelete = true
                        }
                    }) {
                        Icon(
                            imageVector = CtIcons.Delete, contentDescription = "delete"
                        )
                    }
                })
        )
    }

    if (wantsToDelete) {
        viewState.counterInfo?.let {
            DeleteCounterAlertDialog(
                counterName = it.counter.name,
                onConfirm = {
                    onDeleteCounter(it.counter.id)
                    navigateUp()
                },
                onDismiss = { wantsToDelete = false })
        }
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
    viewState: CounterDetailsViewState,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    var userScrollEnabled by remember { mutableStateOf(true) }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        userScrollEnabled = userScrollEnabled
    ) { pageIndex ->
        when (pageIndex) {
            0 -> viewState.counterInfo?.let {
                StatsTab(
                    it.counter,
                    it.increments,
                    it.hasLocations,
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
            SecondaryIndicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        },
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                text = { Text(text = title) },
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

