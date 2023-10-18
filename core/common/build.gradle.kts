plugins {
    alias(libs.plugins.countthis.android.library)
    alias(libs.plugins.countthis.android.hilt)
    alias(libs.plugins.countthis.android.library.jacoco)
}

android {
    namespace = "com.gurpgork.countthis.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
//    testImplementation(projects.core.testing)
}