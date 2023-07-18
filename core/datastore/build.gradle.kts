plugins {
    id("countthis.android.library")
    id("countthis.android.library.jacoco")
    id("countthis.android.hilt")
    alias(libs.plugins.protobuf)
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
    namespace = "com.gurpgork.countthis.core.datastore"
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

// Setup protobuf configuration, generating lite Java and Kotlin classes
protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin")  {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.protobuf.kotlin.lite)

//    testImplementation(project(":core:datastore-test"))
//    testImplementation(project(":core:testing"))
}
