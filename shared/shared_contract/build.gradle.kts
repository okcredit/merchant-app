plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":platform:base"))
            api(libs.voyager.navigator)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "app.okcredit.shared.contract"
}
