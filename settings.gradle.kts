pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

//plugins {
////    id("com.gradle.enterprise") version "3.13"
//
//    alias(libs.plugins.gradle.enterprise)
//}


//gradleEnterprise {
//    buildScan {
//        termsOfServiceUrl = "https://gradle.com/terms-of-service"
//        termsOfServiceAgree = "yes"
//        publishAlways()
//    }
//}

//enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "countthis"
include(":app")
include(":core:analytics")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:ui")

include(":feature:settings")
include(":feature:counterdetails")
include(":feature:addedit")
include(":feature:allcounters")
include(":feature:accountui")
