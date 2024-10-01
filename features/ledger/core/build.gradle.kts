import okcredit.gradle.addKspDependencyForAllTargets

plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
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
            implementation(project(":platform:okdoc"))

            implementation(project(":features:ledger:contract"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutinesTest)
            implementation(libs.turbine)
            implementation(libs.assertk)
        }
        androidMain.dependencies {
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
        create("LedgerDatabase") {
            packageName.set("app.okcredit.ledger.local")
        }
    }
}

android {
    namespace = "app.okcredit.ledger.core"
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
addKspDependencyForAllTargets(libs.kotlininject.anvil.compiler)
