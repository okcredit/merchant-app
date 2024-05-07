plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
    id("okcredit.compose")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.material3)
            }
        }
    }
}

android {
    namespace = "app.okcredit.ui"
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}
