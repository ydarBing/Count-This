package com.gurpgork.countthis.feature.counterdetails.navigation

import androidx.annotation.VisibleForTesting
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gurpgork.countthis.feature.counterdetails.CounterDetailsRoute


@VisibleForTesting
internal const val counterIdArg = "counterId"
//internal class CounterDetailsArgs(val counterId: Long){
//    constructor(savedStateHandle: SavedStateHandle):
//            this(savedStateHandle[counterIdArg])
//}

fun NavController.navigateToCounter(counterId: Long){
    this.navigate("counter_route/$counterId"){
        launchSingleTop = true
    }
}

fun NavGraphBuilder.counterDetailsScreen(
    navigateUp: () -> Unit,
    openEditCounter: (Long, Boolean) -> Unit,
    openCounterDetails: (Long) -> Unit,
){
    composable(
        route = "counter_route/{$counterIdArg}",
        arguments = listOf(
            navArgument(counterIdArg){ type = NavType.LongType},
        ),
    ){
        CounterDetailsRoute(
            navigateUp = navigateUp,
            openEditCounter = openEditCounter,
            openCounterDetails = openCounterDetails,
        )
    }
}