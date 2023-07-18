package com.gurpgork.countthis.core.datastore.preference

import kotlinx.coroutines.flow.Flow

interface CountThisPreferences {

    fun setup()

    var requestingLocationUpdates: Int
    fun observeTrackingLocation(): Flow<Int>

    var theme: Theme
    fun observeTheme(): Flow<Theme>
    var dynamicColors: Boolean
    fun observeDynamicColors(): Flow<Boolean>
    var buttonIncrements: Boolean
    fun observeButtonIncrements(): Flow<Boolean>

    enum class Theme {
        LIGHT,
        DARK,
        SYSTEM
    }

}