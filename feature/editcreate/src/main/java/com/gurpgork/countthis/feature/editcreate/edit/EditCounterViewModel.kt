package com.gurpgork.countthis.feature.editcreate.edit

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.data.repository.UserDataRepository
import com.gurpgork.countthis.core.designsystem.component.UiMessageManager
import com.gurpgork.countthis.core.domain.interactors.UpdateCounter
import com.gurpgork.countthis.core.model.data.Counter
import com.gurpgork.countthis.feature.editcreate.CounterFormEvent
import com.gurpgork.countthis.feature.editcreate.CounterFormViewState
import com.gurpgork.countthis.feature.editcreate.ValidationEvent
import com.gurpgork.countthis.feature.editcreate.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCounterViewModel @Inject constructor(
//    private val preferences: CountThisPreferences,
    private val userDataRepository: UserDataRepository,
    private val updateCounter: UpdateCounter,
    counterRepository: CounterRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    //TODO move/merge all this to CreateCounterViewModel because it's so similar
    private val counterId: Long = savedStateHandle["counterId"]!!
    private val wasTrackingLocation: Boolean = savedStateHandle["wasTrackingLocation"]!!
    private val uiMessageManager = UiMessageManager()
    private var editingCounter: Counter = counterRepository.getCounter(counterId)

    //    val validationEvent = MutableSharedFlow<ValidationEvent>()
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    //    private val formStuff: MutableStateFlow<CounterFormViewState> = MutableStateFlow(
//        CounterFormViewState(
//            name = editingCounter.name,
//            incrementBy = editingCounter.increment.toString(),
//            goal = editingCounter.goal.toString(),
//            startCount = editingCounter.count.toString(),
//            trackLocation = editingCounter.trackLocation,
//
//            namePlaceholder = editingCounter.name,
//            incrementByPlaceholder = editingCounter.increment.toString(),
//            goalPlaceholder = editingCounter.goal.toString(),
//            startCountPlaceholder = editingCounter.count.toString(),
////            incrementByPlaceholder = "1",
////            namePlaceholder = "",
////            startCountPlaceholder = "0",
////            goalPlaceholder = "0",
//        )
//    )
    private val _uiState: MutableStateFlow<EditUiState> = MutableStateFlow(
        EditUiState.Success(
            CounterFormViewState(
                name = editingCounter.name,
                incrementBy = editingCounter.increment.toString(),
                goal = editingCounter.goal.toString(),
                startCount = editingCounter.count.toString(),
                trackLocation = editingCounter.trackLocation,

                namePlaceholder = editingCounter.name,
                incrementByPlaceholder = editingCounter.increment.toString(),
                goalPlaceholder = editingCounter.goal.toString(),
                startCountPlaceholder = editingCounter.count.toString(),
            )
        )
    )

    val state: StateFlow<EditUiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EditUiState.Success(
                CounterFormViewState(
                    name = editingCounter.name,
                    incrementBy = editingCounter.increment.toString(),
                    goal = editingCounter.goal.toString(),
                    startCount = editingCounter.count.toString(),
                    trackLocation = editingCounter.trackLocation,

                    namePlaceholder = editingCounter.name,
                    incrementByPlaceholder = editingCounter.increment.toString(),
                    goalPlaceholder = editingCounter.goal.toString(),
                    startCountPlaceholder = editingCounter.count.toString(),
                )
            )
        )

    fun onEvent(event: CounterFormEvent) {
        when (event) {
            is CounterFormEvent.NameChanged -> {
                (_uiState.value as EditUiState.Success).form =
                    (_uiState.value as EditUiState.Success).form.copy(name = event.name)
            }

            is CounterFormEvent.CountChanged -> {
                if (event.count.isEmpty() || event.count.isDigitsOnly())
                    (_uiState.value as EditUiState.Success).form =
                        (_uiState.value as EditUiState.Success).form.copy(startCount = event.count)
            }

            is CounterFormEvent.GoalChanged -> {
                if (event.goal.isEmpty() || event.goal.isDigitsOnly())
                    (_uiState.value as EditUiState.Success).form =
                        (_uiState.value as EditUiState.Success).form.copy(goal = event.goal)
            }

            is CounterFormEvent.IncrementChanged -> {
                if (event.increment.isEmpty() || event.increment.isDigitsOnly())
                    (_uiState.value as EditUiState.Success).form =
                        (_uiState.value as EditUiState.Success).form.copy(incrementBy = event.increment)
            }

            is CounterFormEvent.TrackLocationChanged -> {
                (_uiState.value as EditUiState.Success).form =
                    (_uiState.value as EditUiState.Success).form.copy(trackLocation = event.isTracking)
            }

            is CounterFormEvent.Submit -> {
                validateInputs()
            }
        }
    }


    private fun updateCounter() {
        viewModelScope.launch {
            if (wasTrackingLocation && !(_uiState.value as EditUiState.Success).form.trackLocation) {
                userDataRepository.decrementCountersTrackingLocation()
                // were tracking location and now NOT wanting to, update preferences
//                preferences.requestingLocationUpdates -= 1 // TODO test because this could potentially set this preference to negative

            } else if (!wasTrackingLocation && (_uiState.value as EditUiState.Success).form.trackLocation) {
                // were NOT tracking location and now want to, update preferences
//                preferences.requestingLocationUpdates += 1
                userDataRepository.incrementCountersTrackingLocation()
            }

            //TODO this is ugly, make it better
            editingCounter = editingCounter.copy(
                name = (_uiState.value as EditUiState.Success).form.name,
                trackLocation = (_uiState.value as EditUiState.Success).form.trackLocation,
                count = if ((_uiState.value as EditUiState.Success).form.startCount.isEmpty())
                    editingCounter.count else (_uiState.value as EditUiState.Success).form.startCount.toInt(),
                goal = if ((_uiState.value as EditUiState.Success).form.startCount.isEmpty())
                    editingCounter.goal else (_uiState.value as EditUiState.Success).form.goal.toInt(),
                increment = if ((_uiState.value as EditUiState.Success).form.startCount.isEmpty())
                    editingCounter.increment else (_uiState.value as EditUiState.Success).form.incrementBy.toInt(),
            )

            updateCounter.executeSync(UpdateCounter.Params(editingCounter))
//            updateCounter.executeSync(UpdateCounter.Params(state.value.toCounterEntity(counterId)))
        }
    }

    private fun validateInputs() {
        val nameResult = Validator.validateName((_uiState.value as EditUiState.Success).form.name)
        val countResult =
            Validator.validateCount((_uiState.value as EditUiState.Success).form.startCount)
        val incrementResult =
            Validator.validateIncrement((_uiState.value as EditUiState.Success).form.incrementBy)
        val goalResult = Validator.validateGoal((_uiState.value as EditUiState.Success).form.goal)
        val trackResult =
            Validator.validateTrackLocation((_uiState.value as EditUiState.Success).form.trackLocation)
        val hasError = listOf(
            nameResult,
            countResult,
            incrementResult,
            goalResult,
            trackResult
        ).any { !it.status }

        if (!hasError) {
            viewModelScope.launch {
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

sealed interface EditUiState {
    //    object Loading : CreateUiState
    data class Success(var form: CounterFormViewState) : EditUiState

    //    data class Success(val createViewState: CreateCounterViewState) : CreateUiState
    object Error : EditUiState
//    data class Error(val exception: Throwable) : CreateUiState
}