plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
    id("okcredit.compose")

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:shared_contract"))

                implementation(project(":app-platform:ab"))
                implementation(project(":app-platform:analytics"))
                implementation(project(":app-platform:auth"))
                implementation(project(":app-platform:base"))
                implementation(project(":app-platform:device"))
                implementation(project(":app-platform:identity"))
                implementation(project(":app-platform:identity:contract"))
                implementation(project(":app-platform:okdoc"))
                implementation(project(":app-platform:design_system"))

                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.runtime)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.bundles.moko.resources)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activityCompose)

                implementation(compose.uiTooling)
            }
        }
    }
}

android {
    namespace = "app.okcredit.shared"
}

dependencies {
    add("kspCommonMainMetadata", libs.kotlininject.compiler)
    add("kspAndroid", libs.kotlininject.compiler)
    add("kspIosArm64", libs.kotlininject.compiler)
    add("kspIosSimulatorArm64", libs.kotlininject.compiler)
    add("kspJvm", libs.kotlininject.compiler)
}
