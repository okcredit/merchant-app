import okcredit.gradle.addKspDependencyForAllTargets

plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.bundles.multiplatform.settings)

            implementation(libs.kotlinx.atomicfu)

            implementation(project(":platform:base"))

            implementation(libs.ktor.clientAuth)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "tech.okcredit.auth"
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
addKspDependencyForAllTargets(libs.kotlininject.anvil.compiler)
