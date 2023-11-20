plugins {
    alias(libs.plugins.countthis.android.library)
    alias(libs.plugins.countthis.android.library.jacoco)
    alias(libs.plugins.countthis.android.hilt)
    id("kotlinx-serialization")
    // getting error when trying to put secrets not in app gradle
//    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.gurpgork.countthis.core.network"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}
// getting error when trying to put secrets not in app gradle
//secrets {
//    defaultPropertiesFileName = "local.defaults.properties"
//}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(libs.googlemaps.location)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.service)
}