package com.gurpgork.countthis.home

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.gurpgork.countthis.CountThisActivity
import com.gurpgork.countthis.core.analytics.AnalyticsHelper
import com.gurpgork.countthis.core.analytics.LocalAnalyticsHelper
import com.gurpgork.countthis.core.designsystem.component.ContentViewSetter
import com.gurpgork.countthis.core.designsystem.theme.CtTheme
import com.gurpgork.countthis.core.model.data.DarkThemeConfig
import com.gurpgork.countthis.core.ui.CountThisDateFormatter
import com.gurpgork.countthis.core.ui.LocalCountThisDateFormatter
import com.gurpgork.countthis.location.ForegroundOnlyLocationService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : CountThisActivity() {

    @Inject
    internal lateinit var countThisDateFormatter: CountThisDateFormatter

    //    @Inject
//    internal lateinit var preferences: CountThisPreferences
    @Inject
    internal lateinit var contentViewSetter: ContentViewSetter

    //    @Inject
//    internal lateinit var analytics: Analytics
    @Inject
    internal lateinit var analyticsHelper: AnalyticsHelper

    //    private lateinit var viewModel: MainActivityViewModel
    val viewModel: MainActivityViewModel by viewModels()

    private var foregroundOnlyLocationServiceBound = false

    // TODO setup like preferences are setup
//    @Inject
//    internal lateinit var foregroundOnlyLocationService: ForegroundOnlyLocationService
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null
        set(value) {
            field = value
            // once set, check to see if we should try and get updates
            startLocationUpdates()
        }


    // Monitors connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            //foregroundOnlyLocationService.unbindService(this)
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect()
            }
        }
        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState)

            // Update the dark content of the system bars to match the theme
            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose {}
            }

            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper,
                LocalCountThisDateFormatter provides countThisDateFormatter
            ) {
                CtTheme(
                    darkTheme = darkTheme,
                    dynamicColor = shouldUseDynamicColors(uiState)
                ) {
                    Home(
                        windowSizeClass = calculateWindowSizeClass(this),
//                    analytics = analytics,
//                        onOpenSettings = {
//                            startActivity(
//                                Intent(
//                                    this@MainActivity,
//                                    com.gurpgork.countthis.core.datastore.preference.SettingsActivity::class.java
//                                )
//                            )
//                        },
                    )
                }
            }

        }
//        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
//        val composeView = ComposeView(this).apply {
//            setContent {
//                CountThisContent()
//            }
//        }
//        // Copied from setContent {} ext-fun
//        setOwners()
//        contentViewSetter.setContentView(this, composeView)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }


    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun startLocationUpdates() {
        //TODO is this use of uiState correct
        if (shouldTrackLocation(viewModel.uiState.value) && foregroundPermissionApproved())
            foregroundOnlyLocationService?.subscribeToLocationUpdates()
    }

    private fun stopLocationUpdates() {
        foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
    }


    override fun onStart() {
        super.onStart()

        val serviceIntent = Intent(this, ForegroundOnlyLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)

        // TODO figure out of distinctUntilChanged is necessary
        lifecycleScope.launch {
//            preferences.observeTrackingLocation()
            viewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                /*.distinctUntilChanged()*/.collect {
                    // tracking location updated!
                    if (shouldTrackLocation(it) && foregroundPermissionApproved()) {
                        foregroundOnlyLocationService?.subscribeToLocationUpdates()
                    } else {
                        foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
                    }
                }
        }
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        super.onStop()
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun notificationPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        )
    }

//    @Composable
//    private fun CountThisContent(darkTheme: Boolean) {
//        CompositionLocalProvider(
//            LocalAnalyticsHelper provides analyticsHelper,
//            LocalCountThisDateFormatter provides countThisDateFormatter
//        ) {
//            CountThisTheme(
//                darkTheme = darkTheme,
//                dynamicColor = shouldUseDynamicColors(uiState)
//            ) {
//                Home(
////                    analytics = analytics,
////                    onOpenSettings = {
////                        startActivity(
////                            Intent(
////                                this@MainActivity,
////                                com.gurpgork.countthis.core.datastore.preference.SettingsActivity::class.java
////                            )
////                        )
////                    },
//                )
//            }
//        }
//    }
}

/**
 * Returns `true` if user wants location to be tracked
 * (has 1 or more counters that track location)
 */
private fun shouldTrackLocation(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> uiState.userData.countersTrackingLocation >= 1
}

/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.DARK -> true
        DarkThemeConfig.LIGHT -> false
    }
}

/**
 * Returns `true` if the dynamic color is be used, as a function of the [uiState].
 */
@Composable
private fun shouldUseDynamicColors(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> true
    is MainActivityUiState.Success -> uiState.userData.useDynamicColor
}

private fun ComponentActivity.setOwners() {
    val decorView = window.decorView
    if (decorView.findViewTreeLifecycleOwner() == null) {
        decorView.setViewTreeLifecycleOwner(this)
    }
    if (decorView.findViewTreeViewModelStoreOwner() == null) {
        decorView.setViewTreeViewModelStoreOwner(this)
    }
    if (decorView.findViewTreeSavedStateRegistryOwner() == null) {
        decorView.setViewTreeSavedStateRegistryOwner(this)
    }
}