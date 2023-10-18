plugins {
    alias(libs.plugins.countthis.android.feature)
    alias(libs.plugins.countthis.android.library.compose)
    alias(libs.plugins.countthis.android.library.jacoco)
}

android {
    namespace = "com.gurpgork.countthis.feature.accountui"
}