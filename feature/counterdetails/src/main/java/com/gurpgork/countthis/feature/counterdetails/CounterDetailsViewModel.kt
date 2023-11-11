package com.gurpgork.countthis.feature.counterdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurpgork.countthis.core.designsystem.component.UiMessageManager
import com.gurpgork.countthis.core.domain.interactors.DeleteCounter
import com.gurpgork.countthis.core.domain.interactors.DeleteHistory
import com.gurpgork.countthis.core.domain.interactors.DeleteIncrement
import com.gurpgork.countthis.core.domain.observers.ObserveCounterDetailed
import com.gurpgork.countthis.core.extensions.combine
import com.gurpgork.countthis.core.ui.CounterStateSelector
import com.gurpgork.countthis.feature.counterdetails.navigation.CounterDetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CounterDetailsViewModel @Inject constructor(
    private val deleteCounter: DeleteCounter,
    private val deleteIncrement: DeleteIncrement,
    private val deleteHistory: DeleteHistory,
    observeCounterDetailed: ObserveCounterDetailed,
    savedStateHandle: SavedStateHandle,
//TODO should add sorted increments grouped by date for easy displaying on details page
) : ViewModel() {
    private val addEditArgs: CounterDetailsArgs = CounterDetailsArgs(savedStateHandle)
    private var currentCounterId: Long = addEditArgs.counterId

    private val uiMessageManager = UiMessageManager()

    private val incrementSelection = CounterStateSelector()
    private val historySelection = CounterStateSelector()

    private val _state = MutableStateFlow(CounterDetailsViewState())
    val state = _state.asStateFlow()

    init {
        observeCounterDetailed(ObserveCounterDetailed.Params(currentCounterId))

        viewModelScope.launch {
            combine(
                observeCounterDetailed.flow,
                incrementSelection.observeSelectedIds(),
                historySelection.observeSelectedIds(),
                incrementSelection.observeIsSelectionOpen(),
                historySelection.observeIsSelectionOpen(),
                uiMessageManager.message,
            ) { counter, incrementIds, historyIds, isIncrementSelectionOpen, isHistorySelectionOpen, messages ->
                CounterDetailsViewState(
                    counter,
                    incrementIds,
                    historyIds,
                    isIncrementSelectionOpen,
                    isHistorySelectionOpen,
                    incrementIds.isNotEmpty() || historyIds.isNotEmpty(),
                    messages,
                )
            }.collect { _state.value = it }
        }
    }


    fun onLongClick(tab: CounterDetailTabs, id: Long){
        when(tab){
            CounterDetailTabs.STATS -> TODO()
            CounterDetailTabs.DETAILS -> onIncrementLongClick(id)
            CounterDetailTabs.HISTORY -> onHistoryLongClick(id)
        }
    }
    fun onClick(tab: CounterDetailTabs, id: Long){
        when(tab){
            CounterDetailTabs.STATS -> TODO()
            CounterDetailTabs.DETAILS -> onIncrementClick(id)
            CounterDetailTabs.HISTORY -> onHistoryClick(id)
        }
    }

    private fun onIncrementLongClick(id: Long) {
        incrementSelection.onItemLongClick(id)
    }

    private fun onIncrementClick(id: Long) {
        incrementSelection.onItemClick(id)
    }

    private fun onHistoryLongClick(id: Long) {
        historySelection.onItemLongClick(id)
    }

    private fun onHistoryClick(id: Long) {
        historySelection.onItemClick(id)
    }

    fun deleteCounter(deleteType: DeleteType, id: Long, listIndex: Int) {
        when (deleteType) {
            DeleteType.NONE -> assert(false)
            DeleteType.COUNTER -> {
                viewModelScope.launch {
                    deleteCounter(
                        DeleteCounter.Params(id, listIndex)
                    ).collect()
                }
            }

            DeleteType.INCREMENT -> {
                viewModelScope.launch {
                    deleteIncrement(
                        DeleteIncrement.Params(incrementSelection.getSelectedIds())
                    ).collect()
                    incrementSelection.clearSelection()
                }
            }

            DeleteType.HISTORY -> {
                viewModelScope.launch {
                    deleteHistory(
                        DeleteHistory.Params(historySelection.getSelectedIds())
                    ).collect()
                    historySelection.clearSelection()
                }
            }
        }

    }
}

enum class CounterDetailTabs {
    STATS, DETAILS, HISTORY
}