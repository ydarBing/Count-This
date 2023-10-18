package com.gurpgork.countthis.feature.addedit

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurpgork.countthis.core.data.repository.UserDataRepository
import com.gurpgork.countthis.core.designsystem.component.UiMessageManager
import com.gurpgork.countthis.core.domain.GetCounter
import com.gurpgork.countthis.core.domain.interactors.UpsertCounter
import com.gurpgork.countthis.core.model.data.CREATE_COUNTER_ID
import com.gurpgork.countthis.core.model.data.Counter
import com.gurpgork.countthis.feature.addedit.navigation.AddEditArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class AddEditCounterViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val upsertCounter: UpsertCounter,
    private val getCounter: GetCounter,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val addEditArgs: AddEditArgs = AddEditArgs(savedStateHandle)
    var currentCounterId: Long = addEditArgs.counterId

    private val uiMessageManager = UiMessageManager()

    private var editingCounter: Counter? = null
    private var currentCounterListIndex: Int = -1
    private var wasTrackingLocation: Boolean = false

    //    val validationEvent = MutableSharedFlow<ValidationEvent>()
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(
        CounterFormViewState(
            namePlaceholder = "Enter Name",
            startCountPlaceholder = "0",
            incrementByPlaceholder = "1",
            goalPlaceholder = "0",
        )
    )
    val uiState = _state.asStateFlow()


    init {
        if (currentCounterId != CREATE_COUNTER_ID) {
            // we're editing a counter
            viewModelScope.launch {
                getCounter(currentCounterId)?.also { counter ->
                    editingCounter = counter

                    wasTrackingLocation = counter.trackLocation
                    currentCounterListIndex = counter.listIndex
                    _state.update {
                        it.copy(
                            namePlaceholder = counter.name,
                            name = counter.name,
                            startCountPlaceholder = counter.count.toString(),
                            startCount = counter.count.toString(),
                            incrementByPlaceholder = counter.increment.toString(),
                            incrementBy = counter.increment.toString(),
                            goalPlaceholder = counter.goal.toString(),
                            goal = counter.goal.toString(),
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: CounterFormEvent) {
        when (event) {
            is CounterFormEvent.NameChanged -> {
                _state.update { it.copy(name = event.name) }
            }

            is CounterFormEvent.CountChanged -> {
                if (event.count.isEmpty() || event.count.isDigitsOnly())
                    _state.update { it.copy(startCount = event.count) }
            }

            is CounterFormEvent.GoalChanged -> {
                if (event.goal.isEmpty() || event.goal.isDigitsOnly())
                    _state.update { it.copy(goal = event.goal) }
            }

            is CounterFormEvent.IncrementChanged -> {
                if (event.increment.isEmpty() || event.increment.isDigitsOnly())
                    _state.update { it.copy(incrementBy = event.increment) }
            }

            is CounterFormEvent.TrackLocationChanged -> {
                _state.update { it.copy(trackLocation = event.isTracking) }
            }

            is CounterFormEvent.Submit -> {
                validateInputs()
            }
        }
    }

    private fun validateInputs() {
        val nameResult = Validator.validateName(_state.value.name)
        val countResult = Validator.validateCount(_state.value.startCount)
        val incrementResult = Validator.validateIncrement(_state.value.incrementBy)
        val goalResult = Validator.validateGoal(_state.value.goal)
        val trackResult = Validator.validateTrackLocation(_state.value.trackLocation)
        _state.update {
            it.copy(
                hasNameError = !nameResult.status,
                hasStartCountError = !countResult.status,
                hasIncrementError = !incrementResult.status,
                hasGoalError = !goalResult.status,
                hasTrackLocationError = !trackResult.status,
            )
        }
        val hasError = listOf(
            nameResult, countResult, incrementResult, goalResult, trackResult
        ).any { !it.status }

        if (!hasError) {
            viewModelScope.launch {
                upsertCounter()
                validationEventChannel.send(ValidationEvent.Success)
            }
        }
    }

    private suspend fun upsertCounter() {
        val editedCounter = _state.value.asExternalModel(
            listIndex = editingCounter?.listIndex ?: addEditArgs.numCounters,
            creationDate = editingCounter?.creationDate ?: Clock.System.now(),
            id = currentCounterId,
        )

        // creating a new counter that tracks location
        if (currentCounterId == CREATE_COUNTER_ID && editedCounter.trackLocation) {
            userDataRepository.incrementCountersTrackingLocation()
        }
        // editing counter to not track location anymore
        else if (wasTrackingLocation && !editedCounter.trackLocation) {
            userDataRepository.decrementCountersTrackingLocation()
        }
        // editing counter that was NOT tracking location and now IS
        else if (!wasTrackingLocation && editedCounter.trackLocation) {
            userDataRepository.incrementCountersTrackingLocation()
        }

        upsertCounter.executeSync(UpsertCounter.Params(editedCounter))
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}

sealed interface AddEditUiState {
    //    object Loading : CreateUiState
    data class Success(var form: CounterFormViewState) : AddEditUiState
    object Error : AddEditUiState
//    data class Error(val exception: Throwable) : CreateUiState
}