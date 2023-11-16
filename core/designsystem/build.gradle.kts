plugins {
    alias(libs.plugins.countthis.android.library)
    alias(libs.plugins.countthis.android.library.compose)
    alias(libs.plugins.countthis.android.library.jacoco)
}

android {
    namespace = "com.gurpgork.countthis.core.designsystem"

    lint {
        checkDependencies = true
    }
}

dependencies {

    api(libs.androidx.activity.compose)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    implementation(libs.kotlinx.datetime)

    debugApi(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.core.ktx)

//    androidTestImplementation(projects.core.testing)
}