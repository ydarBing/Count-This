plugins {
    alias(libs.plugins.countthis.android.library)
    alias(libs.plugins.countthis.android.library.compose)
    alias(libs.plugins.countthis.android.hilt)
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