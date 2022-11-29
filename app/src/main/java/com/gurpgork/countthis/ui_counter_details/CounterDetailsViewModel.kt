package com.gurpgork.countthis.ui_counter_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurpgork.countthis.compose.UiMessageManager
import com.gurpgork.countthis.domain.observers.ObserveCounterDetailed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CounterDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeCounterDetailed: ObserveCounterDetailed
//TODO should add sorted increments grouped by date for easy displaying on details page
) : ViewModel() {
    private val counterId: Long = savedStateHandle["counterId"]!!
    private val uiMessageManager = UiMessageManager()

    private val _state = MutableStateFlow(CounterDetailsViewState())
    val state: StateFlow<CounterDetailsViewState>
        get() =  _state

    init {
        observeCounterDetailed(ObserveCounterDetailed.Params(counterId))

        viewModelScope.launch {
            combine(
                observeCounterDetailed.flow,
                uiMessageManager.message,
            ){ counter, messages ->
                CounterDetailsViewState(counter, messages)
            }.collect{_state.value = it}
        }
    }
}