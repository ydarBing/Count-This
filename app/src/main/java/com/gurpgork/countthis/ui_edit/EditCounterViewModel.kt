package com.gurpgork.countthis.ui_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurpgork.countthis.compose.UiMessageManager
import com.gurpgork.countthis.data.daos.CounterDao
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.domain.interactors.UpdateCounter
import com.gurpgork.countthis.settings.CountThisPreferences
import com.gurpgork.countthis.ui_create.CounterFormEvent
import com.gurpgork.countthis.ui_create.CounterFormViewState
import com.gurpgork.countthis.ui_create.ValidationEvent
import com.gurpgork.countthis.ui_create.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCounterViewModel @Inject constructor(
    private val preferences: CountThisPreferences,
    private val updateCounter: UpdateCounter,
    dao: CounterDao,
    savedStateHandle: SavedStateHandle,
//    initialState: CounterFormViewState,
) : ViewModel() {
    //TODO move/merge all this to CreateCounterViewModel because it's so similar
    private val counterId: Long = savedStateHandle["counterId"]!!
    private val wasTrackingLocation: Boolean = savedStateHandle["wasTrackingLocation"]!!
    private val uiMessageManager = UiMessageManager()
    private var editingCounter: CounterEntity = CounterEntity.EMPTY_COUNTER
//    val validationEvent = MutableSharedFlow<ValidationEvent>()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val formStuff: MutableStateFlow<CounterFormViewState> = MutableStateFlow(
        CounterFormViewState()
    )

    val state: StateFlow<EditCounterViewState> = combine(
        formStuff,
        uiMessageManager.message,
        ::EditCounterViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = EditCounterViewState.Empty
    )

    init {
        viewModelScope.launch {
            editingCounter = dao.getCounter(counterId)!!
            formStuff.value = formStuff.value.copy(
                name = editingCounter.name,
                incrementBy = editingCounter.increment,
                goal = editingCounter.goal,
                startCount = editingCounter.count,
                trackLocation = editingCounter.track_location == true
            )
        }
    }

    fun onEvent(event: CounterFormEvent) {
        when (event) {
            is CounterFormEvent.NameChanged -> {
                formStuff.value = formStuff.value.copy(name = event.name)
            }//{ bad does not work...formStuff.value.name = event.name }
            is CounterFormEvent.CountChanged -> {
                formStuff.value = formStuff.value.copy(startCount = event.count)
            }
            is CounterFormEvent.GoalChanged -> {
                formStuff.value = formStuff.value.copy(goal = event.goal)
            }
            is CounterFormEvent.IncrementChanged -> {
                formStuff.value = formStuff.value.copy(incrementBy = event.increment)
            }
            is CounterFormEvent.TrackLocationChanged -> {
                formStuff.value = formStuff.value.copy(trackLocation = event.isTracking)
            }
            is CounterFormEvent.Submit -> {
                validateInputs()
            }
        }
    }


    private fun updateCounter() {
        viewModelScope.launch {
            if (wasTrackingLocation && !state.value.form.trackLocation) {
                // were tracking location and now NOT wanting to, update preferences
                preferences.requestingLocationUpdates -= 1 // TODO test because this could potentially set this preference to negative

            } else if (!wasTrackingLocation && state.value.form.trackLocation) {
                // were NOT tracking location and now want to, update preferences
                preferences.requestingLocationUpdates += 1
            }

            editingCounter = editingCounter.copy(
                name = formStuff.value.name,
                count = formStuff.value.startCount,
                goal = formStuff.value.goal,
                increment = formStuff.value.incrementBy,
                track_location = formStuff.value.trackLocation,
            )

            updateCounter.executeSync(UpdateCounter.Params(editingCounter))
//            updateCounter.executeSync(UpdateCounter.Params(state.value.toCounterEntity(counterId)))
        }
    }

    private fun validateInputs() {
        val nameResult = Validator.validateName(formStuff.value.name)
        val countResult = Validator.validateCount(formStuff.value.startCount)
        val incrementResult = Validator.validateIncrement(formStuff.value.incrementBy)
        val goalResult = Validator.validateGoal(formStuff.value.goal)
        val trackResult = Validator.validateTrackLocation(formStuff.value.trackLocation)
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