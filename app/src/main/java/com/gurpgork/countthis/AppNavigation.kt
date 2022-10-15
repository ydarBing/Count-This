package com.gurpgork.countthis

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.navigation
import com.gurpgork.countthis.ui_account.AccountUI
import com.gurpgork.countthis.ui_counter_details.CounterDetails
import com.gurpgork.countthis.ui_list.CounterList
import com.gurpgork.countthis.ui_create.CreateCounter

internal sealed class Screen(val route: String) {
    object CounterList : Screen("counterlist")
}

private sealed class LeafScreen(
    private val route: String,
) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    object CounterList : LeafScreen("counterlist")
//    object History : LeafScreen("history")
    object Account : LeafScreen("account")
    object CounterDetails : LeafScreen("counter/{counterId}") {
        fun createRoute(root: Screen, counterId: Long): String {
            return "${root.route}/counter/$counterId"
        }
    }
    object CreateCounter : LeafScreen("create")
}

@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.CounterList.route,
        enterTransition = { defaultCountThisEnterTransition(initialState, targetState) },
        exitTransition = { defaultCountThisExitTransition(initialState, targetState) },
        popEnterTransition = { defaultCountThisPopEnterTransition() },
        popExitTransition = { defaultCountThisPopExitTransition() },
        modifier = modifier,
    ) {
        addCounterListTopLevel(navController, onOpenSettings)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCounterListTopLevel(
    navController: NavController,
    openSettings: () -> Unit,
) {
    navigation(
        route = Screen.CounterList.route,
        startDestination = LeafScreen.CounterList.createRoute(Screen.CounterList),
    ) {
        addCounterList(navController, Screen.CounterList)
        addAccount(Screen.CounterList, openSettings)
        addCreateCounter(navController, Screen.CounterList)
        addCounterDetails(navController, Screen.CounterList)
//        addSettings(Screen.Discover, openSettings)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCounterList(
    navController: NavController,
    root: Screen,
) {
    composable(
        route = LeafScreen.CounterList.createRoute(root),
        debugLabel = "CounterList()",
    ) {
        CounterList(
            createNewCounter = {navController.navigate(LeafScreen.CreateCounter.createRoute(root))},
            openCounter = { counterId ->
                navController.navigate(LeafScreen.CounterDetails.createRoute(root, counterId))
            },
            openUser = {
                navController.navigate(LeafScreen.Account.createRoute(root))
            },
        )
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCounterDetails(
    navController: NavController,
    root: Screen,
) {
    composable(
        route = LeafScreen.CounterDetails.createRoute(root),
        debugLabel = "CounterDetails()",
        arguments = listOf(
            navArgument("counterId"){ type = NavType.LongType}
        )
    ) {
        CounterDetails(
            navigateUp = navController::navigateUp,
            openCounterDetails = { counterId ->
                navController.navigate(LeafScreen.CounterDetails.createRoute(root, counterId))
            },
        )
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCreateCounter(
    navController: NavController,
    root: Screen,
) {
    composable(
        route = LeafScreen.CreateCounter.createRoute(root),
        debugLabel = "CreateCounter()",
    ) {
        CreateCounter(
            navigateUp = navController::navigateUp
        )
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addAccount(
    root: Screen,

    onOpenSettings: () -> Unit,
) {
    dialog(
        route = LeafScreen.Account.createRoute(root),
        debugLabel = "AccountUi()",
    ) {
        AccountUI(
            openSettings = onOpenSettings
        )
    }
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultCountThisEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultCountThisExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultCountThisPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultCountThisPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}
