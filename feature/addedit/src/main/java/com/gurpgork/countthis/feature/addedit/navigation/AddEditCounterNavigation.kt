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
import com.gurpgork.countthis.core.model.data.INVALID_LIST_INDEX
import com.gurpgork.countthis.feature.addedit.AddEditCounterRoute

@VisibleForTesting
internal const val counterIdArg = "counterId"
// If creating counter, list index will be the number of counters already created(put at bottom of list)
// if editing counter, list index will be current index
internal const val listIndexArg = "listIndex"

internal class AddEditArgs(val counterId: Long, val numCounters: Int) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                savedStateHandle[counterIdArg] ?: INVALID_COUNTER_ID,
                savedStateHandle[listIndexArg] ?: INVALID_LIST_INDEX
                )
}

fun NavController.navigateToAddEditCounter(counterId: Long, listIndex: Int) {
    this.navigate(
        "add_edit_counter?counterId=${counterId}&listIndex=${listIndex}"
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
        route = "add_edit_counter?counterId={$counterIdArg}&listIndex={$listIndexArg}",
        arguments = listOf(
            navArgument(counterIdArg) {
                type = NavType.LongType
                defaultValue = CREATE_COUNTER_ID//INVALID_COUNTER_ID
            },
            navArgument(listIndexArg) {
                type = NavType.IntType
                defaultValue = INVALID_LIST_INDEX
            }
        ),
    ) {
        AddEditCounterRoute(navigateUp, onShowSnackbar, onComposing)
    }
}