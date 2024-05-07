plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":app-platform:base"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.mixpanel.android)

                implementation(libs.firebase.analytics)
                implementation(libs.firebase.crashlytics)
            }
        }
    }
}

android {
    namespace = "tech.okcredit.analytics"
}

dependencies {
    implementation(platform(libs.firebase.bom))

    add("kspCommonMainMetadata", libs.kotlininject.compiler)
    add("kspAndroid", libs.kotlininject.compiler)
    add("kspIosArm64", libs.kotlininject.compiler)
    add("kspIosSimulatorArm64", libs.kotlininject.compiler)
    add("kspJvm", libs.kotlininject.compiler)
}
