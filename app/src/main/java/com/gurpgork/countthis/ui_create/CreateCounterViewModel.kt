package com.gurpgork.countthis.ui_create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.domain.interactors.AddCounter
import com.gurpgork.countthis.settings.CountThisPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject


@HiltViewModel
internal class CreateCounterViewModel @Inject constructor(
    private val addCounter: AddCounter,
    private val preferences: CountThisPreferences,
) : ViewModel() {

    var state by mutableStateOf(CreateCounterViewState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()


    private fun addCounter() {
        viewModelScope.launch {
            val now = OffsetDateTime.now()
            val newCounter = CounterEntity(
                track_location = state.trackLocation,
                count = state.startCount,
                increment = state.incrementBy,
                name = state.name,
                goal = state.goal,
                creation_date_time = now,
            )
            if (state.trackLocation) {
                // user has accepted location permission and wants to track this counter
                // tell preferences that we should try getting locations
                // getting locations will still have to check for permission
                preferences.requestingLocationUpdates += 1
            }
            addCounter(AddCounter.Params(newCounter)).collect()
        }
    }

//    fun clearMessage(id: Long) {
//        viewModelScope.launch {
//            uiMessageManager.clearMessage(id)
//        }
//    }

    fun onEvent(event: CounterFormEvent){
        when(event){
            is CounterFormEvent.NameChanged -> { state = state.copy(name  = event.name)}
            is CounterFormEvent.CountChanged -> { state = state.copy(startCount  = event.count)}
            is CounterFormEvent.GoalChanged -> { state = state.copy(goal  = event.goal)}
            is CounterFormEvent.IncrementChanged -> { state = state.copy(incrementBy  = event.increment)}
            is CounterFormEvent.TrackLocationChanged -> { state = state.copy(trackLocation  = event.isTracking)}
            CounterFormEvent.Submit -> { submitData() }
        }
    }
    private fun submitData(){
        val nameResult = Validator.validateName(state.name)
        val countResult = Validator.validateCount(state.startCount)
        val incrementResult = Validator.validateIncrement(state.incrementBy)
        val goalResult = Validator.validateGoal(state.goal)
        val trackResult = Validator.validateTrackLocation(state.trackLocation)

        state = state.copy(
            hasNameError = !nameResult.status,
            hasStartCountError = !countResult.status,
            hasIncrementError = !incrementResult.status,
            hasGoalError = !goalResult.status,
            hasTrackLocationError = !trackResult.status,
        )

        val hasError = listOf(
            nameResult,
            countResult,
            incrementResult,
            goalResult,
            trackResult
        ).any { !it.status }

        viewModelScope.launch {
            if (!hasError) {
                addCounter()
                validationEventChannel.send(ValidationEvent.Success)
            }
        }
    }
}