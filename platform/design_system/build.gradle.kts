plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
    id("okcredit.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.material3)
        }
    }
}

android {
    namespace = "app.okcredit.ui"
}
