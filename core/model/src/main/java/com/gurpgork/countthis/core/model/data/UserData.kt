package com.gurpgork.countthis.core.model.data

data class UserData (
    val countersTrackingLocation: Int,
    val useButtonIncrements: Boolean,
//    val incrementOptionConfig: IncrementOptionConfig,
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val shouldHideOnboarding: Boolean,
    val currentSort: SortOption,
    val sortAsc: Boolean,
    val shouldDisableCrashAnalytics: Boolean,
)