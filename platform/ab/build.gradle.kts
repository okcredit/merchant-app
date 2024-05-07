plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    id("okcredit.ktorfit")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.bundles.sqldelight.common)
                api(project(":app-platform:base"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.workmanager)
                implementation(libs.sqldelight.androidDriver)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(libs.sqldelight.nativeDriver)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.sqliteDriver)
            }
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

dependencies {
    add("kspCommonMainMetadata", libs.kotlininject.compiler)
    add("kspAndroid", libs.kotlininject.compiler)
    add("kspIosArm64", libs.kotlininject.compiler)
    add("kspIosSimulatorArm64", libs.kotlininject.compiler)
    add("kspJvm", libs.kotlininject.compiler)
}
