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

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gurpgork.countthis.data.fakeCounter
import com.gurpgork.countthis.theme.CountThisTheme

@Preview(widthDp = 340, name = "340 width - Fake Counter")
@Composable
fun CounterPreview340() {
    CountThisTheme {
        CounterScreen(fakeCounter)
    }
}

@Preview(widthDp = 480, name = "480 width - Fake Counter")
@Composable
fun CounterPreview480FakeCounter() {
    CountThisTheme {
        CounterScreen(fakeCounter)
    }
}

@Preview(widthDp = 480, name = "480 width - Other")
@Composable
fun CounterPreview480Other() {
    CountThisTheme {
        CounterScreen(fakeCounter)
    }
}
@Preview(widthDp = 340, name = "340 width - Fake Counter - Dark")
@Composable
fun CounterPreview340FakeCounterDark() {
    CountThisTheme(darkTheme = true) {
        CounterScreen(fakeCounter)
    }
}

@Preview(widthDp = 480, name = "480 width - Fake Counter - Dark")
@Composable
fun CounterPreview480FakeCounterDark() {
    CountThisTheme(darkTheme = true) {
        CounterScreen(fakeCounter)
    }
}

@Preview(widthDp = 480, name = "480 width - Other - Dark")
@Composable
fun CounterPreview480OtherDark() {
    CountThisTheme(darkTheme = true) {
        CounterScreen(fakeCounter)
    }
}
