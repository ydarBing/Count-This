plugins {
    id("countthis.android.feature")
    id("countthis.android.library.compose")
    id("countthis.android.library.jacoco")
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
    implementation(libs.playservices.location)
    implementation(libs.playservices.places)
    implementation(libs.playservices.maps)
    implementation(libs.playservices.maps.utils)
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    testImplementation(libs.junit4)
//    androidTestImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.test.espresso.core)
}