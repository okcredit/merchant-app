plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
    id("okcredit.compose")

    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
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
            api(libs.bundles.ktor.common)
            api(libs.kotlininject.runtime)
            api(libs.bundles.voyager.common)
            implementation(libs.bundles.sqldelight.common)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            api(libs.androidx.workmanager)
            implementation(libs.sqldelight.androidDriver)
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.nativeDriver)
            implementation(libs.ktor.client.ios)
        }
        jvmMain.dependencies {
            implementation(libs.sqldelight.sqliteDriver)
            implementation(libs.ktor.client.cio)
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
