plugins {
    alias(libs.plugins.countthis.android.library)
    alias(libs.plugins.countthis.android.library.jacoco)
    alias(libs.plugins.countthis.android.hilt)
    alias(libs.plugins.countthis.android.room)
}

android {
    namespace = "com.gurpgork.countthis.core.database"

}

dependencies {
    implementation(projects.core.model)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.room.paging)

//    androidTestImplementation(project(":core:testing"))
}