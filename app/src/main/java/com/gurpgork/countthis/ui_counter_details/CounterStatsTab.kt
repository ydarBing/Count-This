package com.gurpgork.countthis.ui_counter_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.gurpgork.countthis.FunctionalityNotAvailablePopup
import com.gurpgork.countthis.R
import com.gurpgork.countthis.components.baselineHeight
import com.gurpgork.countthis.compose.Layout
import com.gurpgork.countthis.compose.LocalCountThisDateFormatter
import com.gurpgork.countthis.counter.IncrementEntity
import com.gurpgork.countthis.data.entities.CounterEntity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StatsTab(
    counterInfo: CounterEntity,
    increments: List<IncrementEntity>,
    hasLocations: Boolean,
) {
    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
//        verticalArrangement = Arrangement.Top
    ) {
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Name(counterInfo)
//        }
        CounterInfoFields(counterInfo, increments.lastOrNull())
        // counter has locations (increment with longitude value)
        if (hasLocations) {
            GoogleMapClusteringWithIncrements(increments)
        }
    }
}


@Composable
private fun CounterInfoFields(counterInfo: CounterEntity, mostRecentIncrement: IncrementEntity?) {
    if (counterInfo.creation_date_time != null) {
        Divider()
        CounterProperty(
            stringResource(R.string.stats_date_created),
            LocalCountThisDateFormatter.current
                .formatShortDate(counterInfo.creation_date_time)
        )
    }

    Divider()
    CounterProperty(
        stringResource(R.string.stats_current_count),
        counterInfo.count.toString()
    )
    if (mostRecentIncrement != null) {
        Divider()
        CounterProperty(
            stringResource(R.string.stats_most_recent),
            LocalCountThisDateFormatter.current
                .formatMediumDateTime(mostRecentIncrement.date)
        )
    }
    Divider()
    GoalProgressBar(counterInfo.count, counterInfo.goal)
}


@Composable
private fun Name(counterInfo: CounterEntity, modifier: Modifier = Modifier) {
    Text(
        text = counterInfo.name,
        modifier = modifier,
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
private fun CounterProperty(label: String, value: String) {
    Row(
        modifier = Modifier
            .padding(horizontal = Layout.bodyMargin, vertical = Layout.gutter)
//            .wrapContentHeight(Alignment.CenterVertically)
            .fillMaxWidth(),
//            .heightIn(min = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
//            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GoalProgressBar(count: Int, goal: Int) {
    if (goal > 0) {
        Column(
            modifier = Modifier
                .padding(horizontal = Layout.bodyMargin, vertical = Layout.gutter)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            val goalsAchieved = (count / goal)
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.secondaryContainer,
                progress = (count - (goalsAchieved * goal)) / goal.toFloat()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "0",
                    style = MaterialTheme.typography.labelSmall
                )

                Column(verticalArrangement = Arrangement.Top) {
                    Text(
                        text = goal.toString(),
                        style = MaterialTheme.typography.labelSmall,
                    )
                    if (goalsAchieved > 0) {
                        Text(
                            text = "X$goalsAchieved",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoogleMapClusteringWithIncrements(increments: List<IncrementEntity>) {
    val markers = remember { mutableStateListOf<ClusterMapMarker>() }
    //TODO go through increments and place any increment that has location on map
    LaunchedEffect(Unit) {
        increments.forEachIndexed { index, increment ->
            if (increment.latitude != null && increment.longitude != null) {
                val position = LatLng(
                    increment.latitude,
                    increment.longitude
                )
//                val dateSnippet = LocalCountThisDateFormatter.current
//                    .formatMediumDateTime(increment.date)
                markers.add(
                    ClusterMapMarker(
                        position,
                        "",
                        "test snippet",
                        index
                    )
                )
            }
        }
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
private fun GoogleMapClustering(markers: List<ClusterMapMarker>) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markers.last().position, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        var clusterManager by remember { mutableStateOf<ClusterManager<ClusterMapMarker>?>(null) }
        // TODO DOES USING CONTEXT LIKE THIS WORK AS EXPECTED???????
        val context = LocalContext.current
        MapEffect(markers) { map ->
            if (clusterManager == null) {
                clusterManager = ClusterManager<ClusterMapMarker>(context, map)
            }
            clusterManager?.addItems(markers)
        }
        LaunchedEffect(key1 = cameraPositionState.isMoving) {
            if (!cameraPositionState.isMoving) {
                clusterManager?.onCameraIdle()
            }
        }
//        MarkerInfoWindow(
//            state = rememberMarkerState(position = )
//        )
    }
}

data class ClusterMapMarker(
    val itemPosition: LatLng,
    val itemTitle: String,
    val itemSnippet: String,
    val number: Int,
) : ClusterItem {
    override fun getPosition(): LatLng = itemPosition
    override fun getTitle(): String = itemTitle
    override fun getSnippet(): String = itemSnippet
}