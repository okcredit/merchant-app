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

        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/releases/")
    }
}

rootProject.name = "merchant-app"
include(":androidApp")
include(":webApp")
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

include(":features:auth_ui")
include(":features:growth:customization")
include(":features:ledger:core")
include(":features:ledger:contract")
include(":features:online_payments:collection")
include(":features:ledger:ui")
include(":features:auth_ui")
