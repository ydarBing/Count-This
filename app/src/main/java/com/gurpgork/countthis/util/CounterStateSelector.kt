package com.gurpgork.countthis.util

import com.gurpgork.countthis.data.entities.CounterEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CounterStateSelector {
    private val selectedCounterIds = MutableStateFlow<Set<Long>>(emptySet())
    private val isSelectionOpen = MutableStateFlow(false)

    fun observeSelectedCounterIds(): StateFlow<Set<Long>> = selectedCounterIds.asStateFlow()

    fun observeIsSelectionOpen(): StateFlow<Boolean> = isSelectionOpen.asStateFlow()

    fun getSelectedCounterIds(): Set<Long> = selectedCounterIds.value

    fun onItemClick(counter: CounterEntity): Boolean {
        if (isSelectionOpen.value) {
            val selectedIds = selectedCounterIds.value
            val newSelection = when (counter.id) {
                in selectedIds -> selectedIds - counter.id
                else -> selectedIds + counter.id
            }
            isSelectionOpen.value = newSelection.isNotEmpty()
            selectedCounterIds.value = newSelection
            return true
        }
        return false
    }

    fun onItemLongClick(counter: CounterEntity): Boolean {
        if (!isSelectionOpen.value) {
            isSelectionOpen.value = true

            val newSelection = selectedCounterIds.value + counter.id
            isSelectionOpen.value = newSelection.isNotEmpty()
            selectedCounterIds.value = newSelection
            return true
        }
        return false
    }

    fun clearSelection() {
        selectedCounterIds.value = emptySet()
        isSelectionOpen.value = false
    }
}
