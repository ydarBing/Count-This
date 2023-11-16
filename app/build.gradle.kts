import com.gurpgork.countthis.CtBuildType

plugins {
    alias(libs.plugins.countthis.android.application)
    alias(libs.plugins.countthis.android.application.compose)
    alias(libs.plugins.countthis.android.application.flavors)
    alias(libs.plugins.countthis.android.application.jacoco)
    alias(libs.plugins.countthis.android.hilt)
    id("jacoco")
    alias(libs.plugins.countthis.android.application.firebase)

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

//val useReleaseKeystore = rootProject.file("release/app-release.jks").exists()

android {
    namespace = "com.gurpgork.countthis"

    defaultConfig {
        applicationId = "com.gurpgork.countthis"
        versionCode = 1
        versionName = "0.1.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
//            signingConfig = signingConfigs["debug"]
//            versionNameSuffix = "-dev"
            applicationIdSuffix = CtBuildType.DEBUG.applicationIdSuffix
        }

        val release by getting {
            isMinifyEnabled = true
            applicationIdSuffix = CtBuildType.RELEASE.applicationIdSuffix
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.getByName("debug")

//            signingConfig = if (useReleaseKeystore) {
//                signingConfigs["release"]
//            } else {
//                // Otherwise just use the debug keystore (this is mainly for PR CI builds)
//                signingConfigs["debug"]
//            }
//            isShrinkResources = true
        }
    }
    packaging {
        resources {
            excludes += setOf("META-INF/LICENSE*", "META-INF/NOTICE.txt")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.feature.accountui)
    implementation(projects.feature.allcounters)
    implementation(projects.feature.counterdetails)
    implementation(projects.feature.addedit)
    implementation(projects.feature.settings)

    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.analytics)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.localbroadcastmanager)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.preferences)
    implementation(libs.androidx.savedstate)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.service)

    // ROOM
    implementation(libs.room.paging)
    // PAGING 3.0
    implementation(libs.androidx.paging.compose)

    implementation(libs.playservices.location)
    implementation(libs.playservices.places)
    implementation(libs.playservices.maps)
    implementation(libs.playservices.maps.utils)

    // TODO move to sync if adding to core
    implementation(libs.androidx.tracing.ktx)
//  NEEDED to stop pending intent crash..
    implementation(libs.androidx.work.ktx)
// Dagger - Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.ext.work)
    ksp(libs.hilt.ext.compiler)


    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)

    api(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.ui.viewbinding)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.compose.maps)
    implementation(libs.compose.maps.utils)
    implementation(libs.compose.maps.widgets)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.room.testing)
    testImplementation(libs.androidx.test.espresso.core)
    testImplementation(libs.androidx.compose.ui.test)
}

secrets {
    defaultPropertiesFileName = "local.defaults.properties"
}

if (file("google-services.json").exists()) {
    apply(plugin = libs.plugins.gms.get().pluginId)
    apply(plugin = libs.plugins.firebase.crashlytics.get().pluginId)
}

//fun <T : Any> propOrDef(propertyName: String, defaultValue: T): T {
//    @Suppress("UNCHECKED_CAST")
//    val propertyValue = project.properties[propertyName] as T?
//    return propertyValue ?: defaultValue
//}

// androidx.test is forcing JUnit, 4.12. This forces it to use 4.13
configurations.configureEach {
    resolutionStrategy {
        force(libs.junit4)
        // Temporary workaround for https://issuetracker.google.com/174733673
        force("org.objenesis:objenesis:2.6")
    }
}