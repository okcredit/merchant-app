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
            implementation(libs.bundles.sqldelight.common)
            api(project(":platform:base"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
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
        create("AbDatabase") {
            packageName.set("tech.okcredit.ab.local")
        }
    }
}

android {
    namespace = "tech.okcredit.ab"
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
addKspDependencyForAllTargets(libs.kotlininject.anvil.compiler)
