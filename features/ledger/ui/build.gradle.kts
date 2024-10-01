import okcredit.gradle.addKspDependencyForAllTargets

plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
    id("okcredit.compose")

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.mokkery)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.bundles.multiplatform.settings)
            implementation(libs.bundles.sqldelight.common)

            implementation(project(":platform:analytics"))
            implementation(project(":platform:base"))
            implementation(project(":platform:identity:contract"))
            implementation(project(":features:ledger:contract"))
            implementation(project(":features:ledger:core"))
            implementation(project(":platform:design_system"))
            implementation(project(":features:online_payments:collection"))

            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.runtime)

            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.bundles.coil.common)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutinesTest)
            implementation(libs.turbine)
            implementation(libs.assertk)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.androidDriver)
            implementation(libs.androidx.activityCompose)
            implementation(libs.coil.core.android)

            implementation(compose.uiTooling)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.nativeDriver)
        }
        jvmMain.dependencies {
            implementation(libs.sqldelight.sqliteDriver)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "app.okcredit.ledger.ui"
    generateResClass = always
}

android {
    namespace = "app.okcredit.ledger.ui"
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
addKspDependencyForAllTargets(libs.kotlininject.anvil.compiler)
