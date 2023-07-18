package com.gurpgork.countthis.feature.editcreate.create.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.gurpgork.countthis.feature.editcreate.create.CreateCounterRoute

const val createCounterNavigationRoute = "create"

fun NavController.navigateToCreteCounter(navOptions: NavOptions? = null) {
    this.navigate(createCounterNavigationRoute, navOptions)
}

fun NavGraphBuilder.CreateCounterScreen(
    navigateUp: () -> Unit,
) {
    composable(
        route = createCounterNavigationRoute,
//        arguments = listOf(
//            navArgument()
//        ),
    ) {
        CreateCounterRoute(navigateUp)
    }
}