package com.gurpgork.countthis.home

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.gurpgork.countthis.feature.allcounters.navigation.allCountersNavigationRoute
import com.gurpgork.countthis.feature.allcounters.navigation.navigateToAllCounters
import com.gurpgork.countthis.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberCtAppState(
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
//    networkMonitor: NetworkMonitor,
): CtAppState {

    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
//        networkMonitor,
    ){
        CtAppState(
            navController,
            coroutineScope,
            windowSizeClass,
//            networkMonitor,
        )
    }
}

@Stable
class CtAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
//    networkMonitor: NetworkMonitor
){
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when(currentDestination?.route){
            allCountersNavigationRoute -> TopLevelDestination.ALL_COUNTERS
//            groupsRoute -> TopLevelDestination.GROUPS
            else -> null
        }

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

//    val isOffline = networkMonitor.isOnline
//        .map(Boolean::not)
//        .stateIn(
//            scope = coroutineScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = false,
//        )

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination){
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true

            }

            when (topLevelDestination) {
                TopLevelDestination.ALL_COUNTERS -> navController.navigateToAllCounters(
                    topLevelNavOptions
                )
            }
        }
    }

//    fun navigateToSearch(){
//        navController.navigateToSearch()
//    }
}