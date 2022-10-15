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

package com.gurpgork.countthis.components

import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue.Closed
import androidx.compose.runtime.Composable
import com.gurpgork.countthis.theme.CountThisTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountThisScaffold(
    drawerState: DrawerState = rememberDrawerState(initialValue = Closed),
    onCounterClicked: (String) -> Unit,
    onGroupClicked: (String) -> Unit,
    content: @Composable () -> Unit
) {
    CountThisTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                CountThisDrawer(
                    onCounterClicked = onCounterClicked,
                    onGroupClicked = onGroupClicked
                )
            },
            content = content
        )
    }
}
