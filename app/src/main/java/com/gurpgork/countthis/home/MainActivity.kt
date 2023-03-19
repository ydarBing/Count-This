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
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.*
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.gurpgork.countthis.CountThisActivity
import com.gurpgork.countthis.compose.ContentViewSetter
import com.gurpgork.countthis.compose.LocalCountThisDateFormatter
import com.gurpgork.countthis.compose.shouldUseDarkColors
import com.gurpgork.countthis.compose.shouldUseDynamicColors
import com.gurpgork.countthis.location.ForegroundOnlyLocationService
import com.gurpgork.countthis.settings.CountThisPreferences
import com.gurpgork.countthis.settings.SettingsActivity
import com.gurpgork.countthis.theme.CountThisTheme
import com.gurpgork.countthis.util.Analytics
import com.gurpgork.countthis.util.CountThisDateFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : CountThisActivity() {
    private lateinit var viewModel: MainActivityViewModel

    @Inject
    internal lateinit var countThisDateFormatter: CountThisDateFormatter
    @Inject
    internal lateinit var preferences: CountThisPreferences

    @Inject
    internal lateinit var contentViewSetter: ContentViewSetter
    @Inject
    internal lateinit var analytics: Analytics

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


    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        val composeView = ComposeView(this).apply {
            setContent {
                CountThisContent()
            }
        }

        // Copied from setContent {} ext-fun
        setOwners()
        contentViewSetter.setContentView(this, composeView)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }


    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun startLocationUpdates(){
        if(preferences.requestingLocationUpdates > 0 &&
            foregroundPermissionApproved())
            foregroundOnlyLocationService?.subscribeToLocationUpdates()
    }

    private fun stopLocationUpdates(){
        foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
    }

    override fun onStart() {
        super.onStart()

        val serviceIntent = Intent(this, ForegroundOnlyLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)

//        if (preferences.requestingLocationUpdates)
//        {
//            if(foregroundPermissionApproved())
//                foregroundOnlyLocationService?.subscribeToLocationUpdates()
//            // if permissions not approved, wait until user tries to increment counter that is
//            // tracking location
//        }
        // TODO figure out of distinctUntilChanged is necessary
        lifecycleScope.launch{
            preferences.observeTrackingLocation().flowWithLifecycle(lifecycle, Lifecycle.State.STARTED )
                /*.distinctUntilChanged()*/.collect{
                    // tracking location updated!
                    if(it > 0)
                    {
                        foregroundOnlyLocationService?.subscribeToLocationUpdates()
                    }
                    else
                    {
                        foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
                    }
                }
        }
    }
    override fun onStop() {
        if(foregroundOnlyLocationServiceBound){
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        super.onStop()
    }

    private fun foregroundPermissionApproved(): Boolean{
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun notificationPermissionApproved(): Boolean{
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        )
    }
    @Composable
    private fun CountThisContent() {
        CompositionLocalProvider(
            LocalCountThisDateFormatter provides countThisDateFormatter
        ) {
            CountThisTheme(
                darkTheme = preferences.shouldUseDarkColors(),
                dynamicColor = preferences.shouldUseDynamicColors()
            ) {
                Home(
                    analytics = analytics,
                    onOpenSettings = {
                        startActivity(
                            Intent(this@MainActivity, SettingsActivity::class.java)
                        )
                    },
                )
            }
        }
    }
}

private fun ComponentActivity.setOwners() {
    val decorView = window.decorView
    if (decorView.findViewTreeLifecycleOwner() == null) {
        decorView.setViewTreeLifecycleOwner(this)
    }
    if(decorView.findViewTreeViewModelStoreOwner() == null){
        decorView.setViewTreeViewModelStoreOwner(this)
    }
    if (decorView.findViewTreeSavedStateRegistryOwner() == null) {
        decorView.setViewTreeSavedStateRegistryOwner(this)
    }
}