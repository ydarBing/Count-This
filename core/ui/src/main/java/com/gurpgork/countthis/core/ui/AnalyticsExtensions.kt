/*

 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gurpgork.countthis.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.gurpgork.countthis.core.analytics.AnalyticsEvent
import com.gurpgork.countthis.core.analytics.AnalyticsHelper
import com.gurpgork.countthis.core.analytics.LocalAnalyticsHelper

/**
 * Classes and functions associated with analytics events for the UI.
 */
fun AnalyticsHelper.logScreenView(screenName: String) {
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.SCREEN_VIEW,
            extras = listOf(
                AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.SCREEN_NAME, screenName),
            ),
        ),
    )
}

fun AnalyticsHelper.logCounterOpened(counterId: String) {
    logEvent(
        event = AnalyticsEvent(
            type = "counter_opened",
            extras = listOf(
                AnalyticsEvent.Param("opened_counter", counterId),
            ),
        ),
    )
}

/**
 * A side-effect which records a screen view event.
 */
@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
    onDispose {}
}
