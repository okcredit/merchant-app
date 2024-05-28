plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
    id("okcredit.compose")

    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:shared_contract"))

            implementation(project(":platform:ab"))
            implementation(project(":platform:analytics"))
            implementation(project(":platform:auth"))
            implementation(project(":platform:base"))
            implementation(project(":platform:device"))
            implementation(project(":platform:identity"))
            implementation(project(":platform:identity:contract"))
            implementation(project(":platform:okdoc"))
            implementation(project(":platform:design_system"))

            implementation(project(":features:ledger:contract"))
            implementation(project(":features:ledger:core"))

            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.runtime)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            implementation(libs.bundles.moko.resources)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            implementation(libs.androidx.activityCompose)

            implementation(compose.uiTooling)
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
