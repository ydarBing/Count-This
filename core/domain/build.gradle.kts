plugins {
    alias(libs.plugins.countthis.android.library)
    alias(libs.plugins.countthis.android.library.jacoco)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.gurpgork.countthis.core.domain"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(libs.androidx.paging.common)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    ksp(libs.hilt.compiler)

//    testImplementation(projects.core.testing)
}