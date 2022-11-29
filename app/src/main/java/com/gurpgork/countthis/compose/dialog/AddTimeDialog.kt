package com.gurpgork.countthis.compose.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.model.LatLng
import com.gurpgork.countthis.R
import java.util.*


data class AddTimeInformation(
    var time: String,
    var date: String,
    var location: LatLng? = null
)

@Composable
fun AddTimeDialog(
//    title: String,
//    modifier: Modifier,
//    buttons: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmNewIncrement: (AddTimeInformation) -> Unit,
    trackingLocation: Boolean,
    onLocationButtonClick: () -> Unit
) {
    val data = remember { mutableStateOf(AddTimeInformation("", "")) }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(MaterialTheme.colorScheme.primaryContainer),
            verticalArrangement = Arrangement.Center
        ) {
//                Title(title)
            Body(
                onDateChanged = { data.value.date = it },
                onTimeChanged = { data.value.time = it },
                trackingLocation = trackingLocation,
                onLocationButtonClick = onLocationButtonClick
            )
            BottomButtons(
                onDismissRequest,
                onConfirm = { onConfirmNewIncrement(data.value) })
        }
    }
}

@Composable
private fun Title(title: String) {

}


@Composable
private fun Body(
    onDateChanged: (newDate: String) -> Unit,
    onTimeChanged: (newTime: String) -> Unit,
    trackingLocation: Boolean,
    onLocationButtonClick: () -> Unit
) {
//    // Value for storing time as a string
//    val mTime = remember { mutableStateOf("") }
//    // Declaring a string value to
//    // store date in string format
//    val mDate = remember { mutableStateOf("") }


    // Fetching the Local Context
    val mContext = LocalContext.current
    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    // Fetching current year, month and day
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    val dateText = remember { mutableStateOf("$mDay/${mMonth + 1}/$mYear") }
    val timeText = remember { mutableStateOf("$mHour:$mMinute") }

    // Creating a TimePicker dialog
    val mTimePickerDialog = TimePickerDialog(
        mContext,
        { _, mHour: Int, mMinute: Int ->
            val newTime = "$mHour:$mMinute"
            onTimeChanged(newTime)
            timeText.value = newTime
//            data.value.time = "$mHour:$mMinute"
//            mTime.value = "$mHour:$mMinute"
        },
        mHour, mMinute, false
    )
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val newDate = "$mDayOfMonth/${mMonth + 1}/$mYear"
            onDateChanged(newDate)
            dateText.value = newDate
//            data.value.date = "$mDayOfMonth/${mMonth + 1}/$mYear"
//            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )
//    if (!mDatePickerDialog.isShowing)
//        mDatePickerDialog.show()
//    if (!mTimePickerDialog.isShowing)
//        mTimePickerDialog.show()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { mDatePickerDialog.show() }) {
                Text(text = "Open Date Picker")
            }
//            Text(text = mDate.value)
            Text(text = dateText.value)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { mTimePickerDialog.show() }) {
                Text(text = "Open Time Picker")
            }
//            Text(text = mTime.value)
            Text(text = timeText.value)
        }
        if (trackingLocation) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(onClick = onLocationButtonClick) {
                    Text(text = "Open Location Picker")
                }
                Text(text = "LatLng")
            }
        }
    }
}

@Composable
private fun BottomButtons(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = { onDismissRequest() },
//            modifier = Modifier
//                .fillMaxWidth(),
//                .padding(end = 5.dp),
//            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = stringResource(R.string.dialog_add_time_dismiss_button_text),
                fontSize = 20.sp
            )
        }

        TextButton(
            onClick = onConfirm,
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = stringResource(R.string.dialog_add_time_confirm_button_text),
                fontSize = 20.sp
            )
        }
    }

}
