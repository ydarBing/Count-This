package com.gurpgork.countthis.ui_create

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.gurpgork.countthis.R
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.theme.CountThisTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateCounter(
//    expandedValue: ModalBottomSheetValue,
    navigateUp: () -> Unit,
) {
    CreateCounter(
        viewModel = hiltViewModel(),
//        expandedValue = expandedValue,
        navigateUp = navigateUp,
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun CreateCounter(
    viewModel: CreateCounterViewModel,
//    expandedValue: ModalBottomSheetValue,
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    val viewState = viewModel.state

    // TODO should effect be created inside OptionOne?
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Create counter successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navigateUp()
                }
            }
        }
    }

    CreateCounter(
        viewState = viewState,//viewModel.state.value,
//        expandedValue = expandedValue,
        navigateUp = navigateUp,
//        confirmCreateCounter = {
////            viewModel.onEvent(UIEvent.Submit)
//            viewModel.addCounter()
//            navigateUp()
//        }, //confirmCreateCounter,
        onEvent = { viewModel.onEvent(it) },
//        logout = { viewModel.logout() }
    )
}

@OptIn(
    ExperimentalMaterialApi::class
)
@Composable
internal fun CreateCounter(
    viewState: CreateCounterViewState,
//    expandedValue: ModalBottomSheetValue,
    navigateUp: () -> Unit,
//    confirmCreateCounter: () -> Unit,
    onEvent: (event: CounterFormEvent) -> Unit,
) {

    OptionOne(
        viewState = viewState,
//        expandedValue = expandedValue,
        navigateUp = navigateUp,
//        confirmSubmitCounter = confirmCreateCounter
        counterFormEvent = onEvent,

    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@Composable
internal fun OptionOne(
    viewState: CreateCounterViewState,
//    expandedValue: ModalBottomSheetValue,
    navigateUp: () -> Unit,
    counterFormEvent: (CounterFormEvent) -> Unit,
) {
//    val scaffoldState = rememberScaffoldState()
    val keyBoardController = LocalSoftwareKeyboardController.current

    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

//    viewState.message?.let { message ->
//        LaunchedEffect(message) {
//            scaffoldState.snackbarHostState.showSnackbar(message.message)
//            // Notify the view model that the message has been dismissed
//            onMessageShown(message.id)
//        }
//    }


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
                    CreateCounterAppBar(
                        backgroundColor = Color.Transparent,
                        navigateUp = navigateUp,
                        elevation = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                    )
                }
            }

        }
    ) { contentPadding ->
        Surface(
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding)
            ) {
                // First, get a reference to two focus requesters
                val (first, second) = FocusRequester.createRefs() // using this to "skip" track location from focusing

                CounterInputField(
                    text = viewState.name,
                    onTextChanged = {
                        counterFormEvent(CounterFormEvent.NameChanged(it))
                    },
                    placeholder = stringResource(R.string.create_counter_name),
                    label = stringResource(R.string.create_counter_name),
                    isError = viewState.hasNameError,
                    modifier = Modifier.focusOrder(first) {
                        next = second
                        down = second
                    })


                SwitchWithLocationPermission(
                    permissions,
                    viewState,
                    rationaleMessage = stringResource(R.string.location_permission_rationale_message),
                    deniedMessage = stringResource(R.string.location_permission_denied_message)
                )

                AppNumberField(
                    text = viewState.startCount.toString(),
                    label = stringResource(R.string.create_counter_start_count),
                    placeholder = CounterEntity.EMPTY_COUNTER.count.toString(),
                    modifier = Modifier.focusOrder(second),
                    onChange = { raw ->
                        val parsed = raw.toIntOrNull() ?: CounterEntity.EMPTY_COUNTER.count
                        counterFormEvent(CounterFormEvent.CountChanged(parsed))
                    })
                AppNumberField(
                    text = viewState.goal.toString(),
                    label = stringResource(R.string.create_counter_goal),
                    placeholder = CounterEntity.EMPTY_COUNTER.goal.toString(),
                    onChange = { raw ->
                        val parsed = raw.toIntOrNull() ?: CounterEntity.EMPTY_COUNTER.goal
                        counterFormEvent(CounterFormEvent.GoalChanged(parsed))
                    })
                AppNumberField(
                    text = viewState.incrementBy.toString(),
                    label = stringResource(R.string.create_counter_increment),
                    placeholder = CounterEntity.EMPTY_COUNTER.increment.toString(),
                    imeAction = ImeAction.Done,
                    keyBoardActions = KeyboardActions(onDone = { keyBoardController?.hide() }),
                    onChange = { raw ->
                        val parsed = raw.toIntOrNull() ?: CounterEntity.EMPTY_COUNTER.increment
                        counterFormEvent(CounterFormEvent.IncrementChanged(parsed))
                    })
                TextButton(
                    // TODO should empty counters be allowed?
                    enabled = viewState.name.isNotEmpty(),
                    onClick = { counterFormEvent(CounterFormEvent.Submit) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary,
                    ),
                    modifier = Modifier.align(CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(R.string.create_counter_confirm_create),
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNumberField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String,
    label: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    onChange: (String) -> Unit = {},
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Number,
    keyBoardActions: KeyboardActions = KeyboardActions(),
    isEnabled: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        onValueChange = onChange,
        leadingIcon = leadingIcon,
        textStyle = TextStyle(fontSize = 18.sp),
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        keyboardActions = keyBoardActions,
        enabled = isEnabled,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            disabledBorderColor = Color.Gray,
            disabledTextColor = Color.Black
        ),
        placeholder = {
            Text(text = placeholder, style = TextStyle(fontSize = 18.sp, color = Color.LightGray))
        },
        label = { Text(label) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterInputField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String,
    label: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    onTextChanged: (String) -> Unit = {},
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyBoardActions: KeyboardActions = KeyboardActions(),
    isEnabled: Boolean = true,
    isError: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        onValueChange = onTextChanged,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        textStyle = TextStyle(fontSize = 18.sp),
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        keyboardActions = keyBoardActions,
        enabled = isEnabled,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            disabledBorderColor = Color.Gray,
            disabledTextColor = Color.Black
        ),
        placeholder = {
            Text(text = placeholder, style = TextStyle(fontSize = 18.sp, color = Color.LightGray))
        },
        isError = isError,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateCounterAppBar(
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


@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun SwitchWithLocationPermission(
    permissions: List<String>,
    viewState: CreateCounterViewState,
    deniedMessage: String,
    rationaleMessage: String,
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    HandleRequests(
        multiplePermissionsState = multiplePermissionsState,
        deniedContent = { shouldShowRationale ->
            LocationPermissionDeniedContent(
                viewState = viewState,
                deniedMessage = deniedMessage,
                rationaleMessage = rationaleMessage,
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = { multiplePermissionsState.launchMultiplePermissionRequest() }
            )
        },
        content = { permissionGranted ->
            LocationPermissionSwitchContent(
                viewState = viewState,
                permissionGranted = permissionGranted,
                switchClickCallback = { multiplePermissionsState.launchMultiplePermissionRequest() },
                userDeniedPermission = deniedMessage
            )
        })
}

@ExperimentalPermissionsApi
@Composable
private fun HandleRequests(
    multiplePermissionsState: MultiplePermissionsState,
    deniedContent: @Composable (Boolean) -> Unit,
    content: @Composable (Boolean) -> Unit
) {
    var shouldShowRationale by remember { mutableStateOf(false) }
//    val result = multiplePermissionsState.permissions.all {
//        shouldShowRationale = it.status.shouldShowRationale
//        it.status == PermissionStatus.Granted
//    }
    val locationAccessGranted = multiplePermissionsState.permissions.first().status.isGranted ||
            multiplePermissionsState.permissions.last().status.isGranted

    if (locationAccessGranted) {
        content(true)
    } else {
        deniedContent(shouldShowRationale)
    }
}

@Composable
private fun LocationPermissionSwitchContent(
    viewState: CreateCounterViewState,
    permissionGranted: Boolean,
    switchClickCallback: () -> Unit,
    userDeniedPermission: String
) {
    var trackLocation by remember { mutableStateOf(viewState.trackLocation) }
    var askedPermission by remember { mutableStateOf(false) }

    if (askedPermission && !permissionGranted) {
        Row(
            modifier = Modifier
                .focusable(false)
                .padding(20.dp)
                .fillMaxWidth()
        )
        {
            Text(
                text = userDeniedPermission,
                modifier = Modifier
                    .weight(4F)
                    .align(CenterVertically)
            )
        }
    }


    Row(
        modifier = Modifier
            .focusable(false)
            .padding(20.dp)
            .fillMaxWidth()
    )
    {
        Text(
            text = stringResource(id = R.string.create_counter_track_location),
            modifier = Modifier
                .weight(4F)
                .align(CenterVertically)
        )

        Switch(
            checked = trackLocation,
            onCheckedChange = {
                if (!permissionGranted) {
                    askedPermission = true
                    switchClickCallback()
                } else {
                    trackLocation = it
                    viewState.trackLocation = it
                }
            }
        )
    }
}

@ExperimentalPermissionsApi
@Composable
private fun LocationPermissionDeniedContent(
    viewState: CreateCounterViewState,
    deniedMessage: String,
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {
    if (shouldShowRationale) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Permission Request",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(rationaleMessage)
            },
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text("Give Permission")
                }
            }
        )
    } else {
        LocationPermissionSwitchContent(
            viewState = viewState,
            permissionGranted = false,
            switchClickCallback = onRequestPermission,
            userDeniedPermission = deniedMessage,
        )
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun AddCounterPreview() {
    CountThisTheme {
        CreateCounter(
//            expandedValue = ModalBottomSheetValue.Expanded,
            navigateUp = { },
        )
    }
}