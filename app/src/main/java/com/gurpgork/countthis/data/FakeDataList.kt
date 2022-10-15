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

import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.ui_list.CounterListViewState
import java.time.OffsetDateTime

private val initialFakeList = listOf(
    CounterEntity.EMPTY_COUNTER,
    CounterEntity(
        id = 1234,
        name = "first item",
        increment = 1,
        count = 10,
//        today_count = 3,
//        most_recent_date = OffsetDateTime.now(),
        creation_date_time = OffsetDateTime.now().minusDays(10),
        goal = 20,
        //groupName = "",
        list_index = 0,
        track_location = false,
        //updateTimes = CounterUpdates()
    ),
    CounterEntity(
        id = 1234,
        name = "first item",
        increment = 1,
        count = 10,
//        today_count = 3,
//        most_recent_date = OffsetDateTime.now(),
        creation_date_time = OffsetDateTime.now().minusDays(10),
        goal = 20,
        //groupName = "",
        list_index = 0,
        track_location = false,
        //updateTimes = CounterUpdates()
    ),
    CounterEntity(
        id = 1234,
        name = "first item",
        increment = 1,
        count = 10,
//        today_count = 3,
//        most_recent_date = OffsetDateTime.now(),
        creation_date_time = OffsetDateTime.now().minusDays(10),
        goal = 20,
        //groupName = "",
        list_index = 0,
        track_location = false,
        //updateTimes = CounterUpdates()
    ),
    CounterEntity(
        id = 1234,
        name = "first item",
        increment = 1,
        count = 10,
//        today_count = 3,
//        most_recent_date = OffsetDateTime.now(),
        creation_date_time = OffsetDateTime.now().minusDays(10),
        goal = 20,
        //groupName = "",
        list_index = 0,
        track_location = false,
        //updateTimes = CounterUpdates()
    ),
    CounterEntity(
        id = 1234,
        name = "first item",
        increment = 1,
        count = 10,
//        today_count = 3,
//        most_recent_date = OffsetDateTime.now(),
        creation_date_time = OffsetDateTime.now().minusDays(10),
        goal = 20,
        //groupName = "",
        list_index = 0,
        track_location = false,
        //updateTimes = CounterUpdates()
    ),
    CounterEntity(
        id = 1234,
        name = "first item",
        increment = 1,
        count = 10,
//        today_count = 3,
//        most_recent_date = OffsetDateTime.now(),
        creation_date_time = OffsetDateTime.now().minusDays(10),
        goal = 20,
        //groupName = "",
        list_index = 0,
        track_location = false,
        //updateTimes = CounterUpdates()
    )
)


val exampleListUiState = CounterListViewState(
    counters = initialFakeList,
)
val exampleEmptyListUiState = CounterListViewState(
    counters =  listOf<CounterEntity>()
)

/**
 * Example "fake" list.
 */
val fakeList = CounterListViewState(exampleListUiState.counters)
