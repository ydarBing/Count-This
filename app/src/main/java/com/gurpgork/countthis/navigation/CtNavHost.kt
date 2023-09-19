package com.gurpgork.countthis.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.gurpgork.countthis.core.designsystem.component.CtAppBarState
import com.gurpgork.countthis.feature.addedit.navigation.addEditCounterScreen
import com.gurpgork.countthis.feature.addedit.navigation.navigateToAddEditCounter
import com.gurpgork.countthis.feature.allcounters.navigation.allCountersNavigationRoute
import com.gurpgork.countthis.feature.allcounters.navigation.allCountersScreen
import com.gurpgork.countthis.feature.counterdetails.navigation.counterDetailsScreen
import com.gurpgork.countthis.feature.counterdetails.navigation.navigateToCounter
import com.gurpgork.countthis.home.CtAppState


object MainDestinations {
    const val ALL_COUNTERS_ROUTE = "allCounters"
    const val COUNTER_DETAILS_ROUTE = "counter"
    const val COUNTER_DETAIL_ID_KEY = "counterId"
}

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
    onSettingsClicked: () -> Unit,
    onComposing: (CtAppBarState) -> Unit,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        allCountersScreen(
            onShowSnackbar = onShowSnackbar,
            openCounter = navController::navigateToCounter,
            addEditCounter = navController::navigateToAddEditCounter,
            openUser = {},//navController.navigateToUser(),
            onSettingsClicked = onSettingsClicked,
            onComposing = onComposing,
        )
        addEditCounterScreen(navController::navigateUp, onShowSnackbar, onComposing)
        counterDetailsScreen(
            navigateUp = navController::navigateUp,
            openEditCounter = navController::navigateToAddEditCounter,
            onComposing = onComposing,
        )

//        groupedCountersScreen(
//            openGroup = navController::navigateToGroup,
//            openCounter = navController::navigateToCounter,
//            addEditGroup = navController::navigateToAddEditGroup,
//            openUser = {},//navController.navigateToUser(),
//            onSettingsClicked = onSettingsClicked,
//            onComposing = onComposing,
//        )
    }
}
