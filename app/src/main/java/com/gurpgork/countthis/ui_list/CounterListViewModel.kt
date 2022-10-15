package com.gurpgork.countthis.ui_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.gms.maps.model.LatLng
import com.gurpgork.countthis.compose.UiMessageManager
import com.gurpgork.countthis.data.ListItemContextMenuOption
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.data.resultentities.CounterWithIncrementInfo
import com.gurpgork.countthis.domain.interactors.*
import com.gurpgork.countthis.domain.observers.ObserveLocations
import com.gurpgork.countthis.domain.observers.ObservePagedCounterList
import com.gurpgork.countthis.extensions.combine
import com.gurpgork.countthis.location.Location
import com.gurpgork.countthis.settings.CountThisPreferences
import com.gurpgork.countthis.util.CounterStateSelector
import com.gurpgork.countthis.util.ObservableLoadingCounter
import com.gurpgork.countthis.util.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class CounterListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val addCounter: AddCounter,
    private val deleteCounter: DeleteCounter,
    private val resetCounter: ResetCounter,
    private val incrementCounter: IncrementCounter,
    private val decrementCounter: DecrementCounter,
    private val observePagedCounterList: ObservePagedCounterList,

    observeLocations: ObserveLocations,
    preferences: CountThisPreferences,
    //private val updateCounter: UpdateCounter
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()

    private val loadingState = ObservableLoadingCounter()
    private val counterSelection = CounterStateSelector()

    private val trackingLocationState = preferences.observeTrackingLocation()

    val pendingDelete: Boolean = false
    val pendingReset: Boolean = false
    val pendingMove: Boolean = false

    private val availableSorts = listOf(
        SortOption.ALPHABETICAL,
        SortOption.DATE_ADDED,
        SortOption.LAST_UPDATED,
        SortOption.USER_SORTED)

    private val availableContextMenuOptions = listOf(
        ListItemContextMenuOption.ADD_TIME,
        ListItemContextMenuOption.MOVE,
        ListItemContextMenuOption.EDIT,
        ListItemContextMenuOption.RESET,
        ListItemContextMenuOption.DELETE,
    )


    val pagedList: Flow<PagingData<CounterWithIncrementInfo>> =
        observePagedCounterList.flow.cachedIn(viewModelScope)

    private val sort = MutableStateFlow(SortOption.DATE_ADDED)

    private var userCurrentLng = MutableStateFlow(0.0)
    private var userCurrentLat = MutableStateFlow(0.0)
    private var userCurrentAlt = MutableStateFlow(0.0)
    private var userCurrentAccuracy = MutableStateFlow(0.0f)

    val state: StateFlow<CounterListViewState> = combine(
        loadingState.observable,
        counterSelection.observeSelectedCounterIds(),
        counterSelection.observeIsSelectionOpen(),
        sort,
        uiMessageManager.message,
        trackingLocationState,
        observeLocations.flow
    ){ loading, selectedCounterIds, isSelectionOpen, sort, message, userWantsLocationTracked,
        mostRecentLocation ->
        CounterListViewState(
            isLoading = loading,
            availableContextMenuOptions = availableContextMenuOptions,
            availableSorts = availableSorts,
            selectedCounterIds = selectedCounterIds,
            isSelectionOpen = isSelectionOpen,
            sort = sort,
            message = message,
            trackUserLocation = userWantsLocationTracked,
            mostRecentLocation = mostRecentLocation
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CounterListViewState.Empty
    )


    init {
        observePagedCounterList(ObservePagedCounterList.Params(sort = sort.value,
            pagingConfig = PAGING_CONFIG))

        observeLocations(Unit)

        // When the sort option change, update the data source
        viewModelScope.launch {
            sort.collect { updateDatasource() }
        }
    }

    private fun updateDatasource(){
        observePagedCounterList(ObservePagedCounterList.Params(
            sort = sort.value,
            pagingConfig = PAGING_CONFIG
        ))
    }

    fun setSort(sort: SortOption){
        viewModelScope.launch {
            this@CounterListViewModel.sort.emit(sort)
        }
    }

    fun insertCounter(counter: CounterEntity) {
        viewModelScope.launch {
            addCounter(
                AddCounter.Params(counter)
            ).collect()
        }
    }

    fun incrementCounter(id: Long, location: Location?) {
        viewModelScope.launch {
            incrementCounter(
                IncrementCounter.Params(id, location, OffsetDateTime.now(), Instant.now())
            ).collect()
        }
    }

    fun decrementCounter(id: Long, location: Location?) {
        viewModelScope.launch {
            decrementCounter(
                DecrementCounter.Params(id, location, OffsetDateTime.now(), Instant.now())
            ).collect()
        }
    }

    private fun resetCounter(id: Long) {
        viewModelScope.launch {
            resetCounter(
                ResetCounter.Params(id)
            ).collect()
        }
    }
    private fun removeCounter(id: Long) {
        viewModelScope.launch {
            deleteCounter(
                DeleteCounter.Params(id)
            ).collect()
        }
    }

    fun handleContextMenuOptionSelected(counterId: Long, option: ListItemContextMenuOption){
        when(option){
            ListItemContextMenuOption.ADD_TIME -> TODO()//{this@CounterListViewModel.}
            ListItemContextMenuOption.MOVE -> TODO()//{ this@CounterListViewModel. }
            ListItemContextMenuOption.EDIT -> TODO()
            ListItemContextMenuOption.RESET -> { resetCounter(counterId) }
            ListItemContextMenuOption.DELETE -> { removeCounter(counterId) }
        }
    }

    fun currentUserGeoCoord(latLng: LatLng){
        userCurrentLat.value = latLng.latitude
        userCurrentLng.value = latLng.longitude
//        userCurrentAccuracy.value = 0.0f
//        userCurrentAlt.value = 0.0
    }

    fun currentUserGeoCoord(loc: Location){
        userCurrentLat.value = loc.latitude
        userCurrentLng.value = loc.longitude
        userCurrentAccuracy.value = loc.accuracy
        userCurrentAlt.value = loc.altitude
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }


    companion object{
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 30,
            initialLoadSize = 30
        )
    }
}
