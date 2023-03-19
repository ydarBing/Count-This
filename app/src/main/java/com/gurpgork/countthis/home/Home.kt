package com.gurpgork.countthis.home

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.gurpgork.countthis.AppNavigation
import com.gurpgork.countthis.debugLabel
import com.gurpgork.countthis.util.Analytics

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class
)
@Composable
internal fun Home(
    analytics: Analytics,
    onOpenSettings: () -> Unit,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberAnimatedNavController(bottomSheetNavigator)

//    val navController = rememberAnimatedNavController()
    val configuration = LocalConfiguration.current
    val useBottomNavigation by remember {
        derivedStateOf { configuration.smallestScreenWidthDp < 600 }
    }
    // Launch an effect to track changes to the current back stack entry, and push them
    // as a screen views to analytics
    LaunchedEffect(navController, analytics) {
        navController.currentBackStackEntryFlow.collect { entry ->
            analytics.trackScreenView(
                label = entry.debugLabel,
                route = entry.destination.route,
                arguments = entry.arguments
            )
        }
    }

//    BottomSheetScaffold(sheetContent = ) {
//
//    }
    Scaffold() {
        Row(Modifier.fillMaxSize()) {
            ModalBottomSheetLayout(bottomSheetNavigator = bottomSheetNavigator) {
                AppNavigation(
                    navController = navController,
                    onOpenSettings = onOpenSettings,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
            }
        }
    }
}