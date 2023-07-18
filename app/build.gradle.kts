import com.gurpgork.countthis.CtBuildType

plugins {
    id("countthis.android.application")
    id("countthis.android.application.compose")
    id("countthis.android.application.flavors")
    id("countthis.android.application.jacoco")
    id("countthis.android.hilt")
    id("jacoco")
    id("countthis.android.application.firebase")

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

//val appVersionCode = propOrDef("COUNTTHIS_VERSIONCODE", 1).toInt()
//println("APK version code: $appVersionCode")

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

//    signingConfigs {
//        // We use a bundled debug keystore, to allow debug builds from CI to be upgradable
//        getByName("debug") {
//            storeFile = rootProject.file("release/app-debug.keystore")
//            storePassword = "android"
//            keyAlias = "androiddebugkey"
//            keyPassword = "android"
//        }
//        create("release"){
//            if (useReleaseKeystore) {
//                storeFile = rootProject.file("release/app-release.jks")
//                storePassword = propOrDef("COUNTTHIS_RELEASE_KEYSTORE_PWD", "")
//                keyAlias = "countthis"
//                keyPassword = propOrDef("COUNTTHIS_RELEASE_KEY_PWD", "")
//            }
//        }
//    }

//    lint {
//        baseline = file("lint-baseline.xml")
//        // Disable lintVital. Not needed since lint is run on CI
//        checkReleaseBuilds = false
//        // Ignore any tests
//        ignoreTestSources = true
//        // Make the build fail on any lint errors
//        abortOnError = true
//        // Allow lint to check dependencies
//        checkDependencies = true
//    }
//TODO test uncommenting this
//    buildFeatures {
//        viewBinding = true
//        compose = true
//    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = libs.versions.composecompiler.get()
//    }



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
    implementation(project(":feature:accountui"))
    implementation(project(":feature:allcounters"))
    implementation(project(":feature:counterdetails"))
    implementation(project(":feature:editcreate"))
    implementation(project(":feature:settings"))

    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:analytics"))

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
    kapt(libs.hilt.ext.compiler)


    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)

    api(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
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


    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.insetsui)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.navigation.material)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.systemuicontroller)


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