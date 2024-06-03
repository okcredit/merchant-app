package okcredit.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KtorfitPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("de.jensklingenberg.ktorfit")
            }

            dependencies {
                val ktorFitCompiler = libs.findLibrary("ktorfit-ksp").get()

                add("kspCommonMainMetadata", ktorFitCompiler)
                add("kspAndroid", ktorFitCompiler)
                add("kspIosArm64", ktorFitCompiler)
                add("kspIosSimulatorArm64", ktorFitCompiler)
                add("kspJvm", ktorFitCompiler)
            }
        }
    }

}