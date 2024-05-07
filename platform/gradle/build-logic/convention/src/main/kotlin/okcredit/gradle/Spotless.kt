package okcredit.gradle

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.LineEnding
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

fun Project.configureSpotless() {
  with(pluginManager) {
    apply("com.diffplug.spotless")
  }

  spotless {
    // Workaround for https://github.com/diffplug/spotless/issues/1644
    lineEndings = LineEnding.PLATFORM_NATIVE

    val ktlintVersion = libs.findVersion("ktlint").get().requiredVersion

    kotlin {
      target("src/**/*.kt")
      ktlint(ktlintVersion)
        .editorConfigOverride(
          mapOf(
            "ktlint_standard_package-name" to "disabled",
            "ktlint_standard_no-wildcard-imports" to "disabled",
            "ktlint_standard_filename" to "disabled",
            "ktlint_function_naming_ignore_when_annotated_with" to "Composable, Test",
          )
        )
    }

    kotlinGradle {
      target("*.kts")
      ktlint(ktlintVersion)
    }
  }
}

private fun Project.spotless(action: SpotlessExtension.() -> Unit) = extensions.configure<SpotlessExtension>(action)
