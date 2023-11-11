package com.gurpgork.countthis.feature.counterdetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gurpgork.countthis.core.designsystem.component.Layout
import com.gurpgork.countthis.core.model.data.History
import com.gurpgork.countthis.core.ui.LocalCountThisDateFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryTab(
    history: List<History>,
    selectedIds: Set<Long>,
    onRowLongClick: (Long) -> Unit,
    onRowClick: (Long) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = history,
            key = { it.id },
        ) {
            Row(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .combinedClickable(
                        onLongClick = { onRowLongClick(it.id) },
                        onClick = { onRowClick(it.id) }
                    )
                    .background(
                        if(selectedIds.contains(it.id)){
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    )
                    .heightIn(min = 48.dp)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .padding(horizontal = Layout.bodyMargin, vertical = Layout.gutter)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = it.count.toString())
                }
                val s = LocalCountThisDateFormatter.current
                    .formatShortDate(it.startDate)
                val e = LocalCountThisDateFormatter.current
                    .formatShortDate(it.endDate)
                Text(text = "$s - $e")
            }

            HorizontalDivider()
        }

    }
//    }
//    else {
//        val singapore = LatLng(1.35, 103.87)
//        val cameraPositionState: CameraPositionState = rememberCameraPositionState {
//            position = CameraPosition.fromLatLngZoom(singapore, 11f)
//        }
//        Box(Modifier.fillMaxSize()) {
//            GoogleMap(cameraPositionState = cameraPositionState)
//            Button(onClick = {
//                // Move the camera to a new zoom level
//                cameraPositionState.move(CameraUpdateFactory.zoomIn())
//            }) {
//                Text(text = "Zoom In")
//            }
//        }
//    }
}
