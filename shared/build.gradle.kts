import okcredit.gradle.addKspDependencyForAllTargets
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
    id("okcredit.compose")

    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        targets.withType<KotlinNativeTarget>().configureEach {
            binaries.framework {
                isStatic = true
                baseName = "shared"
                linkerOpts("-lsqlite3")
            }
        }

        commonMain.dependencies {
            implementation(project(":shared:shared_contract"))

            implementation(project(":platform:ab"))
            implementation(project(":platform:analytics"))
            implementation(project(":platform:auth"))
            implementation(project(":platform:base"))
            implementation(project(":platform:design_system"))
            implementation(project(":platform:device"))
            implementation(project(":platform:identity"))
            implementation(project(":platform:identity:contract"))
            implementation(project(":platform:okdoc"))
            implementation(project(":platform:web"))

            implementation(project(":features:growth:customization"))
            implementation(project(":features:auth_ui"))
            implementation(project(":features:ledger:contract"))
            implementation(project(":features:ledger:core"))
            implementation(project(":features:online_payments:collection"))
            implementation(project(":features:ledger:ui"))

            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.runtime)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.bundles.coil.common)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            implementation(libs.androidx.activityCompose)
            implementation(compose.uiTooling)
        }
        iosMain.invoke {
        }
    }
}

android {
    namespace = "app.okcredit.shared"
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
addKspDependencyForAllTargets(libs.kotlininject.anvil.compiler)
