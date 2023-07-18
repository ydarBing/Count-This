package com.gurpgork.countthis.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.gurpgork.countthis.core.designsystem.component.CtBackground
import com.gurpgork.countthis.core.designsystem.component.CtGradientBackground
import com.gurpgork.countthis.core.designsystem.component.CtTopAppBar
import com.gurpgork.countthis.core.designsystem.icon.CtIcons
import com.gurpgork.countthis.core.designsystem.theme.GradientColors
import com.gurpgork.countthis.core.designsystem.theme.LocalGradientColors
import com.gurpgork.countthis.feature.settings.SettingsDialog
import com.gurpgork.countthis.navigation.CtNavHost
import com.gurpgork.countthis.navigation.TopLevelDestination
import com.gurpgork.countthis.feature.settings.R as settingsR

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
internal fun Home(
    windowSizeClass: WindowSizeClass, appState: CtAppState = rememberCtAppState(
        windowSizeClass = windowSizeClass,
//        networkMonitor = networkMonitor,
    )
//    analytics: Analytics,
//    onOpenSettings: () -> Unit,
) {
    val shouldShowGradientBackground =
        appState.currentTopLevelDestination == TopLevelDestination.ALL_COUNTERS
    var showSettingsDialog by rememberSaveable {
        mutableStateOf(false)
    }

    CtBackground {
        CtGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            },
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
//            val isOffline by appState.isOffline.collectAsStateWithLifecycle()
//            // If user is not connected to the internet show a snack bar to inform them.
//            val notConnectedMessage = stringResource(R.string.not_connected)
//            LaunchedEffect(isOffline) {
//                if (isOffline) {
//                    snackbarHostState.showSnackbar(
//                        message = notConnectedMessage,
//                        duration = SnackbarDuration.Indefinite,
//                    )
//                }
//            }

            if (showSettingsDialog) {
                SettingsDialog(
                    onDismiss = { showSettingsDialog = false },
                )
            }

//            val unreadDestinations by appState.topLevelDestinationsWithUnreadResources.collectAsStateWithLifecycle()
            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = { SnackbarHost(snackbarHostState) },
//                bottomBar = {
//                    if(appState.shouldShowBottomBar){
//                        CtBottomBar(
//                            destinations = appState.topLevelDestinations,
//                            onNavigateToDestination = appState::navigateToTopLevelDestination,
//                            currentDestination = appState.currentDestination,
//                            modifier = Modifier.testTag("CtBottomBar"),
//                        )
//                    }
//                },

            ) { padding ->
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(padding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal,
                            )
                        )
                ) {
//                    if(appState.shouldShowNavRail){
//                        CtNavRail(
//                            destinations = appState.topLevelDestinations,
//                            onNavigateToDestination = appState::navigateToTopLevelDestination,
//                            currentDestination = appState.currentDestination,
//                            modifier = Modifier.testTag("CtNavRail").safeDrawingPadding(),
//                        )
//                    }

                    Column(Modifier.fillMaxSize()) {
                        val destination = appState.currentTopLevelDestination
                        if (destination != null) {
                            CtTopAppBar(
                                titleRes = destination.titleTextId,
                                actionIcon = CtIcons.Settings,
                                actionIconContentDescription = stringResource(id = settingsR.string.top_app_bar_action_icon_description),
                                onActionClick = { showSettingsDialog = true },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = Color.Transparent,
                                ),
                            )
                        }

                        CtNavHost(appState = appState, onShowSnackbar = { message, action ->
                            snackbarHostState.showSnackbar(
                                message = message,
                                actionLabel = action,
                                duration = SnackbarDuration.Short,
                            ) == SnackbarResult.ActionPerformed
                        })
                    }

                    //OLD layout stuff
//                    ModalBottomSheetLayout(bottomSheetNavigator = bottomSheetNavigator) {
//                        AppNavigation(
//                            navController = navController,
////                    onOpenSettings = onOpenSettings,
//                            modifier = Modifier
//                                .weight(1f)
//                                .fillMaxHeight(),
//                        )
//                    }
                }
            }
        }
    }
}