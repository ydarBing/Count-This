plugins {
    id("countthis.android.library")
    id("countthis.android.library.jacoco")
    id("countthis.android.hilt")
}

android {
    namespace = "com.gurpgork.countthis.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
//    testImplementation(project(":core:testing"))
}