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
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
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

enum class DeleteType {
    NONE, COUNTER, INCREMENT, HISTORY
}

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
        onDelete = { deleteType, id, listIndex ->
            viewModel.deleteCounter(
                deleteType, id, listIndex
            )
        },
        onComposing = onComposing,
        hasSelectedItems = viewModel.hasSelectedItems(),
        onRowLongClick = viewModel::onLongClick,
        onRowClick = viewModel::onClick,
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
    onDelete: (deleteType: DeleteType, counterId: Long, listIndex: Int) -> Unit,
    onComposing: (CtAppBarState) -> Unit,
    hasSelectedItems: Boolean,
    onRowLongClick: (CounterDetailTabs, Long) -> Unit,
    onRowClick: (CounterDetailTabs, Long) -> Unit,
) {
    var whatToDelete by remember { mutableStateOf(DeleteType.NONE) }

    val pages = listOf("STATS", "DETAILS", "HISTORY")
    val pagerState = rememberPagerState(pageCount = { pages.size })


    //TODO when viewState.isHistorySelectionOpen or viewState.isIncrementSelectionOpen update appbar
    LaunchedEffect(key1 = viewState.counterInfo) {
        val title = viewState.counterInfo?.counter?.name ?: ""
        onComposing(
            CtAppBarState(title = title, navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        CtIcons.ArrowBack,
                        contentDescription = stringResource(R.string.cd_navigate_up)
                    )
                }
            }, actions = {
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
                        whatToDelete = when (pagerState.currentPage) {
                            0 -> DeleteType.COUNTER
                            1 -> DeleteType.INCREMENT
                            2 -> DeleteType.HISTORY
                            else -> DeleteType.NONE
                        }
                        if (!hasSelectedItems) whatToDelete = DeleteType.COUNTER
                    }
                }) {
                    Icon(
                        imageVector = CtIcons.Delete, contentDescription = "delete"
                    )
                }
            })
        )
    }

    if (whatToDelete != DeleteType.NONE) {
        viewState.counterInfo?.let {
            val toDeleteOption =
                if (!hasSelectedItems || pagerState.currentPage == 0) it.counter.name
                else if (pagerState.currentPage == 1) "increment(s)"
                else "history"

            DeleteCounterAlertDialog(counterName = toDeleteOption, onConfirm = {
                onDelete(whatToDelete, it.counter.id, it.counter.listIndex)
                if (whatToDelete == DeleteType.COUNTER) navigateUp()
            }, onDismiss = { whatToDelete = DeleteType.NONE })
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
                .background(MaterialTheme.colorScheme.primaryContainer),
            onRowLongClick = onRowLongClick,
            onRowClick = onRowClick,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CounterDetailsPager(
    viewState: CounterDetailsViewState,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    onRowLongClick: (CounterDetailTabs, Long) -> Unit,
    onRowClick: (CounterDetailTabs, Long) -> Unit,
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
                StatsTab(it.counter, it.increments, it.hasLocations, onMapMoved = { movingMap ->
                    // only update userScrollEnabled when a new val comes in
                    if (movingMap == userScrollEnabled) userScrollEnabled = !movingMap
                })
            }

            1 -> viewState.counterInfo?.let { counter ->
                DetailsTab(counter.increments,
                    viewState.selectedIncrementIds,
                    onRowLongClick = { onRowLongClick(CounterDetailTabs.DETAILS, it) },
                    onRowClick = { onRowClick(CounterDetailTabs.DETAILS, it) })
            }

            2 -> viewState.counterInfo?.let { counter ->
                HistoryTab(counter.history,
                    viewState.selectedHistoryIds,
                    onRowLongClick = { onRowLongClick(CounterDetailTabs.HISTORY, it) },
                    onRowClick = { onRowClick(CounterDetailTabs.HISTORY, it) })
            }
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

    PrimaryTabRow(
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
            Tab(text = { Text(text = title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })
        }
    }
}


fun AppBar(){

}
