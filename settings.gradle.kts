enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("gradle/build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex(".*google.*")
                includeGroupByRegex(".*android.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex(".*google.*")
                includeGroupByRegex(".*android.*")
            }
        }
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "merchant-app"
include(":androidApp")
include(":shared")
include(":shared:shared_contract")

include(":app-platform:ab")
include(":app-platform:auth")
include(":app-platform:base")
include(":app-platform:device")
include(":app-platform:analytics")
include(":app-platform:identity")
include(":app-platform:identity:contract")
include(":app-platform:okdoc")
include(":app-platform:design_system")