package com.gurpgork.countthis.ui_edit

import android.widget.Toast
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.gurpgork.countthis.ui_create.CounterFormEvent
import com.gurpgork.countthis.ui_create.OptionOne
import com.gurpgork.countthis.ui_create.ValidationEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditCounter(
//    expandedValue: ModalBottomSheetValue,
    navigateUp: () -> Unit,
) {
    EditCounter(
        viewModel = hiltViewModel(),
//        expandedValue = expandedValue,
        navigateUp = navigateUp,
    )
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalLifecycleComposeApi::class
)
@Composable
fun EditCounter(
    viewModel: EditCounterViewModel,
//    expandedValue: ModalBottomSheetValue,
    navigateUp: () -> Unit
) {
//    val viewState by viewModel.state.collectAsStateWithLifecycle()
//    val viewState by viewModel.state.collectAsState()
    val viewState = viewModel.state
    val context = LocalContext.current

    // TODO should effect be created inside OptionOne?
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Counter Update Successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navigateUp()
                }
            }
        }
    }

    EditCounter(
        viewState = viewState,
//        expandedValue = expandedValue,
        navigateUp = navigateUp,
        onEvent = viewModel::onEvent
    )
}

@OptIn( ExperimentalMaterialApi::class )
@Composable
internal fun EditCounter(
    viewState: EditCounterViewState,
//    expandedValue: ModalBottomSheetValue,
    navigateUp: () -> Unit,
    onEvent: (CounterFormEvent) -> Unit,
) {
    // use the same dialog as creating a counter but with the already created counter fields
    // filled out
    OptionOne(
        viewState = viewState.toCreateCounterViewState(),
//        expandedValue = expandedValue,
        navigateUp = navigateUp,
        counterFormEvent = onEvent,
    )
}
