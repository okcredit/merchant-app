plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
    id("okcredit.ktorfit")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.bundles.multiplatform.settings)
                implementation(libs.bundles.sqldelight.common)

                implementation(project(":app-platform:analytics"))
                implementation(project(":app-platform:base"))
                implementation(project(":app-platform:identity:contract"))
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
        create("IdentityDatabase") {
            packageName.set("tech.okcredit.identity.local")
            generateAsync.set(false)
        }
    }
}

android {
    namespace = "tech.okcredit.identity"
}

dependencies {
    add("kspCommonMainMetadata", libs.kotlininject.compiler)
    add("kspAndroid", libs.kotlininject.compiler)
    add("kspIosArm64", libs.kotlininject.compiler)
    add("kspIosSimulatorArm64", libs.kotlininject.compiler)
    add("kspJvm", libs.kotlininject.compiler)
}
