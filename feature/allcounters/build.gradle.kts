plugins {
    alias(libs.plugins.countthis.android.feature)
    alias(libs.plugins.countthis.android.library.compose)
    alias(libs.plugins.countthis.android.library.jacoco)
}

android {
    namespace = "com.gurpgork.countthis.feature.allcounters"
}

dependencies {

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.paging.compose)
    implementation(libs.compose.maps)
    implementation(libs.compose.maps.utils)
    implementation(libs.compose.maps.widgets)
    implementation(libs.kotlinx.datetime)
    implementation(libs.playservices.location)
    implementation(libs.playservices.places)
    implementation(libs.playservices.maps)
    implementation(libs.playservices.maps.utils)
}