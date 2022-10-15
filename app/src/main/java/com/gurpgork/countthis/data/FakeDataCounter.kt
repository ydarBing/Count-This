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

package com.gurpgork.countthis.data

import com.gurpgork.countthis.counter.CounterScreenState

//import com.gurpgork.countthis.list.CounterListUiState
//private val initialFakeCounter = Counter(
//    name = "Test item",
//    currentCount = 10,
//    goal = 20,
//    groupName = "",
//    increment = 1,
//    lastUpdateDate = 2000,
//    startDate = 1500,
//    listIndex = 0,
//    mId = 1234,
//    todayCount = 3,
//    trackLocation = false,
//    updateTimes = CounterUpdates()
//)


/**
 * Example "fake" counter.
 */
val fakeCounter = CounterScreenState(
    name = "first item",
    currentCount = 10,
    dateCreated = 2000,
    mostRecent = 2010,
    lastLocation = "Here",
)

val exampleCounterUiState = fakeCounter
