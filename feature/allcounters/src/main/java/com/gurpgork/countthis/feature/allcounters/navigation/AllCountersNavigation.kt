package com.gurpgork.countthis.feature.allcounters.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.gurpgork.countthis.feature.allcounters.AllCountersRoute

const val allCountersNavigationRoute = "all_counters_route"

fun NavController.navigateToAllCounters(navOptions: NavOptions? = null) {
    this.navigate(allCountersNavigationRoute, navOptions)
}

fun NavGraphBuilder.allCountersScreen(
//    onCounterClick: (String) -> Unit,
    createNewCounter: () -> Unit,
    editCounter: (counterId: Long, wasTrackingLocation: Boolean) -> Unit,
    openCounter: (counterId: Long) -> Unit,
    openUser: () -> Unit,
) {
    composable(
        route = allCountersNavigationRoute,
//        arguments = listOf(
//            navArgument()
//        ),
    ) {
        AllCountersRoute(createNewCounter, editCounter, openCounter, openUser)
    }
}