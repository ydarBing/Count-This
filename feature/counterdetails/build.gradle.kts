plugins {
    id("countthis.android.feature")
    id("countthis.android.library.compose")
    id("countthis.android.library.jacoco")
}

android {
    namespace = "com.gurpgork.countthis.feature.counterdetails"
}

dependencies {

    implementation(libs.accompanist.insetsui)
    implementation(libs.compose.maps)
    implementation(libs.compose.maps.utils)
    implementation(libs.compose.maps.widgets)
    implementation(libs.kotlinx.datetime)
    implementation(libs.playservices.location)
    implementation(libs.playservices.places)
    implementation(libs.playservices.maps)
    implementation(libs.playservices.maps.utils)
}