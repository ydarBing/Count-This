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

package com.gurpgork.countthis.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gurpgork.countthis.core.designsystem.icon.CtIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CtTopAppBar(
    @StringRes titleRes: Int,
    navigationIcon: ImageVector,
    navigationIconContentDescription: String?,
    actionIcon: ImageVector,
    actionIconContentDescription: String?,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onNavIconClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        navigationIcon = {
            IconButton(onClick = { onNavIconClick }) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
//            CountThisIcon(
//                contentDescription = stringResource(id = R.string.navigation_drawer_open),
//                modifier = Modifier
//                    .size(64.dp)
//                    .clickable(onClick = onNavIconPressed)
//                    .padding(16.dp)
//            )
        },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = colors,
        modifier = modifier.testTag("CtTopAppBar"),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CtTopAppBar(
    @StringRes titleRes: Int,
    actionIcon: ImageVector,
    actionIconContentDescription: String?,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = colors,
        modifier = modifier.testTag("CtTopAppBar"),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CountThisAppBarPreview() {
    CtTopAppBar(
        titleRes = android.R.string.untitled,
        navigationIcon = CtIcons.Add,
        navigationIconContentDescription = "Navigation Icon",
        actionIcon = CtIcons.Remove,
        actionIconContentDescription = "Action Icon",
    )
}

