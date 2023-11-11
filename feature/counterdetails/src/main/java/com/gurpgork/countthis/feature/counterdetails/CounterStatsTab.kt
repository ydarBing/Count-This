package com.gurpgork.countthis.feature.counterdetails

import android.location.Location
import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.gurpgork.countthis.core.designsystem.component.FunctionalityNotAvailablePopup
import com.gurpgork.countthis.core.designsystem.component.Layout
import com.gurpgork.countthis.core.model.data.Counter
import com.gurpgork.countthis.core.model.data.Increment
import com.gurpgork.countthis.core.ui.LocalCountThisDateFormatter

var initialSetup: Boolean = true

@Composable
fun StatsTab(
    counterInfo: Counter,
    increments: List<Increment>,
    hasLocations: Boolean,
    onMapMoved: (mapMoving: Boolean) -> Unit
) {
    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }
//    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
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
//            Box(Modifier.weight(0.9f)){
                GoogleMapClusteringWithIncrements(increments, onMapMoved)
//            }
        }
    }
}


@Composable
private fun CounterInfoFields(counterInfo: Counter, mostRecentIncrement: Increment?) {
    HorizontalDivider()
    CounterProperty(
        stringResource(R.string.stats_date_created),
        LocalCountThisDateFormatter.current
            .formatShortDate(counterInfo.creationDate)
    )

    HorizontalDivider()
    CounterProperty(
        stringResource(R.string.stats_current_count),
        counterInfo.count.toString()
    )
    if (mostRecentIncrement != null) {
        HorizontalDivider()
        CounterProperty(
            stringResource(R.string.stats_most_recent),
            LocalCountThisDateFormatter.current
                .formatMediumDateTime(mostRecentIncrement.incrementDate)
        )
    }
    HorizontalDivider()
    GoalProgressBar(counterInfo.count, counterInfo.goal)
}


@Composable
private fun Name(counterInfo: Counter, modifier: Modifier = Modifier) {
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
                progress = {
                    (count - (goalsAchieved * goal)) / goal.toFloat()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.secondaryContainer,
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
private fun GoogleMapClusteringWithIncrements(
    increments: List<Increment>,
    onMapMoved: (mapMoving: Boolean) -> Unit
) {
    val markers = remember { mutableStateListOf<ClusterMapMarker>() }
    //TODO go through increments and place any increment that has location on map
    LaunchedEffect(Unit) {
        increments.forEachIndexed { index, increment ->
            if (increment.latitude != null && increment.longitude != null) {
                val position = LatLng(
                    increment.latitude!!,
                    increment.longitude!!
                )
//                val dateSnippet = LocalCountThisDateFormatter.current
//                    .formatMediumDateTime(increment.date)
                markers.add(
                    ClusterMapMarker(
                        position,
                        increment.incrementDate.toString(),
                        "",
                        index
                    )
                )
            }
        }
    }

    if(!markers.isEmpty())
        GoogleMapClustering(markers = markers, onMapMoved)
}
private fun areBoundsTooSmall(bounds: LatLngBounds, minDistanceInMeter: Int): Boolean {
    val result = FloatArray(1)
    Location.distanceBetween(
        bounds.southwest.latitude,
        bounds.southwest.longitude,
        bounds.northeast.latitude,
        bounds.northeast.longitude,
        result
    )
    return result[0] < minDistanceInMeter
}
private fun fitMarkersInCameraPosition(markers: List<ClusterMapMarker>): CameraUpdate//CameraPosition
{
    // TODO no markers should mean no map
//    if(markers.isEmpty())
//        return CameraPosition.fromLatLngZoom(LatLng(), 15f)
    if(markers.size == 1)
        return CameraUpdateFactory.newLatLngZoom(markers.last().position, 13f)

    val bounds = LatLngBounds.builder()
    markers.forEach {
        bounds.include(it.position)
    }
    val b = bounds.build()

    return if (areBoundsTooSmall(b, 300))
        CameraUpdateFactory.newLatLngZoom(b.center, 15f)
    else
        CameraUpdateFactory.newLatLngBounds(b, 150)
}

@OptIn(MapsComposeExperimentalApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
private fun GoogleMapClustering(
    markers: List<ClusterMapMarker>,
    onMapMoved: (mapMoving: Boolean) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markers.last().position, 10f)//fitMarkersInCameraPosition(markers)
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (cameraPositionState.isMoving) {
            Log.d(
                "Camera movement",
                "Map camera started moving due to ${cameraPositionState.cameraMoveStartedReason.name}"
            )
        } else {
            // this is where we want to update location query text
            Log.d(
                "Camera stopped moving",
                "Map camera stopped moving due to ${cameraPositionState.cameraMoveStartedReason.name}"
            )
//            cameraPositionUpdated(cameraPositionState.position.target)
        }
    }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true,
                maxZoomPreference = 20f,
//                minZoomPreference = 2f
            )
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                mapToolbarEnabled = false,
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true,
                rotationGesturesEnabled = true,
                tiltGesturesEnabled = true,
                scrollGesturesEnabled = true,
                scrollGesturesEnabledDuringRotateOrZoom = true
            )
        )
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
//            .pointerInteropFilter{
            .motionEventSpy {
                when (it.action) {
                    MotionEvent.ACTION_MOVE -> {
                        onMapMoved.invoke(true)
                    }

                    else -> {
                        onMapMoved.invoke(false)
                    }
                }
            },
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings,
        properties = mapProperties,
        onMapLoaded = {
            cameraPositionState.move(fitMarkersInCameraPosition(markers))
        }
    ){
        Clustering(
            items = markers,
            // Optional: Handle clicks on clusters, cluster items, and cluster item info windows
            onClusterClick = {
                Log.d("CLUSTER", "Cluster clicked! $it")
                false
            },
            onClusterItemClick = {
                Log.d("CLUSTER", "Cluster item clicked! $it")
                false
            },
            onClusterItemInfoWindowClick = {
                Log.d("CLUSTER", "Cluster item info window clicked! $it")
            },
        )
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
    override fun getZIndex(): Float? {
        // TODO how should this be set up
        return 1.0f
    }
}

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun StatsTabComposablePreview() {
//    val singapore = LatLng(1.35, 103.87)
//    StatsTab(
//        counterInfo = CounterEntity.EMPTY_COUNTER,
//        increments = listOf(
//            IncrementEntity(counterId = 1,
//                date = OffsetDateTime.now(),
//                latitude = singapore.latitude,
//                longitude = singapore.longitude)
//        ),
//        hasLocations = true
//    )
//}