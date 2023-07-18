plugins {
    id("countthis.android.library")
    id("countthis.android.library.jacoco")
    id("countthis.android.hilt")
    id("countthis.android.room")
}

android {
    namespace = "com.gurpgork.countthis.core.database"

}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.room.paging)

//    androidTestImplementation(project(":core:testing"))
}