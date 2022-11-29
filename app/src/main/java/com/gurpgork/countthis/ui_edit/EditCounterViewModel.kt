package com.gurpgork.countthis.ui_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurpgork.countthis.compose.UiMessageManager
import com.gurpgork.countthis.data.daos.CounterDao
import com.gurpgork.countthis.domain.interactors.UpdateCounter
import com.gurpgork.countthis.settings.CountThisPreferences
import com.gurpgork.countthis.ui_create.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCounterViewModel @Inject constructor(
    private val preferences: CountThisPreferences,
    private val updateCounter: UpdateCounter,
    dao: CounterDao,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    //TODO move/merge all this to CreateCounterViewModel because it's so similar
    private val counterId: Long = savedStateHandle["counterId"]!!
    private val wasTrackingLocation: Boolean = savedStateHandle["wasTrackingLocation"]!!
    private val uiMessageManager = UiMessageManager()
//    val validationEvent = MutableSharedFlow<ValidationEvent>()

    var state by mutableStateOf(EditCounterViewState(CreateCounterViewState.Empty))

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val counterEntity = dao.getCounter(counterId)
            if(counterEntity != null)
            {
                state.counterInfo.name = counterEntity.name
                state.counterInfo.incrementBy = counterEntity.increment
                state.counterInfo.goal = counterEntity.goal
                state.counterInfo.startCount = counterEntity.count
                state.counterInfo.trackLocation = counterEntity.track_location == true
            }
        }
    }

    private fun updateCounter() {
        viewModelScope.launch {
            state.counterInfo.let {
                if (wasTrackingLocation && !it.trackLocation) {
                    // were tracking location and now NOT wanting to, update preferences
                    preferences.requestingLocationUpdates -= 1 // TODO test because this could potentially set this preference to negative

                } else if (!wasTrackingLocation && it.trackLocation) {
                    // were NOT tracking location and now want to, update preferences
                    preferences.requestingLocationUpdates += 1
                }
                updateCounter(UpdateCounter.Params(it.toCounterEntity()))
            }
        }
    }

    fun onEvent(event: CounterFormEvent) {
        when (event) {
            is CounterFormEvent.NameChanged -> {
                state.counterInfo = state.counterInfo.copy(name = event.name)
            }
            is CounterFormEvent.CountChanged -> {
                state.counterInfo = state.counterInfo.copy(startCount = event.count)
            }
            is CounterFormEvent.GoalChanged -> {
                state.counterInfo = state.counterInfo.copy(goal = event.goal)
            }
            is CounterFormEvent.IncrementChanged -> {
                state.counterInfo = state.counterInfo.copy(incrementBy = event.increment)
            }
            is CounterFormEvent.TrackLocationChanged -> {
                state.counterInfo =
                    state.counterInfo.copy(trackLocation = event.isTracking)
            }
            is CounterFormEvent.Submit -> {
                validateInputs()
            }
        }
    }

    fun validateInputs() {
        val nameResult = Validator.validateName(state.counterInfo.name)
        val countResult = Validator.validateCount(state.counterInfo.startCount)
        val incrementResult = Validator.validateIncrement(state.counterInfo.incrementBy)
        val goalResult = Validator.validateGoal(state.counterInfo.goal)
        val trackResult =
            Validator.validateTrackLocation(state.counterInfo.trackLocation ?: false)
        val hasError = listOf(
            nameResult,
            countResult,
            incrementResult,
            goalResult,
            trackResult
        ).any { !it.status }

        viewModelScope.launch {
            if (!hasError) {
                updateCounter()
                validationEventChannel.send(ValidationEvent.Success)
            }
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}