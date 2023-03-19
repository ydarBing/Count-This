package com.gurpgork.countthis.ui_create

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurpgork.countthis.compose.UiMessage
import com.gurpgork.countthis.compose.UiMessageManager
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.domain.interactors.AddCounter
import com.gurpgork.countthis.settings.CountThisPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject


@HiltViewModel
internal class CreateCounterViewModel @Inject constructor(
    private val addCounter: AddCounter,
    private val preferences: CountThisPreferences,
) : ViewModel() {

    private val uiMessageManager = UiMessageManager()
//    var state by mutableStateOf(CreateCounterViewState(CounterFormViewState.Empty))
//    private val _state by mutableStateOf(CreateCounterViewState(CounterFormViewState.Empty))
//    val state: State<CreateCounterViewState> = _state
    private val formStuff: MutableStateFlow<CounterFormViewState> = MutableStateFlow(
        CounterFormViewState()
    )
    val state: StateFlow<CreateCounterViewState> = combine(
        formStuff,
        uiMessageManager.message,
        ::CreateCounterViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CreateCounterViewState.Empty
    )


    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()


    private fun addCounter() {
        viewModelScope.launch {
            val now = OffsetDateTime.now()
            val newCounter = CounterEntity(
                name = formStuff.value.name,
                track_location = formStuff.value.trackLocation,//state.value.form.trackLocation,
                count = if(formStuff.value.startCount.isEmpty()) CounterEntity.EMPTY_COUNTER.count else formStuff.value.startCount.toInt(),
                increment = if(formStuff.value.incrementBy.isEmpty()) CounterEntity.EMPTY_COUNTER.increment else formStuff.value.incrementBy.toInt(),
                goal = if(formStuff.value.goal.isEmpty()) CounterEntity.EMPTY_COUNTER.goal else formStuff.value.goal.toInt(),
                creation_date_time = now,
            )
            if (formStuff.value.trackLocation) {
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

    // makes sure integer text fields only have digits in them
    fun onEvent(event: CounterFormEvent) {
        when (event) {
            is CounterFormEvent.NameChanged -> {
                formStuff.value = formStuff.value.copy(name = event.name)
            }
            is CounterFormEvent.CountChanged -> {
                if(event.count.isEmpty() || event.count.isDigitsOnly())
                    formStuff.value = formStuff.value.copy(startCount = event.count)
            }
            is CounterFormEvent.GoalChanged -> {
                if(event.goal.isEmpty() || event.goal.isDigitsOnly())
                    formStuff.value = formStuff.value.copy(goal = event.goal)
            }
            is CounterFormEvent.IncrementChanged -> {
                if(event.increment.isEmpty() || event.increment.isDigitsOnly())
                    formStuff.value = formStuff.value.copy(incrementBy = event.increment)
            }
            is CounterFormEvent.TrackLocationChanged -> {
                formStuff.value = formStuff.value.copy(trackLocation = event.isTracking)
            }
            CounterFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val nameResult = Validator.validateName(formStuff.value.name)
        val countResult = Validator.validateCount(formStuff.value.startCount)
        val incrementResult = Validator.validateIncrement(formStuff.value.incrementBy)
        val goalResult = Validator.validateGoal(formStuff.value.goal)
        val trackResult = Validator.validateTrackLocation(formStuff.value.trackLocation)

        formStuff.value = formStuff.value.copy(
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
                uiMessageManager.emitMessage(UiMessage(formStuff.value.name))
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