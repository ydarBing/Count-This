package com.gurpgork.countthis.core.data.repository

import com.gurpgork.countthis.core.model.data.DarkThemeConfig
import com.gurpgork.countthis.core.model.data.SortOption
import com.gurpgork.countthis.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Increment location tracking by 1.  If above 0, at least one counter
     * is tracking location so location updates are necessary
     */
    suspend fun incrementCountersTrackingLocation()

    /**
     * Decrement location tracking by 1.  If above 0, at least one counter
     * is tracking location so location updates are necessary
     * If already at 0, number not updated
     */
    suspend fun decrementCountersTrackingLocation()

    /**
     * Toggles the user's preference for using button increments vs swiping.
     */
    suspend fun setUseButtonIncrements(useButtonIncrements: Boolean)

    /**
     * Sets the desired dark theme config.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    /**
     * Sets the preferred dynamic color config.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    /**
     * Sets whether the user has completed the onboarding process.
     */
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)
    /**
     * Sets how the main screen counters should be sorted
     */
    suspend fun setSort(sort: SortOption)
    /**
     * Sets how the main screen counters should be ordered, ascending or descending
     */
    suspend fun setSortAsc(sortAsc: Boolean)
    /**
     * Sets how the main screen counters should be ordered, ascending or descending
     */
    suspend fun toggleSortAsc()
    /**
     * Sets if firebase analytics should be sent
     */
    suspend fun setCrashAnalyticsPreference(enableAnalytics: Boolean)

}