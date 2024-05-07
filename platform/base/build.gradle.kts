plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
    id("okcredit.compose")

    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib-common"))
                api(libs.kotlinx.coroutinesCore)
                api(libs.kotlinx.serializationCore)
                api(libs.kotlinx.dateTime)

                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)

                api(libs.log.kermit)

                api(libs.bundles.multiplatform.settings)

                api(libs.ktorfit)
                api(libs.ktorfit.converter)
                api(libs.ktor.contentNegotiation)
                api(libs.ktor.json)
                api(libs.ktor.clientLogging)

                api(libs.kotlininject.runtime)

                api(libs.voyager.navigator)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.androidx.workmanager)
            }
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.kotlininject.compiler)
    add("kspAndroid", libs.kotlininject.compiler)
    add("kspIosArm64", libs.kotlininject.compiler)
    add("kspIosSimulatorArm64", libs.kotlininject.compiler)
    add("kspJvm", libs.kotlininject.compiler)
}

android {
    namespace = "okcredit.base"
}
