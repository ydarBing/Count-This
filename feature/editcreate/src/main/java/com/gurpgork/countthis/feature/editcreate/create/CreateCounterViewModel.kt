package com.gurpgork.countthis.feature.editcreate.create

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurpgork.countthis.core.data.repository.UserDataRepository
import com.gurpgork.countthis.core.domain.interactors.AddCounter
import com.gurpgork.countthis.feature.editcreate.CounterFormEvent
import com.gurpgork.countthis.feature.editcreate.CounterFormViewState
import com.gurpgork.countthis.feature.editcreate.ValidationEvent
import com.gurpgork.countthis.feature.editcreate.Validator
import com.gurpgork.countthis.feature.editcreate.asExternalModel
import com.gurpgork.countthis.feature.editcreate.create.CreateUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class CreateCounterViewModel @Inject constructor(
    private val addCounter: AddCounter,
    private val userDataRepository: UserDataRepository,
//    private val preferences: CountThisPreferences,
) : ViewModel() {

    private val _uiState: MutableStateFlow<CreateUiState> = MutableStateFlow(
        CreateUiState.Success(
            CounterFormViewState(
                incrementByPlaceholder = "1",
                namePlaceholder = "",
                startCountPlaceholder = "0",
                goalPlaceholder = "0",
            )
        )
    )

    val uiState: StateFlow<CreateUiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CreateUiState.Success(
                CounterFormViewState(
                    incrementByPlaceholder = "1",
                    namePlaceholder = "",
                    startCountPlaceholder = "0",
                    goalPlaceholder = "0",
                )
            )
        )


//    val countersTrackingLocation: Flow<Int> = userDataRepository.userData
//        .map { it.countersTrackingLocation }

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()


    private fun addCounter(/*form: CounterFormViewState*/) {
        viewModelScope.launch {
            val newCounter = (_uiState.value as Success).form.asExternalModel()

            if (newCounter.trackLocation) {
                // user has accepted location permission and wants to track this counter
                // tell preferences that we should try getting locations
                // getting locations will still have to check for permission
                //preferences.requestingLocationUpdates += 1
                userDataRepository.incrementCountersTrackingLocation()
            }
            addCounter(AddCounter.Params(newCounter)).collect()
        }
    }

    // makes sure integer text fields only have digits in them
    fun onEvent(event: CounterFormEvent) {
        when (event) {
            is CounterFormEvent.NameChanged -> {
                    (_uiState.value as Success).form =
                    (_uiState.value as Success).form.copy(name = event.name)
//                form.value = form.value.copy(name = event.name)
            }

            is CounterFormEvent.CountChanged -> {
                if (event.count.isEmpty() || event.count.isDigitsOnly())
                    (_uiState.value as Success).form =
                        (_uiState.value as Success).form.copy(startCount = event.count)
//                    form.value = form.value.copy(startCount = event.count)
            }

            is CounterFormEvent.GoalChanged -> {
                if (event.goal.isEmpty() || event.goal.isDigitsOnly())
                    (_uiState.value as Success).form =
                        (_uiState.value as Success).form.copy(goal = event.goal)
//                    form.value = form.value.copy(goal = event.goal)
            }

            is CounterFormEvent.IncrementChanged -> {
                if (event.increment.isEmpty() || event.increment.isDigitsOnly())
                    (_uiState.value as Success).form =
                        (_uiState.value as Success).form.copy(incrementBy = event.increment)
//                    form.value = form.value.copy(incrementBy = event.increment)
            }

            is CounterFormEvent.TrackLocationChanged -> {
                (_uiState.value as Success).form =
                    (_uiState.value as Success).form.copy(trackLocation = event.isTracking)
//                form.value = form.value.copy(trackLocation = event.isTracking)
            }

            CounterFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val nameResult = Validator.validateName((_uiState.value as Success).form.name)
        val countResult = Validator.validateCount((_uiState.value as Success).form.startCount)
        val incrementResult =
            Validator.validateIncrement((_uiState.value as Success).form.incrementBy)
        val goalResult = Validator.validateGoal((_uiState.value as Success).form.goal)
        val trackResult =
            Validator.validateTrackLocation((_uiState.value as Success).form.trackLocation)

        (_uiState.value as Success).form = (_uiState.value as Success).form.copy(
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
//                uiMessageManager.emitMessage(UiMessage(formStuff.value.name))
                validationEventChannel.send(ValidationEvent.Success)
            }
        }
    }
}

sealed interface CreateUiState {
    //    object Loading : CreateUiState
    data class Success(var form: CounterFormViewState) : CreateUiState

    //    data class Success(val createViewState: CreateCounterViewState) : CreateUiState
    object Error : CreateUiState
//    data class Error(val exception: Throwable) : CreateUiState
}