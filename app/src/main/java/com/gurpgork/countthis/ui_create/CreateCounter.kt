package com.gurpgork.countthis.ui_create

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalFocusManager
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

@Composable
fun CreateCounter(
    navigateUp: () -> Unit,
) {
    CreateCounter(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
    )
}


@Composable
internal fun CreateCounter(
    viewModel: CreateCounterViewModel,
    navigateUp: () -> Unit,
) {
    //val viewState by rememberStateWithLifecycle(viewModel.state)

    CreateCounter(
        viewState = viewModel.state.value,//viewState,
        navigateUp = navigateUp,
        confirmCreateCounter = {
            viewModel.addCounter()
            navigateUp()
        }//confirmCreateCounter,
//        logout = { viewModel.logout() }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun CreateCounter(
    viewState: CreateCounterViewState,
    navigateUp: () -> Unit,
    confirmCreateCounter: () -> Unit,
) {
    OptionOne(
        viewState = viewState,
        navigateUp = navigateUp,
        confirmCreateCounter = confirmCreateCounter
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
internal fun OptionOne(
    viewState: CreateCounterViewState,
    navigateUp: () -> Unit,
    confirmCreateCounter: () -> Unit,
) {
    val keyBoardController = LocalSoftwareKeyboardController.current

    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )


    Scaffold(
        topBar = {
            CreateCounterAppBar(
                backgroundColor = Color.Transparent,
                navigateUp = navigateUp,
                elevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
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

                val focusManager = LocalFocusManager.current
                var nameText by remember { mutableStateOf(viewState.name) }
                var goal by remember(viewState.goal) { mutableStateOf(viewState.goal.toString()) }
                var increment by remember(viewState.incrementBy) { mutableStateOf(viewState.incrementBy.toString()) }
                var startCount by remember(viewState.startCount) { mutableStateOf(viewState.startCount.toString()) }

                AppTextField(
                    text = nameText,
                    label = stringResource(R.string.create_counter_name),
                    placeholder = stringResource(R.string.create_counter_name),
                    onChange = {
                        nameText = it
                        viewState.name = it
                    },
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
                    text = startCount,
                    label = stringResource(R.string.create_counter_start_count),
                    placeholder = CounterEntity.EMPTY_COUNTER.count.toString(),
                    modifier = Modifier.focusOrder(second),
                    onChange = { raw ->
                        startCount = raw
                        val parsed = raw.toIntOrNull() ?: CounterEntity.EMPTY_COUNTER.count
                        viewState.startCount = parsed
                        //onChanged(parsed)
                    })
                AppNumberField(
                    text = goal,
                    label = stringResource(R.string.create_counter_goal),
                    placeholder = CounterEntity.EMPTY_COUNTER.goal.toString(),
                    onChange = { raw ->
                        goal = raw
                        val parsed = raw.toIntOrNull() ?: CounterEntity.EMPTY_COUNTER.goal
                        viewState.goal = parsed
                        //onGoalChanged(parsed)
                    })
                AppNumberField(
                    text = increment,
                    label = stringResource(R.string.create_counter_increment),
                    placeholder = CounterEntity.EMPTY_COUNTER.increment.toString(),
                    imeAction = ImeAction.Done,
                    keyBoardActions = KeyboardActions(onDone = { keyBoardController?.hide() }),
                    onChange = { raw ->
                        increment = raw
                        val parsed = raw.toIntOrNull() ?: CounterEntity.EMPTY_COUNTER.increment
                        viewState.incrementBy = parsed
                        //onChanged(parsed)
                    })
                TextButton(
                    // TODO should empty counters be allowed?
                    enabled = nameText.isNotEmpty(),
                    onClick = confirmCreateCounter,
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
fun AppTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String,
    label: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    onChange: (String) -> Unit = {},
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
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
        Row(modifier = Modifier
            .focusable(false)
            .padding(20.dp)
            .fillMaxWidth())
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


@Preview
@Composable
fun AddCounterPreview() {
    CountThisTheme {
        CreateCounter(
            navigateUp = { },
        )
    }
}