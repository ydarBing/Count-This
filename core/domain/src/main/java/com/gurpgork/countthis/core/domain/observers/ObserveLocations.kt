package com.gurpgork.countthis.core.domain.observers

import com.gurpgork.countthis.core.data.repository.LocationRepository
import com.gurpgork.countthis.core.domain.SubjectInteractor
import com.gurpgork.countthis.core.model.data.CtLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLocations @Inject constructor(
    private val locationRepository: LocationRepository,
) : SubjectInteractor<Unit, CtLocation?>() {

    override fun createObservable(params: Unit): Flow<CtLocation?> {
        return locationRepository.mostRecentLocation()
    }
}