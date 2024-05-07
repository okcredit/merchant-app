plugins {
    id("okcredit.root")

    // trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.sqldelight).apply(false)
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kover).apply(false)
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.gms.googleServices) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.ktorfit) apply false
}
