package com.gurpgork.countthis.util

///**
// * Returns the `location` object as a human readable string.
// */
//fun Location?.toText(): String {
//    return if (this != null) {
//        toString(latitude, longitude)
//    } else {
//        "Unknown location"
//    }
//}
//
///**
// * Returns the project model `location` object from an Android location object
// */
//fun android.location.Location?.toLocation(): Location? {
//    return if (this != null) {
//        Location(
//            time = time,
//            latitude = latitude,
//            longitude = longitude
//        )
//    } else {
//        return null
//    }
//}
//
///**
// * Returns the `location` object as a human readable string.
// */
//fun android.location.Location?.toText(): String {
//    return if (this != null) {
//        toString(latitude, longitude)
//    } else {
//        "Unknown location"
//    }
//}
//
//fun toString(lat: Double, lon: Double): String {
//    return "($lat, $lon)"
//}

//internal object SharedPreferenceUtil {
//
//    const val KEY_FOREGROUND_ENABLED = "pref_tracking_foreground_location"
//
//    /**
//     * Returns true if requesting location updates, otherwise returns false.
//     *
//     * @param context The [Context].
//     */
//    fun getLocationTrackingPref(context: Context): Boolean =
//        context.getSharedPreferences(
//            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
//            .getBoolean(KEY_FOREGROUND_ENABLED, false)
//
//    /**
//     * Stores the location updates state in SharedPreferences.
//     * @param requestingLocationUpdates The location updates state.
//     */
//    fun saveLocationTrackingPref(context: Context, requestingLocationUpdates: Boolean) =
//        context.getSharedPreferences(
//            context.getString(R.string.preference_file_key),
//            Context.MODE_PRIVATE).edit {
//            putBoolean(KEY_FOREGROUND_ENABLED, requestingLocationUpdates)
//        }
//}
