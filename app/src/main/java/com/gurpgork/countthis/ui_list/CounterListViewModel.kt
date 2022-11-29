package com.gurpgork.countthis.ui_list

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.CountDownTimer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gurpgork.countthis.compose.UiMessage
import com.gurpgork.countthis.compose.UiMessageManager
import com.gurpgork.countthis.compose.dialog.AddTimeInformation
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
import java.util.*
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
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()

    private val loadingState = ObservableLoadingCounter()
    private val counterSelection = CounterStateSelector()

    private val trackingLocationState = preferences.observeTrackingLocation()

    private var timer: CountDownTimer? = null
    private val addressQuery = MutableStateFlow("")
    val pickerLocation = MutableStateFlow(getInitialLocation())

    private val availableSorts = listOf(
        SortOption.ALPHABETICAL,
        SortOption.DATE_ADDED,
        SortOption.LAST_UPDATED,
        SortOption.USER_SORTED
    )

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

    val state: StateFlow<CounterListViewState> = combine(
        loadingState.observable,
        counterSelection.observeSelectedCounterIds(),
        counterSelection.observeIsSelectionOpen(),
        sort,
        uiMessageManager.message,
        trackingLocationState,
        observeLocations.flow,
        addressQuery
    ) { loading, selectedCounterIds, isSelectionOpen, sort, message, userWantsLocationTracked,
        mostRecentLocation, addressQuery ->
        CounterListViewState(
            isLoading = loading,
            availableContextMenuOptions = availableContextMenuOptions,
            availableSorts = availableSorts,
            selectedCounterIds = selectedCounterIds,
            isSelectionOpen = isSelectionOpen,
            sort = sort,
            message = message,
            trackUserLocation = userWantsLocationTracked,
            mostRecentLocation = mostRecentLocation,
            locationPickerAddressQuery = addressQuery
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CounterListViewState.Empty
    )


    init {
        observePagedCounterList(
            ObservePagedCounterList.Params(
                sort = sort.value,
                pagingConfig = PAGING_CONFIG
            )
        )

        observeLocations(Unit)

        // When the sort option change, update the data source
        viewModelScope.launch {
            sort.collect { updateDatasource() }
        }

        viewModelScope.launch {
            addressQuery.debounce(300)
                .onEach { query ->
                    val job = launch {
//                        loadingState.addLoader()
                        //TODO search geocoder
                    }
//                    job.invokeOnCompletion { loadingState.removeLoader() }
                    job.join()
                }
                .catch { throwable -> uiMessageManager.emitMessage(UiMessage(throwable)) }
                .collect()

        }
    }

    private fun updateDatasource() {
        observePagedCounterList(
            ObservePagedCounterList.Params(
                sort = sort.value,
                pagingConfig = PAGING_CONFIG
            )
        )
    }

    fun setSort(sort: SortOption) {
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

    fun handleContextMenuOptionSelected(counterId: Long, option: ListItemContextMenuOption, addTimeInfo: AddTimeInformation?) {
        when (option) {
            ListItemContextMenuOption.ADD_TIME -> { handleAddTime(counterId, addTimeInfo) }
            ListItemContextMenuOption.MOVE -> TODO()//{ this@CounterListViewModel. }
            ListItemContextMenuOption.EDIT -> TODO()
            ListItemContextMenuOption.RESET -> {
                resetCounter(counterId)
            }
            ListItemContextMenuOption.DELETE -> {
                removeCounter(counterId)
            }
            ListItemContextMenuOption.NONE -> assert(false)
        }
    }

    fun handleAddTime(counterId: Long, addTimeInfo: AddTimeInformation?) {
        TODO()
    }

//    fun currentUserGeoCoord(latLng: LatLng) {
//        userCurrentLat.value = latLng.latitude
//        userCurrentLng.value = latLng.longitude
//        userCurrentAccuracy.value = 0.0f
//        userCurrentAlt.value = 0.0
//    }

//    fun currentUserGeoCoord(loc: Location) {
//        userCurrentLat.value = loc.latitude
//        userCurrentLng.value = loc.longitude
//        userCurrentAccuracy.value = loc.accuracy
//        userCurrentAlt.value = loc.altitude
//    }

    fun getAddressFromLocation(context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address>? = null
        var address: Address?
//        var addressText = ""

        try {
            if (Build.VERSION.SDK_INT >= 33) {
                // declare here the geocodeListener, as it requires Android API 33
                val geocoderListener = Geocoder.GeocodeListener { receivedAddresses ->
                    // do something with the address list
                    address = receivedAddresses[0]
                    addressQuery.value = address?.getAddressLine(0) ?: ""
                }
                geocoder.getFromLocation(
                    pickerLocation.value.latitude,
                    pickerLocation.value.longitude,
                    1,
                    geocoderListener
                )
            } else {
                addresses = geocoder.getFromLocation(
                    pickerLocation.value.latitude,
                    pickerLocation.value.longitude,
                    1
                )

                address = addresses?.get(0)
                addressQuery.value = address?.getAddressLine(0) ?: ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addressQuery.value
    }

    fun onTextChanged(context: Context, text: String) {
        if (text.isEmpty())
            return
        timer?.cancel()
        timer = object : CountDownTimer(1000, 1500) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                pickerLocation.value = getLocationFromAddress(context, text)
            }
        }.start()
    }

    fun getLocationFromAddress(context: Context, strAddress: String): android.location.Location {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?

        if (Build.VERSION.SDK_INT >= 33) {
            // declare here the geocodeListener, as it requires Android API 33
            val geocoderListener = Geocoder.GeocodeListener { receivedAddresses ->
                val loc = android.location.Location("")
                loc.latitude = receivedAddresses[0].latitude
                loc.longitude = receivedAddresses[0].longitude
            }
            geocoder.getFromLocationName(strAddress, 1, geocoderListener)

        } else {
            val address: Address?
            addresses = geocoder.getFromLocationName(strAddress, 1)
            if (!addresses.isNullOrEmpty()) {
                address = addresses[0]
                val loc = android.location.Location("")
                loc.latitude = address.latitude
                loc.longitude = address.longitude
                return loc
            }
        }
        return pickerLocation.value
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    private fun getInitialLocation(): android.location.Location {
        val initialLocation = android.location.Location("")
        initialLocation.latitude = 43.40477213923839
        initialLocation.longitude = -113.52072556208147
        return initialLocation
    }

    fun updatePickerLocation(latitude: Double, longitude: Double) {
        if (latitude != pickerLocation.value.latitude) {
            val location = android.location.Location("")
            location.latitude = latitude
            location.longitude = longitude
            setPickerLocation(location)
        }
    }

    fun setPickerLocation(loc: android.location.Location) {
        pickerLocation.value = loc
    }

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 30,
            initialLoadSize = 30
        )
    }
}
