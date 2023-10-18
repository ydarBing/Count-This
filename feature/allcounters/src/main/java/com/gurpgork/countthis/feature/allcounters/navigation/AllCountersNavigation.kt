package com.gurpgork.countthis.feature.allcounters.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.gurpgork.countthis.core.designsystem.component.CtAppBarState
import com.gurpgork.countthis.feature.allcounters.AllCountersRoute

const val allCountersNavigationRoute = "all_counters_route"

fun NavController.navigateToAllCounters(navOptions: NavOptions? = null) {
    this.navigate(allCountersNavigationRoute, navOptions)
}

fun NavGraphBuilder.allCountersScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    addEditCounter: (counterId: Long, listIndex: Int) -> Unit,
    openCounter: (counterId: Long) -> Unit,
    openUser: () -> Unit,
    onSettingsClicked: () -> Unit,
    onComposing: (CtAppBarState) -> Unit,
) {
    composable(route = allCountersNavigationRoute) {
        AllCountersRoute(
            onShowSnackbar = onShowSnackbar,
            addEditCounter = addEditCounter,
            openCounter = openCounter,
            openUser = openUser,
            onSettingsClicked = onSettingsClicked,
            onComposing = onComposing,
        )
    }
}