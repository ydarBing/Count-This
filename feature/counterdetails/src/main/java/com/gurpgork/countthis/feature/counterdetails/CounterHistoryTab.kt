package com.gurpgork.countthis.feature.counterdetails

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gurpgork.countthis.core.designsystem.component.Layout
import com.gurpgork.countthis.core.model.data.History
import com.gurpgork.countthis.core.ui.LocalCountThisDateFormatter

@Composable
fun HistoryTab(history: List<History>) {
    val listState = rememberLazyListState()

//    if (history.isNotEmpty()) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
//                .verticalScroll(rememberScrollState())
    ) {
        items(
            items = history,
            key = { it.id },
        ) {
            Row(
                modifier = Modifier
                    .fillParentMaxWidth()
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
