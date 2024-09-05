import java.io.FileInputStream
import java.util.Properties

plugins {
    id("okcredit.android.application")
    id("okcredit.kotlin.android")
    id("okcredit.compose")

    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "app.okcredit.merchant.android"
    defaultConfig {
        applicationId = "app.okcredit.merchant"
        versionCode = 100
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "server"

    productFlavors {
        create("staging") {
            dimension = "server"
            versionNameSuffix = "-staging"
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher_staging"
            manifestPlaceholders["appName"] = "OkPay Staging"
            buildConfigField("String", "COMMON_BASE_URL", "\"https://staging.okapis.io/\"")
            buildConfigField("String", "MIXPANEL_TOKEN", "\"b8f375c8df48bb563b24d19af10ffe58\"")
        }

        create("prod") {
            dimension = "server"
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher"
            manifestPlaceholders["appName"] = "OkPay"
            buildConfigField("String", "COMMON_BASE_URL", "\"https://okapis.io/\"")
            buildConfigField("String", "MIXPANEL_TOKEN", "\"e83a9b4a18a938e68053d79329a48af8\"")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources.excludes +=
            setOf(
                // Exclude AndroidX version files
                "META-INF/*.version",
                // Exclude consumer proguard files
                "META-INF/proguard/*",
                // Exclude the Firebase/Fabric/other random properties files
                "/*.properties",
                "fabric/*.properties",
                "META-INF/*.properties",
            )
    }

    signingConfigs {
        val config = loadConfig()
        create("release") {
            storeFile = rootProject.file(config["signing_storeFile"]?.toString() ?: "")
            storePassword = config["signing_storePassword"]?.toString() ?: ""
            keyAlias = config["signing_keyAlias"]?.toString() ?: ""
            keyPassword = config["signing_keyPassword"]?.toString() ?: ""
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

fun loadConfig(): Properties {
    var buildType = "default"

    if (gradle.startParameter.taskNames.firstOrNull()?.contains("Release") == true) {
        buildType = "release"
    }

    val buildTypeEnvVar = System.getenv("BUILD_TYPE")
    if (!buildTypeEnvVar.isNullOrEmpty() && buildTypeEnvVar != "null") {
        buildType = buildTypeEnvVar
    }

    println("Task Started with BUILD_TYPE=$buildType")

    val configFile = "./.config/$buildType/config.properties"
    val config = Properties()
    config.load(FileInputStream(rootProject.file(configFile)))

    return config
}

dependencies {
    implementation(compose.ui)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.runtime)

    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.workmanager)

    implementation(project(":platform:ab"))
    implementation(project(":platform:analytics"))
    implementation(project(":platform:auth"))
    implementation(project(":platform:base"))
    implementation(project(":platform:device"))
    implementation(project(":platform:identity"))
    implementation(project(":platform:identity:contract"))
    implementation(project(":platform:okdoc"))

    implementation(project(":features:auth_ui"))
    implementation(project(":features:growth:customization"))
    implementation(project(":features:ledger:contract"))
    implementation(project(":features:ledger:core"))
    implementation(project(":features:ledger:ui"))

    implementation(project(":features:online_payments:collection"))

    implementation(project(":shared"))

    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.mixpanel.android)

    implementation(libs.voyager.navigator)
    implementation(libs.voyager.tabNavigator)
    implementation(libs.voyager.bottomsheet)

    ksp(libs.kotlininject.compiler)
}
