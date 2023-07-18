plugins {
    id("countthis.android.library")
    id("countthis.android.library.compose")
    id("countthis.android.library.jacoco")
}

android {
    namespace = "com.gurpgork.countthis.core.designsystem"

    lint {
        checkDependencies = true
    }
}

dependencies {

    api(libs.androidx.activity.compose)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)

    debugApi(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.core.ktx)

//    androidTestImplementation(project(":core:testing"))
}