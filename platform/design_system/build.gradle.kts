plugins {
    id("okcredit.android.library")
    id("okcredit.kotlin.multiplatform")
    id("okcredit.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.runtime)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "app.okcredit.ui"
    generateResClass = always
}

android {
    namespace = "app.okcredit.ui"
}
