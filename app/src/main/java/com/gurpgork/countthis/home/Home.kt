package com.gurpgork.countthis.home

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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.gurpgork.countthis.core.designsystem.component.CtAppBarState
import com.gurpgork.countthis.core.designsystem.component.CtBackground
import com.gurpgork.countthis.core.designsystem.component.CtGradientBackground
import com.gurpgork.countthis.core.designsystem.component.CtTopAppBar
import com.gurpgork.countthis.core.designsystem.theme.GradientColors
import com.gurpgork.countthis.core.designsystem.theme.LocalGradientColors
import com.gurpgork.countthis.feature.settings.SettingsDialog
import com.gurpgork.countthis.navigation.CtNavHost
import com.gurpgork.countthis.navigation.TopLevelDestination

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class
)
@Composable
internal fun Home(
    windowSizeClass: WindowSizeClass,
//    networkMonitor = networkMonitor,
    appState: CtAppState = rememberCtAppState(
        windowSizeClass = windowSizeClass,
//        networkMonitor = networkMonitor,
    )
) {
    val shouldShowGradientBackground =
        appState.currentTopLevelDestination == TopLevelDestination.ALL_COUNTERS
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    CtBackground {
        CtGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            },
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
            var appBarState by remember { mutableStateOf(CtAppBarState()) }

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

            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    Column(Modifier.fillMaxSize()) {
                        CtTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Transparent,
                            ),
                            appBarState = appBarState,
                        )

                        CtNavHost(appState = appState,
                            onShowSnackbar = { message, action ->
                                snackbarHostState.showSnackbar(
                                    message = message,
                                    actionLabel = action,
                                    duration = SnackbarDuration.Short,
                                ) == SnackbarResult.ActionPerformed
                            },
                            onSettingsClicked = { showSettingsDialog = true },
                            onComposing = { appBarState = it }
                        )
                    }
                }
            }
        }
    }
}