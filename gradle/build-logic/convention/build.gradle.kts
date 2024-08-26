plugins {
    `kotlin-dsl`
    alias(libs.plugins.spotless)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint()
            .editorConfigOverride(
                mapOf(
                    "ktlint_standard_package-name" to "disabled",
                    "ktlint_standard_no-wildcard-imports" to "disabled",
                    "ktlint_standard_no-semi" to "disabled",
                    "ktlint_standard_trailing-comma-on-declaration-site" to "disabled",
                    "ktlint_standard_trailing-comma-on-call-site" to "disabled",
                    "ktlint_standard_filename" to "disabled",
                    "ktlint_function_naming_ignore_when_annotated_with" to "Composable, Test",
                )
            )
    }

    kotlinGradle {
        target("*.kts")
        ktlint()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = "okcredit.kotlin.multiplatform"
            implementationClass = "okcredit.gradle.KotlinMultiplatformConventionPlugin"
        }

        register("root") {
            id = "okcredit.root"
            implementationClass = "okcredit.gradle.RootConventionPlugin"
        }

        register("kotlinAndroid") {
            id = "okcredit.kotlin.android"
            implementationClass = "okcredit.gradle.KotlinAndroidConventionPlugin"
        }

        register("androidApplication") {
            id = "okcredit.android.application"
            implementationClass = "okcredit.gradle.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "okcredit.android.library"
            implementationClass = "okcredit.gradle.AndroidLibraryConventionPlugin"
        }

        register("compose") {
            id = "okcredit.compose"
            implementationClass = "okcredit.gradle.ComposeMultiplatformConventionPlugin"
        }
    }
}
