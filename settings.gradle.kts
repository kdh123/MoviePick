rootProject.name = "MoviePick"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://jogamp.org/deployment/maven")
    }
}
include(":core:common")
include(":core:designsystem")
include(":core:di")

include(":core:ui")
include(":core:network")
include(":data")
include(":data:movie")
include(":data:tv")
include(":domain")
include(":domain:movie")
include(":domain:tv")
include(":core:testing")
include(":feature:upcoming")
include(":feature:video")
include(":composeApp")
include(":network")
include(":feature:home")
include(":testing")
include("network")
