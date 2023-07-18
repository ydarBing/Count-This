package com.gurpgork.countthis.core.data.repository

import com.gurpgork.countthis.core.model.data.CtLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun mostRecentLocation() : Flow<CtLocation?>

    fun getLocations() : Flow<List<CtLocation>>

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    suspend fun updateLocation(ctLocation: CtLocation)
}