// Top-level build file where you can add configuration options common to all sub-projects/modules.

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.BasePlugin
import dagger.hilt.android.plugin.HiltExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.hilt.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.secrets.gradlePlugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.gms.googleServices) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.versionCatalogUpdate)
}

//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//        // Used for snapshots if needed
//         maven("https://oss.sonatype.org/content/repositories/snapshots/")
//
////        if (!libs.androidx.compose.material3.snapshot.isEmpty()) {
////            maven { url Urls.composeMaterial3SnapshotRepo }
////        }
////        if (!libs.androidx.compose.snapshot.isEmpty()) {
////            maven { url Urls.composeSnapshotRepo }
////        }
//    }
//}

subprojects {
    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)
    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint(libs.versions.ktlint.get())
                .editorConfigOverride(mapOf("disabled_rules" to "filename"))
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
//            allWarningsAsErrors = true

            // Enable experimental coroutines APIs, including Flow
            freeCompilerArgs += listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=kotlin.Experimental"
            )

//            if (project.hasProperty("countthis.enableComposeCompilerReports")) {
//                freeCompilerArgs += listOf(
//                    "-P",
//                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
//                            project.buildDir.absolutePath + "/compose_metrics"
//                )
//                freeCompilerArgs += listOf(
//                    "-P",
//                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
//                            project.buildDir.absolutePath + "/compose_metrics"
//                )
//            }
            // Set JVM target to 11
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

    plugins.withId(rootProject.libs.plugins.hilt.get().pluginId) {
        // Had to turn this off for napt to work
        extensions.getByType<HiltExtension>().enableAggregatingTask = true
    }
    plugins.withId(rootProject.libs.plugins.kotlin.kapt.get().pluginId) {
        extensions.getByType<KaptExtension>().correctErrorTypes = true
    }

    plugins.withType<BasePlugin>().configureEach {
        extensions.configure<BaseExtension> {
            compileSdkVersion(libs.versions.compileSdk.get().toInt())
            defaultConfig {
                minSdk = libs.versions.minSdk.get().toInt()
                targetSdk = libs.versions.targetSdk.get().toInt()
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }
    }
}