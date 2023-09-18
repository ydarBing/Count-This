package com.gurpgork.countthis.feature.addedit.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gurpgork.countthis.core.designsystem.component.CtAppBarState
import com.gurpgork.countthis.core.model.data.CREATE_COUNTER_ID
import com.gurpgork.countthis.core.model.data.INVALID_COUNTER_ID
import com.gurpgork.countthis.feature.addedit.AddEditCounterRoute

@VisibleForTesting
internal const val counterIdArg = "counterId"
//const val addEditCounterNavigationRoute = "add_edit_counter?counterId=${counterIdArg}"

internal class AddEditArgs(val counterId: Long) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(savedStateHandle[counterIdArg] ?: INVALID_COUNTER_ID)
}

fun NavController.navigateToAddEditCounter(counterId: Long) {
    this.navigate(
        "add_edit_counter?counterId=${counterId}"
    ) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.addEditCounterScreen(
    navigateUp: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onComposing: (CtAppBarState) -> Unit,
) {
    composable(
        route = "add_edit_counter?counterId={$counterIdArg}",
        arguments = listOf(
            navArgument(counterIdArg) {
                type = NavType.LongType
                defaultValue = CREATE_COUNTER_ID//INVALID_COUNTER_ID
            },

            ),
    ) {
        AddEditCounterRoute(navigateUp, onShowSnackbar, onComposing)
    }
}