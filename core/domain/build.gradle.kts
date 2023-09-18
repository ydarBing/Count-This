plugins {
    id("countthis.android.library")
    id("countthis.android.library.jacoco")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.gurpgork.countthis.core.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(libs.androidx.paging.common)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    ksp(libs.hilt.compiler)

//    testImplementation(project(":core:testing"))
}