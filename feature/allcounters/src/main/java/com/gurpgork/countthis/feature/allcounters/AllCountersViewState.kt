package com.gurpgork.countthis.feature.allcounters

import androidx.compose.runtime.Immutable
import com.gurpgork.countthis.core.designsystem.component.ListItemContextMenuOption
import com.gurpgork.countthis.core.designsystem.component.UiMessage
import com.gurpgork.countthis.core.model.data.Counter
import com.gurpgork.countthis.core.model.data.CtLocation
import com.gurpgork.countthis.core.model.data.SortOption

@Immutable
data class AllCountersViewState(
    val counters: List<Counter> = emptyList(),
    val availableSorts: List<SortOption> = emptyList(),
    val availableContextMenuOptions: List<ListItemContextMenuOption> = emptyList(),
    val selectedCounterIds: Set<Long> = emptySet(),
    val sort: SortOption =  SortOption.ALPHABETICAL,
    val isSelectionOpen: Boolean = false,
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val message: UiMessage? = null,
    val trackUserLocation: Int = 0,
    val mostRecentLocation: CtLocation? = null,
    val locationPickerAddressQuery: String = "",
    val useButtonIncrements: Boolean = false,
// TODO should all dialog fields be here
    ) {
    companion object {
        val Empty = AllCountersViewState()
    }
}