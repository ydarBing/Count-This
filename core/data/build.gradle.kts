plugins {
    alias(libs.plugins.countthis.android.library)
    alias(libs.plugins.countthis.android.hilt)
    alias(libs.plugins.countthis.android.library.jacoco)
    id("kotlinx-serialization")
}

android {
    namespace = "com.gurpgork.countthis.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(projects.core.analytics)
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.model)
//    implementation(projects.core.network)
//    implementation(projects.core.notifications)

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.paging.common)

//    testImplementation(projects.core.datastore-test)
//    testImplementation(projects.core.testing)
}