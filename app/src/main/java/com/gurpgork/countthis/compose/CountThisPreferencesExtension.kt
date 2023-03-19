package com.gurpgork.countthis.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.gurpgork.countthis.location.CTLocation
import com.gurpgork.countthis.settings.CountThisPreferences

@Composable
fun CountThisPreferences.shouldUseDarkColors(): Boolean {
    val themePreference = remember { observeTheme() }.collectAsState(initial = theme)
    return when (themePreference.value) {
        CountThisPreferences.Theme.LIGHT -> false
        CountThisPreferences.Theme.DARK -> true
        else -> isSystemInDarkTheme()
    }
}

@Composable
fun CountThisPreferences.shouldUseDynamicColors(): Boolean {
    return remember { observeDynamicColors() }
        .collectAsState(initial = dynamicColors)
        .value
}

@Composable
fun CountThisPreferences.useButtonIncrements(): Boolean {
    return remember { observeButtonIncrements() }
        .collectAsState(initial = buttonIncrements)
        .value
}


@Composable
fun CountThisPreferences.currentlyTrackingLocation(): Int {
    val locationPreference = observeTrackingLocation().collectAsState(initial = 0)
    return locationPreference.value
}

/**
 * Returns the `location` object as a human readable string.
 */
fun CTLocation?.toText(): String {
    return if (this != null) {
        toString(latitude, longitude)
    } else {
        "Unknown location"
    }
}

/**
 * Returns the project model `location` object from an Android location object
 */
fun android.location.Location?.toLocation(): CTLocation? {
    return if (this != null) {
        CTLocation(
            time = time,
            latitude = latitude,
            longitude = longitude,
            altitude = altitude,
            accuracy = accuracy
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