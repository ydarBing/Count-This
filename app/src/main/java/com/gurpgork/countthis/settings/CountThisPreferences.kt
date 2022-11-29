package com.gurpgork.countthis.settings

import kotlinx.coroutines.flow.Flow

interface CountThisPreferences {

    fun setup()

    // TODO maybe make this an int and increment/decrement when counters are added/deleted/edited
    // this way we'd know when to fully stop location service
    var requestingLocationUpdates: Int
    fun observeTrackingLocation(): Flow<Int>

    var theme: Theme
    fun observeTheme(): Flow<Theme>

    enum class Theme {
        LIGHT,
        DARK,
        SYSTEM
    }

}