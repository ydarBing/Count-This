package com.gurpgork.countthis.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.gurpgork.countthis.location.Location
import com.gurpgork.countthis.settings.CountThisPreferences

@Composable
fun CountThisPreferences.shouldUseDarkColors(): Boolean {
    val themePreference = observeTheme().collectAsState(initial = CountThisPreferences.Theme.SYSTEM)
    return when (themePreference.value) {
        CountThisPreferences.Theme.LIGHT -> false
        CountThisPreferences.Theme.DARK -> true
        else -> isSystemInDarkTheme()
    }
}


@Composable
fun CountThisPreferences.currentlyTrackingLocation(): Boolean {
    val locationPreference = observeTrackingLocation().collectAsState(initial = false)
    return locationPreference.value
}

/**
 * Returns the `location` object as a human readable string.
 */
fun Location?.toText(): String {
    return if (this != null) {
        toString(latitude, longitude)
    } else {
        "Unknown location"
    }
}

/**
 * Returns the project model `location` object from an Android location object
 */
fun android.location.Location?.toLocation(): Location? {
    return if (this != null) {
        Location(
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