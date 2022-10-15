package com.gurpgork.countthis

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.*
import androidx.navigation.compose.dialog
import com.google.accompanist.navigation.animation.composable

/**
 * Copy of Navigation Animation `composable()`, but with a [debugLabel] parameter.
 */
@ExperimentalAnimationApi
internal fun NavGraphBuilder.composable(
    route: String,
    debugLabel: String? = null,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = route,
        arguments = when {
            debugLabel != null -> {
                arguments + navArgument(DEBUG_LABEL_ARG) { defaultValue = debugLabel }
            }
            else -> arguments
        },
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        content = content,
    )
}

/**
 * Copy of Navigation-Compose `dialog()`, but with a [debugLabel] parameter.
 */
internal fun NavGraphBuilder.dialog(
    route: String,
    debugLabel: String? = null,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    dialogProperties: DialogProperties = DialogProperties(),
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    dialog(
        route = route,
        arguments = when {
            debugLabel != null -> {
                arguments + navArgument(DEBUG_LABEL_ARG) { defaultValue = debugLabel }
            }
            else -> arguments
        },
        deepLinks = deepLinks,
        dialogProperties = dialogProperties,
        content = content
    )
}

private const val DEBUG_LABEL_ARG = "screen_name"

internal val NavBackStackEntry.debugLabel: String
    get() = arguments?.getString(DEBUG_LABEL_ARG) ?: "Composable"
