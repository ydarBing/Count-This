package com.gurpgork.countthis.data.repositories

import androidx.annotation.WorkerThread
import com.gurpgork.countthis.data.daos.LocationDao
import com.gurpgork.countthis.location.Location
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationDao: LocationDao
) {

    fun mostRecentLocation() = locationDao.getMostRecentLocation()

    fun getLocations() = locationDao.getLocations()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateLocation(location: Location) {
        locationDao.updateLocation(location)
    }
}