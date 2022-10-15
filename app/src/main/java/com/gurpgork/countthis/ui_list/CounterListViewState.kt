package com.gurpgork.countthis.ui_list

import androidx.compose.runtime.Immutable
import com.gurpgork.countthis.compose.UiMessage
import com.gurpgork.countthis.data.ListItemContextMenuOption
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.location.Location
import com.gurpgork.countthis.util.SortOption

@Immutable
data class CounterListViewState(
    val counters: List<CounterEntity> = emptyList(),
    val availableSorts: List<SortOption> = emptyList(),
    val availableContextMenuOptions: List<ListItemContextMenuOption> = emptyList(),
    val selectedCounterIds: Set<Long> = emptySet(),
    val sort: SortOption =  SortOption.DATE_ADDED,
    val isSelectionOpen: Boolean = false,
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val message: UiMessage? = null,
    val trackUserLocation: Boolean = false,
    val mostRecentLocation: Location? = null,
) {
    companion object {
        val Empty = CounterListViewState()
    }
}