package com.gurpgork.countthis.feature.allcounters

import android.location.Location
import androidx.compose.runtime.Immutable
import com.gurpgork.countthis.core.data.model.CounterWithIncrementInfo
import com.gurpgork.countthis.core.designsystem.component.ListItemContextMenuOption
import com.gurpgork.countthis.core.designsystem.component.UiMessage
import com.gurpgork.countthis.core.model.data.SortOption

@Immutable
data class AllCountersViewState(
    val countersWithInfo: List<CounterWithIncrementInfo> = emptyList(),
    val availableSorts: List<SortOption> = emptyList(),
    val availableContextMenuOptions: List<ListItemContextMenuOption> = emptyList(),
    val selectedCounterIds: Set<Long> = emptySet(),
    val sort: SortOption =  SortOption.ALPHABETICAL,
    val sortAsc: Boolean =  false,
    val isSelectionOpen: Boolean = false,
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val message: UiMessage? = null,
    val trackUserLocation: Int = 0,
    val initialPickerLocation: Location = Location(""),
    val locationPickerAddressQuery: String = "",
    val useButtonIncrements: Boolean = false,
    ) {
    companion object {
        val Empty = AllCountersViewState()
    }
}