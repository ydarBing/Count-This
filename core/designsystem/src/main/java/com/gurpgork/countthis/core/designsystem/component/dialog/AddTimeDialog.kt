package com.gurpgork.countthis.core.designsystem.component.dialog

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gurpgork.countthis.core.designsystem.component.bodyWidth
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.todayIn
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Locale


data class AddTimeInformation(
    var dateTime: LocalDateTime,
//    var date: LocalDate,
//    var time: LocalTime,
//    var location: Location? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimeDialog(
    onDismissRequest: () -> Unit,
    onConfirmNewIncrement: (AddTimeInformation) -> Unit,
    trackingLocation: Boolean,
    onLocationButtonClick: () -> Unit,
    dismissText: String,
    confirmText: String
) {
    val data = remember { mutableStateOf(AddTimeInformation(LocalDateTime.now())) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.secondaryContainer,
            tonalElevation = 3.0.dp,
        ) {
            Column(
                modifier = Modifier
                    .bodyWidth()
                    .padding(top = 20.dp),
//                    .fillMaxWidth(0.9f),
                verticalArrangement = Arrangement.Center
            ) {

                Body(
                    onDateChanged = {
//                                .atZone(ZoneId.systemDefault()).toLocalDate()
                        data.value.dateTime = LocalDateTime.of(
                            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate(),
                            data.value.dateTime.toLocalTime()
                        )
//                            LocalDateTime.of(it, data.value.dateTime.toLocalTime())
                    },
                    onTimeChanged = {
                        data.value.dateTime =
                            LocalDateTime.of(data.value.dateTime.toLocalDate(), it)
                    },
                    trackingLocation = trackingLocation,
                    onLocationButtonClick = onLocationButtonClick
                )
                BottomButtons(
                    onDismissRequest = onDismissRequest,
                    // TODO possibly causing unnecessary recompositions
                    onConfirm = {
                        onConfirmNewIncrement(data.value)
                    }, dismissText, confirmText
                )
            }
        }
    }
}

@Composable
private fun Title(title: String) {

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun Body(
    onDateChanged: (selectedDateMillisUTC: Long) -> Unit,
    onTimeChanged: (newTime: LocalTime) -> Unit,
    trackingLocation: Boolean,
    onLocationButtonClick: () -> Unit
) {
    val cal = Calendar.getInstance()
    val todaysDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val todaysInstant = todaysDate.atStartOfDayIn(TimeZone.currentSystemDefault())

    //TODO change date picker to as soon as date is pushed, that date is saved and dialog is dismissed
    var savedDate: Long by rememberSaveable { mutableLongStateOf(todaysInstant.toEpochMilliseconds()) }

    val datePickerState = rememberDatePickerState(
        // converting to Instant changes to UTC time,
        // but we want time zone information so we have today as initially selected
        initialSelectedDateMillis = savedDate,
        initialDisplayMode = DisplayMode.Picker
    )
    // TODO should we remember this calendar and then update its values for displaying purposes
    val hour24 = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)

    val timePickerState = rememberTimePickerState(initialHour = hour24, initialMinute = minute)
    var showDateDialog: Boolean by rememberSaveable { mutableStateOf(false) }
    var showTimeDialog: Boolean by rememberSaveable { mutableStateOf(false) }

    if (showDateDialog) {
        val confirmEnabled =
            remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
        DatePickerDialog(onDismissRequest = { showDateDialog = false }, confirmButton = {
            TextButton(
                onClick = {
                    showDateDialog = false
                    savedDate = datePickerState.selectedDateMillis!!
                    onDateChanged(savedDate)
                }, enabled = confirmEnabled.value
            ) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = {
                showDateDialog = false
            }) {
                Text("Cancel")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimeDialog) {
        AlertDialog(onDismissRequest = { showTimeDialog = false }) {
            Surface(
                modifier = Modifier
                    .requiredWidth(360.0.dp)
                    .heightIn(max = 568.0.dp),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.0.dp,
            ) {
                Column(
                    Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TimePicker(state = timePickerState)

                    // buttons
                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(DialogButtonsPadding)
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TextButton(onClick = {
                                showTimeDialog = false
                            }) {
                                Text("Cancel")
                            }
                            TextButton(onClick = {
                                showTimeDialog = false
                                val localTimePicked = LocalTime.now(ZoneId.systemDefault())
                                    .withHour(timePickerState.hour)
                                    .withMinute(timePickerState.minute)

                                onTimeChanged.invoke(localTimePicked)
                            }) {
                                Text("OK")
                            }
                        }

                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//TODO when feeling like it, turn these two rows into a single row for time (like google Maps Timeline)
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            val localDatePicked2 =
//                Instant.ofEpochMilli(datePickerState.selectedDateMillis!!).atOffset(
//                    ZoneOffset.UTC
//                ).toLocalDate()
//
//            CtOutlinedInputField(
//                label = stringResource(id = R.string.increment_time),
//                text = localDatePicked2.toString(),
//                placeholder = localDatePicked2.toString()
//            )
//
//            Text(text = stringResource(id = R.string.add_increment_at))
//
//            cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
//            cal.set(Calendar.MINUTE, timePickerState.minute)
//            val timeFormatted = SimpleDateFormat("h:mm a", Locale.getDefault()).format(cal.time)
//
//            CtOutlinedInputField(
//                text = timeFormatted,
//                placeholder = timeFormatted
//            )
//        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { showDateDialog = true }) {
                Text(text = "Date")
            }
//            val localDatePicked = Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
//                .atZone(ZoneId.systemDefault()).toLocalDate()
            val localDatePicked =
//                Instant.ofEpochMilli(savedDate).atOffset(
                Instant.ofEpochMilli(datePickerState.selectedDateMillis!!).atOffset(
                    ZoneOffset.UTC
                ).toLocalDate()
            Text(text = localDatePicked.toString())
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { showTimeDialog = true }) {
                Text(text = "Time")
            }
            // TODO maybe just add this time offset to a calendar so we can easily display a correct time
            cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
            cal.set(Calendar.MINUTE, timePickerState.minute)

            val timeFormatted = SimpleDateFormat("h:mm a", Locale.getDefault()).format(cal.time)
//            val timeFormatted2 = SimpleDateFormat("hh:mm a",
//                LocalContext.current.resources.configuration.locales.get(0)).format(cal.time)
            Text(text = timeFormatted.toString())
        }
        if (trackingLocation) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onLocationButtonClick) {
                    Text(text = "Change Location")
                }
//                Text(text = "LatLng")
            }
        }
    }
}

@Composable
private fun BottomButtons(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    dismissText: String,
    confirmText: String,
) {
    Row(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = onDismissRequest,
        ) {
            Text(
                text = dismissText,//stringResource(R.string.dialog_add_time_dismiss_button_text),
                fontSize = 20.sp
            )
        }

        TextButton(
            onClick = onConfirm,
        ) {
            Text(
                text = confirmText,//stringResource(R.string.dialog_add_time_confirm_button_text),
                fontSize = 20.sp
            )
        }
    }

}


private val DialogButtonsPadding = PaddingValues(bottom = 8.dp, end = 6.dp)
private val DialogButtonsMainAxisSpacing = 8.dp
private val DialogButtonsCrossAxisSpacing = 12.dp
