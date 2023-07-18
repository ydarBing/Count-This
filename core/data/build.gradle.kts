plugins {
    id("countthis.android.library")
    id("countthis.android.library.jacoco")
    id("countthis.android.hilt")
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
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
//    implementation(project(":core:network"))
//    implementation(project(":core:notifications"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.paging.common)

//    testImplementation(project(":core:datastore-test"))
//    testImplementation(project(":core:testing"))
}