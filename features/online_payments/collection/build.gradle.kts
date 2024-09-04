plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.bundles.multiplatform.settings)
            implementation(libs.bundles.sqldelight.common)

            implementation(project(":platform:analytics"))
            implementation(project(":platform:base"))
            implementation(project(":platform:ab"))
            implementation(project(":platform:identity:contract"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            implementation(libs.androidx.workmanager)
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
        create("CollectionDatabase") {
            packageName.set("tech.okcredit.collection.local")
            generateAsync.set(false)
        }
    }
}

android {
    namespace = "tech.okcredit.collection"
}

dependencies {
    add("kspCommonMainMetadata", libs.kotlininject.compiler)
    add("kspAndroid", libs.kotlininject.compiler)
    add("kspIosArm64", libs.kotlininject.compiler)
    add("kspIosSimulatorArm64", libs.kotlininject.compiler)
    add("kspJvm", libs.kotlininject.compiler)
}