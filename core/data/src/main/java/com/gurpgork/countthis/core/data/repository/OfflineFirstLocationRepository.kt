package com.gurpgork.countthis.core.data.repository

import androidx.annotation.WorkerThread
import com.gurpgork.countthis.core.database.dao.LocationDao
import com.gurpgork.countthis.core.database.model.CtLocationEntity
import com.gurpgork.countthis.core.database.model.asExternalModel
import com.gurpgork.countthis.core.model.data.CtLocation
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstLocationRepository @Inject constructor(
    private val locationDao: LocationDao
) : LocationRepository {

    override fun mostRecentLocation() =
        locationDao.getMostRecentLocationEntity()
            .map { it?.asExternalModel() }

    override fun getLocations() =
        locationDao.getLocations()
            .map { it.map( CtLocationEntity::asExternalModel) }

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    override suspend fun updateLocation(ctLocation: CtLocation) {
        locationDao.updateLocation(
            CtLocationEntity(
                accuracy = ctLocation.accuracy,
                longitude = ctLocation.longitude,
                latitude = ctLocation.latitude,
                altitude = ctLocation.altitude,
                time =  ctLocation.time,
            )
        )
    }
}