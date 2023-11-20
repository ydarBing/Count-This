plugins {
    alias(libs.plugins.countthis.android.library)
    alias(libs.plugins.countthis.android.library.jacoco)
    alias(libs.plugins.countthis.android.hilt)
}

android {
    namespace = "com.gurpgork.countthis.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
//    testImplementation(projects.core.testing)
}