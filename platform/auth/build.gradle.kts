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
                implementation(libs.kotlinx.atomicfu)
                implementation(project(":app-platform:base"))

                implementation(libs.ktor.clientAuth)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "tech.okcredit.auth"
}

dependencies {
    add("kspCommonMainMetadata", libs.kotlininject.compiler)
    add("kspAndroid", libs.kotlininject.compiler)
    add("kspIosArm64", libs.kotlininject.compiler)
    add("kspIosSimulatorArm64", libs.kotlininject.compiler)
    add("kspJvm", libs.kotlininject.compiler)
}
