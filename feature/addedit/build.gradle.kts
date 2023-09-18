plugins {
    id("countthis.android.feature")
    id("countthis.android.library.compose")
    id("countthis.android.library.jacoco")
}

android {
    namespace = "com.gurpgork.countthis.feature.addedit"
}

dependencies {
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.accompanist.permissions)
    implementation(libs.kotlinx.datetime)
}