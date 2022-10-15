package com.gurpgork.countthis.ui_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.domain.interactors.AddCounter
import com.gurpgork.countthis.settings.CountThisPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
internal class CreateCounterViewModel @Inject constructor(
    private val addCounter: AddCounter,
    private val preferences: CountThisPreferences,
) : ViewModel() {

    var state = mutableStateOf(CreateCounterViewState.Empty)
        private set

    fun addCounter() {
        viewModelScope.launch {
            val now = OffsetDateTime.now()
            val newCounter = CounterEntity(
                track_location = state.value.trackLocation,
                count = state.value.startCount,
                increment = state.value.incrementBy,
                name = state.value.name,
                goal = state.value.goal,
                creation_date_time = now,
//                most_recent_date = now,
//                today_count = state.value.startCount,
                )
            if(state.value.trackLocation)
            {
                // user has accepted location permission and wants to track this counter
                // tell preferences that we should try getting locations
                // getting locations will still have to check for permission
                preferences.requestingLocationUpdates = true
            }
            addCounter(AddCounter.Params(newCounter)).collect()
        }
    }
}