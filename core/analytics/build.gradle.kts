plugins {
    id("countthis.android.library")
    id("countthis.android.library.compose")
    id("countthis.android.hilt")
}

android {
    namespace = "com.gurpgork.countthis.core.analytics"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.core.ktx)
    implementation(libs.firebase.analytics)
    implementation(libs.kotlinx.coroutines.android)
}