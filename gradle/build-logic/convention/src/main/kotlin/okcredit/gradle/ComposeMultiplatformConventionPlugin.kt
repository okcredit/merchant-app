package okcredit.gradle

import com.android.build.gradle.internal.lint.AndroidLintAnalysisTask
import com.android.build.gradle.internal.lint.LintModelWriterTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginManager.apply("org.jetbrains.compose")
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
    configureCompose()
  }
}

fun Project.configureCompose() {
  composeCompiler {
    // Enable 'strong skipping'
    // https://medium.com/androiddevelopers/jetpack-compose-strong-skipping-mode-explained-cbdb2aa4b900
    enableStrongSkippingMode.set(true)

    if (project.providers.gradleProperty("okcredit.enableComposeCompilerReports").isPresent) {
      val composeReports = layout.buildDirectory.map { it.dir("reports").dir("compose") }
      reportsDestination.set(composeReports)
      metricsDestination.set(composeReports)
    }
  }

  // Workaround for:
  // Task 'generateDebugUnitTestLintModel' uses this output of task
  // 'generateResourceAccessorsForAndroidUnitTest' without declaring an explicit or
  // implicit dependency.
  tasks.matching { it is AndroidLintAnalysisTask || it is LintModelWriterTask }.configureEach {
    mustRunAfter(tasks.matching { it.name.startsWith("generateResourceAccessorsFor") })
  }
}

fun Project.composeCompiler(block: ComposeCompilerGradlePluginExtension.() -> Unit) {
  extensions.configure<ComposeCompilerGradlePluginExtension>(block)
}
