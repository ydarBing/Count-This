package com.gurpgork.countthis.feature.editcreate.edit.navigation

import androidx.annotation.VisibleForTesting
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gurpgork.countthis.feature.editcreate.edit.EditCounterRoute

@VisibleForTesting
internal const val counterIdArg = "counterId"
internal const val wasTrackingLocationArg = "wasTrackingLocation"

fun NavController.navigateToEditCounter(counterId: Long, wasTrackingLocation: Boolean/*navOptions: NavOptions? = null*/) {
    this.navigate("counter/$counterId/edit?wasTrackingLocation=$wasTrackingLocation"/*, navOptions*/)
    {
        launchSingleTop = true // TODO is this was we want
    }
}

fun NavGraphBuilder.editCounterScreen(
    navigateUp: () -> Unit,
) {
    composable(
        route = "counter/{$counterIdArg}/edit?wasTrackingLocation={$wasTrackingLocationArg}",
        arguments = listOf(
            navArgument(counterIdArg){type = NavType.LongType},
            navArgument(wasTrackingLocationArg){type = NavType.BoolType}
        ),
    ) {
        EditCounterRoute(navigateUp)
    }
}