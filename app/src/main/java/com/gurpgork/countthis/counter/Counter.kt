/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gurpgork.countthis.counter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gurpgork.countthis.FunctionalityNotAvailablePopup
import com.gurpgork.countthis.R
import com.gurpgork.countthis.components.AnimatingFabContent
import com.gurpgork.countthis.components.baselineHeight
import com.gurpgork.countthis.data.fakeCounter
import com.gurpgork.countthis.theme.CountThisTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterScreen(counterData: CounterScreenState, onNavIconPressed: () -> Unit = { }) {

    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        TopAppBar(
            // Use statusBarsPadding() to move the app bar content below the status bar
            modifier = Modifier.statusBarsPadding(),
            //onNavIconPressed = onNavIconPressed,
            title = { },
            actions = {
                // More icon
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clickable(onClick = { functionalityNotAvailablePopupShown = true })
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                        .height(24.dp),
                    contentDescription = stringResource(id = R.string.more_options)
                )
            }
        )
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    CounterInfoFields(counterData, this@BoxWithConstraints.maxHeight)
                }
            }
//            CounterFab(
//                extended = scrollState.value == 0,
//                modifier = Modifier.align(Alignment.BottomEnd),
//                onFabClicked = { functionalityNotAvailablePopupShown = true }
//            )
        }
    }
}

@Composable
private fun CounterInfoFields(counterData: CounterScreenState, containerHeight: Dp) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        NameAndCount(counterData)

        CounterProperty(stringResource(R.string.stats_last_location), counterData.lastLocation)

        CounterProperty(stringResource(R.string.stats_current_count), counterData.currentCount.toString())

        // Add a spacer that always shows part (320.dp) of the fields list regardless of the device,
        // in order to always leave some content at the top.
        Spacer(Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
private fun NameAndCount(
    userData: CounterScreenState
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Name(
            userData,
            modifier = Modifier.baselineHeight(32.dp)
        )
        Count(
            userData,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .baselineHeight(24.dp)
        )
    }
}

@Composable
private fun Name(counterData: CounterScreenState, modifier: Modifier = Modifier) {
    Text(
        text = counterData.name,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
private fun Count(counterData: CounterScreenState, modifier: Modifier = Modifier) {
    Text(
        text = counterData.currentCount.toString(),
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}


@Composable
fun CounterProperty(label: String, value: String, isLink: Boolean = false) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Divider()
        Text(
            text = label,
            modifier = Modifier.baselineHeight(24.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        val style = if (isLink) {
            MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
        } else {
            MaterialTheme.typography.bodyLarge
        }
        Text(
            text = value,
            modifier = Modifier.baselineHeight(24.dp),
            style = style
        )
    }
}

@Composable
fun CounterError() {
    Text(stringResource(R.string.counter_error))
}

@Composable
fun CounterFab(
    extended: Boolean,
    modifier: Modifier = Modifier,
    onFabClicked: () -> Unit = { }
) {
    FloatingActionButton(
        onClick = onFabClicked,
        modifier = modifier
            .padding(16.dp)
            .navigationBarsPadding()
            .height(48.dp)
            .widthIn(min = 48.dp),
        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        AnimatingFabContent(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Edit, //Icons.Outlined.Chat
                    contentDescription = stringResource(
                        R.string.edit_counter
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        id = R.string.edit_counter
                    ),
                )
            },
            extended = extended

        )
    }
}

@Preview(widthDp = 640, heightDp = 360)
@Composable
fun CounterPreviewLandscapeDefault() {
    CountThisTheme {
        CounterScreen(fakeCounter)
    }
}

@Preview(widthDp = 360, heightDp = 480)
@Composable
fun CounterPreviewPortraitDefault() {
    CountThisTheme {
        CounterScreen(fakeCounter)
    }
}

@Preview
@Composable
fun CounterFabPreview() {
    CountThisTheme {
        CounterFab(extended = true)
    }
}
