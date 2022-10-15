package com.gurpgork.countthis.ui_counter_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gurpgork.countthis.compose.LocalCountThisDateFormatter
import com.gurpgork.countthis.counter.HistoryEntity

@Composable
internal fun HistoryTab(history: List<HistoryEntity>) {
    val listState = rememberLazyListState()

    if (history.isNotEmpty()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            items(history) { history ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = history.count.toString())
                    }
                    if (history.start_date != null && history.end_date != null) {
                        val s = LocalCountThisDateFormatter.current
                            .formatShortDate(history.start_date)
                        val e = LocalCountThisDateFormatter.current
                            .formatShortDate(history.end_date)
                        Text(text = "$s - $e")
                    }
                }
            }

        }
    }
}
