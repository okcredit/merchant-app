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

include(":platform:ab")
include(":platform:auth")
include(":platform:base")
include(":platform:device")
include(":platform:analytics")
include(":platform:identity")
include(":platform:identity:contract")
include(":platform:okdoc")
include(":platform:design_system")

include(":features:ledger:core")
include(":features:ledger:contract")
include(":features:auth_ui")
