/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gurpgork.countthis.counter

import androidx.compose.runtime.Immutable
import androidx.lifecycle.*
import com.gurpgork.countthis.data.entities.CounterEntity
import kotlinx.coroutines.launch

class CounterViewModel(private val repository: CounterRepository) : ViewModel() {

    // Using LiveData and caching what allCounters returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allCounters: LiveData<List<CounterEntity>> = repository.allCounters.asLiveData()

    private val _userData = MutableLiveData<CounterScreenState>()
    val userData: LiveData<CounterScreenState> = _userData


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(counter: CounterEntity) = viewModelScope.launch {
        repository.insertCounter(counter)
    }
}

@Immutable
data class CounterScreenState(
    val name: String,
    val currentCount: Int,
    val dateCreated: Long,
    val mostRecent: Long,
    val lastLocation: String, // TODO should this be a LatLon
)

class CounterViewModelFactory(private val repository: CounterRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CounterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}