package com.gurpgork.countthis.ui_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gurpgork.countthis.R
import com.gurpgork.countthis.compose.Layout
import com.gurpgork.countthis.compose.SwipeDismissSnackbarHost
import com.gurpgork.countthis.ui_create.CounterFormEvent
import com.gurpgork.countthis.ui_create.OptionOne
import com.gurpgork.countthis.ui_create.ValidationEvent

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

@Composable
fun EditCounter(
    viewModel: EditCounterViewModel,
//    expandedValue: ModalBottomSheetValue,
    navigateUp: () -> Unit
) {
//    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val viewState by viewModel.state.collectAsState()
//    val viewState = viewModel.state
    val context = LocalContext.current

    // TODO should effect be created inside OptionOne?
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    // TODO move this Toast to snackbar host
//                    Toast.makeText(
//                        context,
//                        "Counter Update Successful",
//                        Toast.LENGTH_LONG
//                    ).show()
                    navigateUp()
                }
            }
        }
    }

    EditCounter(
        viewState = viewState,
//        expandedValue = expandedValue,
        navigateUp = navigateUp,
        onEvent = viewModel::onEvent,
        onMessageShown = viewModel::clearMessage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditCounter(
    viewState: EditCounterViewState,
//    expandedValue: ModalBottomSheetValue,
    navigateUp: () -> Unit,
    onEvent: (CounterFormEvent) -> Unit,
    onMessageShown: (id: Long) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }

    viewState.message?.let { message ->
        LaunchedEffect(message) {
            scaffoldState.snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            onMessageShown(message.id)
        }
    }

    Scaffold(
        topBar = {
            Surface {
                Column {
//                    AnimatedVisibility(visible = expandedValue == ModalBottomSheetValue.Expanded) {
//                        Spacer(
//                            Modifier
//                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.4f))
//                                .windowInsetsTopHeight(WindowInsets.statusBars)
//                                .fillMaxWidth()
//                        )
//                    }
                    EditCounterAppBar(
                        backgroundColor = Color.Transparent,
                        navigateUp = navigateUp,
                        elevation = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                    )
                }
            }

        },
        snackbarHost = {
            SwipeDismissSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(horizontal = Layout.bodyMargin)
                    .fillMaxWidth(),
            )

        }
    ) { contentPadding ->
        // use the same dialog as creating a counter but with the already created counter fields
        // filled out
        OptionOne(
            viewState = viewState.form,
//        expandedValue = expandedValue,
            navigateUp = navigateUp,
            counterFormEvent = onEvent,
            paddingValues = contentPadding,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditCounterAppBar(
    backgroundColor: Color,
    navigateUp: () -> Unit,
    elevation: Dp,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cd_close),
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
        modifier = modifier
    )
}