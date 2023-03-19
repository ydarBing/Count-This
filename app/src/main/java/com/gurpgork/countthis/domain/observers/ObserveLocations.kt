package com.gurpgork.countthis.domain.observers

import com.gurpgork.countthis.data.repositories.LocationRepository
import com.gurpgork.countthis.domain.SubjectInteractor
import com.gurpgork.countthis.location.CTLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLocations @Inject constructor(
    private val locationRepository: LocationRepository,
) : SubjectInteractor<Unit, CTLocation?>() {

    override fun createObservable(params: Unit): Flow<CTLocation?> {
        return locationRepository.mostRecentLocation()
    }
}