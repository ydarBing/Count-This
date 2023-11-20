plugins {
    alias(libs.plugins.countthis.android.feature)
    alias(libs.plugins.countthis.android.library.compose)
    alias(libs.plugins.countthis.android.library.jacoco)
}

android {
    namespace = "com.gurpgork.countthis.feature.counterdetails"
}

dependencies {

    implementation(libs.kotlinx.datetime)
    implementation(libs.googlemaps.compose)
    implementation(libs.googlemaps.compose.utils)
    implementation(libs.googlemaps.compose.widgets)
    implementation(libs.googlemaps.location)
    implementation(libs.googlemaps.places)
    implementation(libs.googlemaps.maps)
    implementation(libs.googlemaps.maps.utils)
}