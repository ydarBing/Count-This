package com.gurpgork.countthis.core.data.repository

import com.gurpgork.countthis.core.analytics.AnalyticsEvent
import com.gurpgork.countthis.core.analytics.AnalyticsEvent.Param
import com.gurpgork.countthis.core.analytics.AnalyticsHelper


fun AnalyticsHelper.logIncrementCountersTrackingLocation() {
    logEvent(
        AnalyticsEvent(type = "counters_tracking_location_incremented"),
    )
}

fun AnalyticsHelper.logDecrementCountersTrackingLocation() {
    logEvent(
        AnalyticsEvent(type = "counters_tracking_location_decremented"),
    )
}

fun AnalyticsHelper.logSetUseButtonIncrementsChanged(useButtonIncrements: Boolean) =
    logEvent(
        AnalyticsEvent(
            type = "increment_option_changed",
            extras = listOf(
                Param(key = "increment_option", value = useButtonIncrements.toString()),
            ),
        ),
    )

fun AnalyticsHelper.logDarkThemeConfigChanged(darkThemeConfigName: String) =
    logEvent(
        AnalyticsEvent(
            type = "dark_theme_config_changed",
            extras = listOf(
                Param(key = "dark_theme_config", value = darkThemeConfigName),
            ),
        ),
    )

fun AnalyticsHelper.logDynamicColorPreferenceChanged(useDynamicColor: Boolean) =
    logEvent(
        AnalyticsEvent(
            type = "dynamic_color_preference_changed",
            extras = listOf(
                Param(
                    key = "dynamic_color_preference",
                    value = useDynamicColor.toString()
                ),
            ),
        ),
    )

fun AnalyticsHelper.logOnboardingStateChanged(shouldHideOnboarding: Boolean) {
    val eventType = if (shouldHideOnboarding) "onboarding_complete" else "onboarding_reset"
    logEvent(
        AnalyticsEvent(type = eventType),
    )
}
