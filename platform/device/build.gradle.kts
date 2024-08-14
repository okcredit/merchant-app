plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")

    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.bundles.multiplatform.settings)
            implementation(project(":platform:base"))
        }
    }
}

android {
    namespace = "tech.okcredit.device"
}

dependencies {
    add("kspCommonMainMetadata", libs.kotlininject.compiler)
    add("kspAndroid", libs.kotlininject.compiler)
    add("kspIosArm64", libs.kotlininject.compiler)
    add("kspIosSimulatorArm64", libs.kotlininject.compiler)
    add("kspJvm", libs.kotlininject.compiler)
}
