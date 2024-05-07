plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":app-platform:base"))
                api(libs.voyager.navigator)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "app.okcredit.shared.contract"
}
