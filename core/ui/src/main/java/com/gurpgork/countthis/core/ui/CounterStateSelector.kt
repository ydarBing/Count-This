package com.gurpgork.countthis.core.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CounterStateSelector {
    private val selectedIds = MutableStateFlow<Set<Long>>(emptySet())
    private val isSelectionOpen = MutableStateFlow(false)

    fun observeSelectedIds(): StateFlow<Set<Long>> = selectedIds.asStateFlow()

    fun observeIsSelectionOpen(): StateFlow<Boolean> = isSelectionOpen.asStateFlow()

    fun getSelectedIds(): Set<Long> = selectedIds.value

    fun onItemLongClick(id: Long): Boolean {
        if (!isSelectionOpen.value) {
            isSelectionOpen.value = true

            val newSelection = selectedIds.value + id
            isSelectionOpen.value = newSelection.isNotEmpty()
            selectedIds.value = newSelection
            return true
        }
        return false
    }

    fun onItemClick(id: Long): Boolean {
        if (isSelectionOpen.value) {
            val currentSelectedIds = selectedIds.value
            val newSelection = when (id) {
                in currentSelectedIds -> currentSelectedIds - id
                else -> currentSelectedIds + id
            }
            isSelectionOpen.value = newSelection.isNotEmpty()
            selectedIds.value = newSelection
            return true
        }
        return false
    }

    fun clearSelection() {
        selectedIds.value = emptySet()
        isSelectionOpen.value = false
    }
}
