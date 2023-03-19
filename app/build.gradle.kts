
plugins {
//    id("kotlin")
//    alias(libs.plugins.android.library)
//    alias(libs.plugins.android.lint)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

val appVersionCode = propOrDef("COUNTTHIS_VERSIONCODE", 1).toInt()
println("APK version code: $appVersionCode")

val useReleaseKeystore = rootProject.file("release/app-release.jks").exists()

android {
    namespace = "com.gurpgork.countthis"

    defaultConfig {
        applicationId = "com.gurpgork.countthis"
        versionCode = appVersionCode
        versionName = "0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        // We use a bundled debug keystore, to allow debug builds from CI to be upgradable
        getByName("debug") {
            storeFile = rootProject.file("release/app-debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release"){
            if (useReleaseKeystore) {
                storeFile = rootProject.file("release/app-release.jks")
                storePassword = propOrDef("COUNTTHIS_RELEASE_KEYSTORE_PWD", "")
                keyAlias = "countthis"
                keyPassword = propOrDef("COUNTTHIS_RELEASE_KEY_PWD", "")
            }
        }
    }

    lint {
        baseline = file("lint-baseline.xml")
        // Disable lintVital. Not needed since lint is run on CI
        checkReleaseBuilds = false
        // Ignore any tests
        ignoreTestSources = true
        // Make the build fail on any lint errors
        abortOnError = true
        // Allow lint to check dependencies
        checkDependencies = true
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composecompiler.get()
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    // I believe *.version is responsible for
//    packagingOptions {
//        packagingOptions.resources.excludes += setOf(
//            // Exclude AndroidX version files
//            "META-INF/*.version",
//            // Exclude consumer proguard files
//            "META-INF/proguard/*",
//            // Exclude the Firebase/Fabric/other random properties files
//            "/*.properties",
//            "fabric/*.properties",
//            "META-INF/*.properties"
//        )
//    }
    packagingOptions {
        resources {
            resources.excludes += setOf("META-INF/LICENSE*", "META-INF/NOTICE.txt")
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs["debug"]
            versionNameSuffix = "-dev"
            applicationIdSuffix = ".debug"
        }

        release {
            signingConfig = if (useReleaseKeystore) {
                signingConfigs["release"]
            } else {
                // Otherwise just use the debug keystore (this is mainly for PR CI builds)
                signingConfigs["debug"]
            }
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")
        }

//        create("benchmark") {
//            initWith(buildTypes["release"])
//            signingConfig = signingConfigs["debug"]
//            matchingFallbacks += "release"
//            isDebuggable = false
//        }
    }
}


ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg( "room.incremental", "true")
}

dependencies {
    // required to avoid crash on Android 12 API 31
    implementation (libs.androidx.work.runtime)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.localbroadcastmanager)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.preferences)
    implementation(libs.androidx.savedstate)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.service)

    // ROOM
    api(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
    // PAGING 3.0
    implementation(libs.androidx.paging.compose)

    implementation(libs.playservices.location)
    implementation(libs.playservices.places)
    implementation(libs.playservices.maps)
    implementation(libs.playservices.maps.utils)

// Dagger - Hilt
    implementation(libs.hilt.library)
    implementation(libs.hilt.compose)
    implementation(libs.hilt.work)
    kapt(libs.hilt.compiler)


    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines.android)

    api(platform(libs.compose.bom))
    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.maps)
    implementation(libs.compose.maps.widgets)
    implementation(libs.compose.material.iconsext)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3.windowSizeClass)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.ui.viewbinding)
    implementation(libs.compose.runtime)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.testmanifest)

    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.insetsui)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.navigation.material)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.flowlayout)


    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.androidx.test.espresso.core)
    testImplementation(libs.compose.ui.test)
}

android.applicationVariants.forEach { variant ->
    tasks.create("open${variant.name.capitalize()}") {
        dependsOn(tasks.named("install${variant.name.capitalize()}"))

        doLast {
            exec {
                commandLine = "adb shell monkey -p ${variant.applicationId} -c android.intent.category.LAUNCHER 1".split(" ")
            }
        }
    }
}

secrets {
    defaultPropertiesFileName = "local.defaults.properties"
}

if (file("google-services.json").exists()) {
    apply(plugin = libs.plugins.gms.googleServices.get().pluginId)
    apply(plugin = libs.plugins.firebase.crashlytics.get().pluginId)
}

fun <T : Any> propOrDef(propertyName: String, defaultValue: T): T {
    @Suppress("UNCHECKED_CAST")
    val propertyValue = project.properties[propertyName] as T?
    return propertyValue ?: defaultValue
}