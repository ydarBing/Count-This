package com.gurpgork.countthis.feature.counterdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurpgork.countthis.core.designsystem.component.UiMessageManager
import com.gurpgork.countthis.core.domain.interactors.DeleteCounter
import com.gurpgork.countthis.core.domain.observers.ObserveCounterDetailed
import com.gurpgork.countthis.feature.counterdetails.navigation.CounterDetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CounterDetailsViewModel @Inject constructor(
    private val deleteCounter: DeleteCounter,
    observeCounterDetailed: ObserveCounterDetailed,
    savedStateHandle: SavedStateHandle,
//TODO should add sorted increments grouped by date for easy displaying on details page
) : ViewModel() {
    private val addEditArgs: CounterDetailsArgs = CounterDetailsArgs(savedStateHandle)
    var currentCounterId: Long = addEditArgs.counterId

    private val uiMessageManager = UiMessageManager()

    private val _state = MutableStateFlow(CounterDetailsViewState())
    val state = _state.asStateFlow()

    init {
        observeCounterDetailed(ObserveCounterDetailed.Params(currentCounterId))

        viewModelScope.launch {
            combine(
                observeCounterDetailed.flow,
                uiMessageManager.message,
            ){ counter, messages ->
                CounterDetailsViewState(counter, messages)
            }.collect{_state.value = it}
        }
    }

    fun deleteCounter(id: Long) {
        viewModelScope.launch {
            deleteCounter(
                DeleteCounter.Params(id)
            ).collect()
        }
    }
}