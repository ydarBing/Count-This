package com.gurpgork.countthis.compose.dialog

//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.compose.ui.window.Dialog
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMapOptions
//import com.google.android.gms.maps.MapView
//import com.google.android.gms.maps.model.CameraPosition
//import com.google.android.gms.maps.model.LatLng
//import com.google.maps.android.compose.GoogleMap
//import com.google.maps.android.compose.MapProperties
//import com.google.maps.android.compose.MapUiSettings
//import com.google.maps.android.compose.rememberCameraPositionState
//import com.gurpgork.countthis.ui_list.CounterListViewModel
//
////@Composable
////fun AddLocationDialogOld(
//////    modifier: Modifier,
////    onDismissRequest: () -> Unit,
////    viewModel: CounterListViewModel
////) {
////    var searchQuery by remember { mutableStateOf(TextFieldValue(viewModel.addressQuery)) }
//////    var text by remember { viewModel.addressText }
////    Dialog(
////        onDismissRequest = onDismissRequest,
////        content = {
////            Column(
////                modifier = Modifier.fillMaxSize(0.9f),
////                verticalArrangement = Arrangement.SpaceBetween
////            ) {
////                Title()
////                Body()
////            }
////        },
////    )
////}
//
//
//@Composable
//private fun Title() {
//    Box{
//       TextField(value = text, onValueChange = )
//    }
//}
//
//
//@Composable
//private fun Body(
//    isEnabled: Boolean,
//    mapView: MapView,
//) {
//    val userLocation = viewModel.getUserLocation()
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(userLocation, 10f)
//    }
//    val mapProperties by remember {
//        mutableStateOf(
//            MapProperties(
//                isMyLocationEnabled = true,
//                maxZoomPreference = 10f,
//                minZoomPreference = 5f
//            )
//        )
//    }
//    val mapUiSettings by remember {
//        mutableStateOf(
//            MapUiSettings(
//                mapToolbarEnabled = false,
//                zoomControlsEnabled = true,
//                zoomGesturesEnabled = true
//            )
//        )
//    }
//
//    GoogleMap(
//        modifier = Modifier.fillMaxSize(),
//        cameraPositionState = cameraPositionState,
//        uiSettings = mapUiSettings,
//        properties = mapProperties,
//    )
//
//
////    AndroidView(factory = { mapView }
////    ) {
////        mapView.getMapAsync { map ->
////            map.uiSettings.setAllGesturesEnabled(isEnabled)
////
////            val location = viewModel.location.value
////            val position = LatLng(location.latitude, location.longitude)
////            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
////
////            map.setOnCameraIdleListener {
////                val cameraPosition = map.cameraPosition
////                viewModel.updateLocation(
////                    cameraPosition.target.latitude,
////                    cameraPosition.target.longitude
////                )
////            }
////        }
////    }
//}
