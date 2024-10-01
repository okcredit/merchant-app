import okcredit.gradle.addKspDependencyForAllTargets

plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.bundles.multiplatform.settings)
            implementation(libs.bundles.sqldelight.common)

            implementation(project(":platform:analytics"))
            implementation(project(":platform:base"))
            implementation(project(":platform:identity:contract"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            implementation(libs.androidx.workmanager)
            implementation(libs.sqldelight.androidDriver)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.nativeDriver)
        }
        jvmMain.dependencies {
            implementation(libs.sqldelight.sqliteDriver)
        }
    }
}

sqldelight {
    databases {
        create("IdentityDatabase") {
            packageName.set("tech.okcredit.identity.local")
        }
    }
}

android {
    namespace = "tech.okcredit.identity"
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
addKspDependencyForAllTargets(libs.kotlininject.anvil.compiler)
