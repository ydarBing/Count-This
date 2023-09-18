package com.gurpgork.countthis.feature.addedit

import android.Manifest
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusProperties
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.gurpgork.countthis.core.designsystem.component.CtAppBarState
import com.gurpgork.countthis.core.model.data.CREATE_COUNTER_ID

@Composable
fun AddEditCounterRoute(
    navigateUp: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onComposing: (CtAppBarState) -> Unit,
) {
    AddEditCounterScreen(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
        onShowSnackbar,
        onComposing,
    )
}

@Composable
fun AddEditCounterScreen(
    viewModel: AddEditCounterViewModel,
    navigateUp: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onComposing: (CtAppBarState) -> Unit,
) {
    val formState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // TODO should effect be created inside AddEditForm?
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    onShowSnackbar("Counter Edited!", null)
                    navigateUp()
                }
            }
        }
    }
    LaunchedEffect(key1 = true) {
        val title = if(viewModel.currentCounterId == CREATE_COUNTER_ID) "Create" else "Edit"

        onComposing(addEditCounterAppBar(title, navigateUp))
    }
    EditCounterScreenViaFormState(
        formState = formState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
internal fun EditCounterScreenViaFormState(
    formState: CounterFormViewState,
    onEvent: (CounterFormEvent) -> Unit,
) {
    AddEditForm(
        viewState = formState,
        counterFormEvent = onEvent,
    )
}


private fun addEditCounterAppBar(
    title: String,
    navigateUp: () -> Unit,
): CtAppBarState = CtAppBarState(title = title, navigationIcon = {
    IconButton(onClick = navigateUp) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.cd_close),
        )
    }
}, actions = {
//        IconButton(onClick = { onSettingsClicked() }) {
//            Icon(
//                imageVector = CtIcons.Settings,
//                contentDescription = "Settings Icon",
//                tint = MaterialTheme.colorScheme.onSurface
//            )
//        }
})


@OptIn(
    ExperimentalComposeUiApi::class,
)
@Composable
internal fun AddEditForm(
    viewState: CounterFormViewState,
    counterFormEvent: (CounterFormEvent) -> Unit,
) {
    val keyBoardController = LocalSoftwareKeyboardController.current

    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    Surface(
        tonalElevation = 2.dp, modifier = Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
//                .imePadding(),
        ) {
            // First, get a reference to two focus requesters
            val (first, second) = FocusRequester.createRefs() // using this to "skip" track location from focusing

            CounterInputField(label = stringResource(R.string.create_counter_name),
                placeholder = viewState.namePlaceholder,
                text = viewState.name,
                onTextChanged = {
                    counterFormEvent(CounterFormEvent.NameChanged(it))
                },
                isError = viewState.hasNameError,
                modifier = Modifier.focusRequester(first).focusProperties(fun FocusProperties.() {
                    next = second
                    down = second
                })
            )


            SwitchWithLocationPermission(
                permissions,
                viewState,
                rationaleMessage = stringResource(R.string.location_permission_rationale_message),
                deniedMessage = stringResource(R.string.location_permission_denied_message)
            )

            AppNumberField(label = stringResource(R.string.create_counter_start_count),
                text = viewState.startCount,
                placeholder = viewState.startCountPlaceholder,
                modifier = Modifier.focusRequester(second),
                onChange = { raw ->
                    counterFormEvent(CounterFormEvent.CountChanged(raw))
                })
            AppNumberField(label = stringResource(R.string.create_counter_goal),

                text = viewState.goal,
                placeholder = viewState.goalPlaceholder,
                onChange = { raw ->
                    counterFormEvent(CounterFormEvent.GoalChanged(raw))
                })
            AppNumberField(label = stringResource(R.string.create_counter_increment),
                text = viewState.incrementBy,
                placeholder = viewState.incrementByPlaceholder,
                imeAction = ImeAction.Done,
                keyBoardActions = KeyboardActions(onDone = { keyBoardController?.hide() }),
                onChange = { raw ->
                    counterFormEvent(CounterFormEvent.IncrementChanged(raw))
                })
            TextButton(
                enabled = viewState.name.isNotEmpty(),
                onClick = { counterFormEvent(CounterFormEvent.Submit) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary,
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.create_counter_confirm_create),
                    style = TextStyle(fontSize = 20.sp)
                )
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
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            disabledBorderColor = Color.Gray,
        ),
        placeholder = {
            Text(text = placeholder, style = TextStyle(fontSize = 18.sp, color = Color.LightGray))
        },
        isError = isError,
    )
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun SwitchWithLocationPermission(
    permissions: List<String>,
    viewState: CounterFormViewState,
    deniedMessage: String,
    rationaleMessage: String,
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    HandleRequests(multiplePermissionsState = multiplePermissionsState,
        deniedContent = { shouldShowRationale ->
            LocationPermissionDeniedContent(viewState = viewState,
                deniedMessage = deniedMessage,
                rationaleMessage = rationaleMessage,
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = { multiplePermissionsState.launchMultiplePermissionRequest() })
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
    val locationAccessGranted =
        multiplePermissionsState.permissions.first().status.isGranted || multiplePermissionsState.permissions.last().status.isGranted

    if (locationAccessGranted) {
        content(true)
    } else {
        deniedContent(shouldShowRationale)
    }
}

@Composable
private fun LocationPermissionSwitchContent(
    viewState: CounterFormViewState,
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
        ) {
            Text(
                text = userDeniedPermission,
                modifier = Modifier
                    .weight(4F)
                    .align(Alignment.CenterVertically)
            )
        }
    }


    Row(
        modifier = Modifier
            .focusable(false)
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.create_counter_track_location),
            modifier = Modifier
                .weight(4F)
                .align(Alignment.CenterVertically)
        )

        Switch(checked = trackLocation && permissionGranted, onCheckedChange = {
            if (!permissionGranted) {
                askedPermission = true
                switchClickCallback()
//                    viewState.trackLocation = it // doing this gives undesired result,  user can
//                                       deny permission but will add counter with location tracking
                // TODO simplify/test the location permission activation procedure/cycle to ensure proper handling of it.
                //      i.e user removes location permission from app info
                trackLocation = it
            } else {
                trackLocation = it
                viewState.trackLocation = it
            }
        })
    }
}

@ExperimentalPermissionsApi
@Composable
private fun LocationPermissionDeniedContent(
    viewState: CounterFormViewState,
    deniedMessage: String,
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {
    if (shouldShowRationale) {
        AlertDialog(onDismissRequest = {}, title = {
            Text(
                text = "Permission Request", style = TextStyle(
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        }, text = {
            Text(rationaleMessage)
        }, confirmButton = {
            Button(onClick = onRequestPermission) {
                Text("Give Permission")
            }
        })
    } else {
        LocationPermissionSwitchContent(
            viewState = viewState,
            permissionGranted = false,
            switchClickCallback = onRequestPermission,
            userDeniedPermission = deniedMessage,
        )
    }

}