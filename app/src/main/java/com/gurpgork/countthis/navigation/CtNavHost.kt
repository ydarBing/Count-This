package com.gurpgork.countthis.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.gurpgork.countthis.feature.allcounters.navigation.allCountersNavigationRoute
import com.gurpgork.countthis.feature.allcounters.navigation.allCountersScreen
import com.gurpgork.countthis.feature.counterdetails.navigation.navigateToCounter
import com.gurpgork.countthis.feature.editcreate.create.navigation.navigateToCreteCounter
import com.gurpgork.countthis.feature.editcreate.edit.navigation.navigateToEditCounter
import com.gurpgork.countthis.home.CtAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun CtNavHost(
    appState: CtAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = allCountersNavigationRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        allCountersScreen(
            openCounter = navController::navigateToCounter,//{counterId -> navController.navigateToCounter(counterId)},
            createNewCounter = navController::navigateToCreteCounter,
            editCounter = { counterId, wasTrackingLocation ->
                navController.navigateToEditCounter(counterId, wasTrackingLocation)},
            openUser = {},//navController.navigateToUser(),
        )
    }
}
