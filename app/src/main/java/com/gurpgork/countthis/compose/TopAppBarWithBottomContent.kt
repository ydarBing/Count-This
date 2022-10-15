/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gurpgork.countthis.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AppBarDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * A wrapper around [TopAppBar] which allows some [bottomContent] below the bar, but within the same
 * surface. This is useful for tabs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithBottomContent(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit,
    bottomContent: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    topAppBarColors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(),
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(backgroundColor),
    tonalElevation: Dp = AppBarDefaults.TopAppBarElevation,
    shadowElevation: Dp = AppBarDefaults.TopAppBarElevation,
    contentPadding: PaddingValues? = null
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        modifier = modifier
    ) {

        Column(contentPadding?.let { Modifier.padding(it) } ?: Modifier) {

            CenterAlignedTopAppBar(
                title = title,
                navigationIcon = navigationIcon,
                actions = actions,
                colors = topAppBarColors,
//                backgroundColor = Color.Transparent,
//                contentColor = LocalContentColor.current,
            )

            bottomContent?.invoke()
        }
    }
}