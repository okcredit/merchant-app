plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    id("okcredit.ktorfit")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.bundles.multiplatform.settings)
                implementation(project(":app-platform:base"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.workmanager)
            }
        }
    }
}

android {
    namespace = "tech.okcredit.device"
}

dependencies {
    add("kspCommonMainMetadata", libs.kotlininject.compiler)
    add("kspAndroid", libs.kotlininject.compiler)
    add("kspIosArm64", libs.kotlininject.compiler)
    add("kspIosSimulatorArm64", libs.kotlininject.compiler)
    add("kspJvm", libs.kotlininject.compiler)
}
