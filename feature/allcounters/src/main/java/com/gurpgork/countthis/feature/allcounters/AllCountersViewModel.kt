package com.gurpgork.countthis.feature.allcounters

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.CountDownTimer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.gms.maps.model.LatLng
import com.gurpgork.countthis.core.data.model.CounterWithIncrementInfo
import com.gurpgork.countthis.core.data.repository.UserDataRepository
import com.gurpgork.countthis.core.designsystem.component.ListItemContextMenuOption
import com.gurpgork.countthis.core.designsystem.component.UiMessage
import com.gurpgork.countthis.core.designsystem.component.UiMessageManager
import com.gurpgork.countthis.core.designsystem.component.dialog.AddTimeInformation
import com.gurpgork.countthis.core.domain.interactors.AddCounter
import com.gurpgork.countthis.core.domain.interactors.DecrementCounter
import com.gurpgork.countthis.core.domain.interactors.DeleteCounter
import com.gurpgork.countthis.core.domain.interactors.IncrementCounter
import com.gurpgork.countthis.core.domain.interactors.ResetCounter
import com.gurpgork.countthis.core.domain.observers.ObserveLocations
import com.gurpgork.countthis.core.domain.observers.ObservePagedCounterList
import com.gurpgork.countthis.core.extensions.combine
import com.gurpgork.countthis.core.model.data.Counter
import com.gurpgork.countthis.core.model.data.CtLocation
import com.gurpgork.countthis.core.model.data.SortOption
import com.gurpgork.countthis.core.ui.CounterStateSelector
import com.gurpgork.countthis.core.ui.ObservableLoadingCounter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.Locale
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
    private val userDataRepository: UserDataRepository,
//    preferences: CountThisPreferences,
    observeLocations: ObserveLocations,
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()

    private val loadingState = ObservableLoadingCounter()
    private val counterSelection = CounterStateSelector()

    private val trackingLocations: Flow<Int> =
        userDataRepository.userData.map { it.countersTrackingLocation }

//    private val trackingLocationState = preferences.observeTrackingLocation()
    //val useButtonIncrements = preferences.buttonIncrements//preferences.shouldUseButtonIncrements()//preferences.buttonIncrements

    private var timer: CountDownTimer? = null
    private val addressQuery = MutableStateFlow("")
    val pickerLocation = MutableStateFlow(getDefaultLocation())

    private val availableSorts = listOf(
        com.gurpgork.countthis.core.model.data.SortOption.ALPHABETICAL,
        com.gurpgork.countthis.core.model.data.SortOption.DATE_ADDED,
        com.gurpgork.countthis.core.model.data.SortOption.LAST_UPDATED,
        com.gurpgork.countthis.core.model.data.SortOption.USER_SORTED
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

    private val sort =
        MutableStateFlow(SortOption.ALPHABETICAL)

    val state: StateFlow<AllCountersViewState> = combine(
        loadingState.observable,
        counterSelection.observeSelectedCounterIds(),
        counterSelection.observeIsSelectionOpen(),
        sort,
        uiMessageManager.message,
        trackingLocations,
        observeLocations.flow,
        addressQuery,
        userDataRepository.userData.map { it.useButtonIncrements }
    ) { loading, selectedCounterIds, isSelectionOpen, sort, message, userWantsLocationTracked,
        mostRecentLocation, addressQuery, useButtonIncrements ->
        AllCountersViewState(
            isLoading = loading,
            availableContextMenuOptions = availableContextMenuOptions,
            availableSorts = availableSorts,
            selectedCounterIds = selectedCounterIds,
            isSelectionOpen = isSelectionOpen,
            sort = sort,
            message = message,
            trackUserLocation = userWantsLocationTracked,
            mostRecentLocation = mostRecentLocation,
            locationPickerAddressQuery = addressQuery,
            useButtonIncrements = useButtonIncrements
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AllCountersViewState.Empty
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

    fun setSort(sort: com.gurpgork.countthis.core.model.data.SortOption) {
        viewModelScope.launch {
            this@CounterListViewModel.sort.emit(sort)
        }
    }

    fun insertCounter(counter: Counter) {
        viewModelScope.launch {
            addCounter(
                AddCounter.Params(counter)
            ).collect()
        }
    }

    fun addIncrement(
        id: Long,
        location: CtLocation?,
        dateTime: OffsetDateTime,
        timestamp: Instant
    ) {
        viewModelScope.launch {
            incrementCounter(
                IncrementCounter.Params(
                    id,
                    location,
                    dateTime,
                    timestamp
                )
//                IncrementCounter.Params(id, location, OffsetDateTime.now(), Instant.now())
            ).collect()
        }
    }

    fun incrementCounter(id: Long, location: CtLocation?) {
        viewModelScope.launch {
            incrementCounter(
                IncrementCounter.Params(
                    id,
                    location,
                    OffsetDateTime.now(),
                    Instant.now()
                )
            ).collect()
        }
    }

    fun decrementCounter(id: Long, location: CtLocation?) {
        viewModelScope.launch {
            decrementCounter(
                DecrementCounter.Params(
                    id,
                    location,
                    OffsetDateTime.now(),
                    Instant.now()
                )
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

    fun handleContextMenuOptionSelected(
        counterId: Long,
        option: ListItemContextMenuOption,
        addTimeInfo: AddTimeInformation?
    ) {
        when (option) {
            ListItemContextMenuOption.ADD_TIME -> {
                if (addTimeInfo != null) {
                    handleAddTime(counterId, addTimeInfo, pickerLocation.value)
                }
            }

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

    private fun SetPickerLocation(latLng: LatLng?) {
        if (latLng != null) {
            pickerLocation.value.longitude = latLng.longitude
            pickerLocation.value.latitude = latLng.latitude
        }
    }

    fun handleAddTime(
        counterId: Long,
        addTimeInfo: AddTimeInformation,
        pickerLocation: Location?
    ) {
//        val mCalendar: Calendar = GregorianCalendar()
//        val mTimeZone: TimeZone = mCalendar.timeZone
//        val mGMTOffset: Int = mTimeZone.rawOffset
        val odt = OffsetDateTime.now()

        val ldt = LocalDateTime.from(odt)
        addIncrement(
            counterId,
            pickerLocation.toCtLocation(),
            OffsetDateTime.of(addTimeInfo.dateTime, odt.offset),
            Instant.ofEpochSecond(addTimeInfo.dateTime.toEpochSecond(odt.offset))
        )
    }

    fun getAddressFromLocation(context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address>?
        var address: Address?

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
        addressQuery.value = text

        timer?.cancel()
        timer = object : CountDownTimer(1000, 1500) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                pickerLocation.value = getLocationFromAddress(context, text)
            }
        }.start()
    }

    fun getLocationFromAddress(context: Context, strAddress: String): Location {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
        if (Build.VERSION.SDK_INT >= 33) {
            // declare here the geocodeListener, as it requires Android API 33
            val geocoderListener = Geocoder.GeocodeListener { receivedAddresses ->
                // TODO set location picker location?
                val loc = Location("")
                loc.latitude = receivedAddresses[0].latitude
                loc.longitude = receivedAddresses[0].longitude
            }
            geocoder.getFromLocationName(strAddress, 1, geocoderListener)

        } else {
            val address: Address?
            addresses = geocoder.getFromLocationName(strAddress, 1)
            if (!addresses.isNullOrEmpty()) {
                address = addresses[0]
                val loc = Location("")
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

    fun getDefaultLocation(): Location {
        val defaultLocation = Location("")
//        val defaultLocation = CtLocation()

        defaultLocation.latitude = 43.40477213923839
        defaultLocation.longitude = -113.52072556208147

        return defaultLocation
    }

    fun updatePickerLocation(context: Context, pos: LatLng) {
        updatePickerLocation(context, pos.latitude, pos.longitude)
    }

    private fun updatePickerLocation(context: Context, latitude: Double, longitude: Double) {
        if (latitude != pickerLocation.value.latitude) {
            val location = Location("")
            location.latitude = latitude
            location.longitude = longitude
            setPickerLocation(location)
            getAddressFromLocation(context)
        }
    }

    private fun setPickerLocation(loc: Location) {
        pickerLocation.value = loc
    }

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 30,
            initialLoadSize = 30
        )
    }
}

/**
 * Returns the project model `location` object from an Android location object
 */
fun android.location.Location?.toCtLocation(): CtLocation? {
    return if (this != null) {
        CtLocation(
            time = time,
            latitude = latitude,
            longitude = longitude,
            altitude = altitude,
            accuracy = accuracy,
            id = 0
        )
    } else {
        return null
    }
}

/**
 * Returns the `location` object as a human readable string.
 */
fun android.location.Location?.toText(): String {
    return if (this != null) {
        toString(latitude, longitude)
    } else {
        "Unknown location"
    }
}

fun toString(lat: Double, lon: Double): String {
    return "($lat, $lon)"
}
