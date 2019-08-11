package com.github.pgreze.fswitch

import com.android.build.gradle.*
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import kotlin.reflect.KClass

class FeatureSwitchPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("Setup FeatureSwitchPlugin")
        project.extensions.create("fswitch", FeatureSwitchExtension::class.java)

        project.plugins.all {
            // From butterknife plugin https://bit.ly/2KnzVbo
            when (it) {
                is FeaturePlugin -> {
                    project.extensions[FeatureExtension::class].run {
                        setupTask(project, featureVariants)
                        setupTask(project, libraryVariants)
                    }
                }
                is LibraryPlugin -> {
                    project.extensions[LibraryExtension::class].run {
                        setupTask(project, libraryVariants)
                    }
                }
                is AppPlugin -> {
                    project.extensions[AppExtension::class].run {
                        setupTask(project, applicationVariants)
                    }
                }
            }
        }
    }

    private fun setupTask(project: Project, variants: DomainObjectSet<out BaseVariant>) {
        val ext = project.extensions.getByType(FeatureSwitchExtension::class.java)

        variants.all { variant ->
            val output = project.buildDir
                .resolve("generated/source/fswitch/${variant.dirName}")

            println("Generate switchs for ${ext.switchs} variant=${variant.name}")

            val task = project.tasks.create(
                "generate${variant.name.capitalize()}FeatureSwitchs",
                FeatureSwitchGenerator::class.java
            ) {
                it.outputDir = output
                it.applicationId = listOfNotNull(
                    variant.mergedFlavor.applicationId,
                    variant.buildType.applicationIdSuffix
                ).joinToString()
                it.variantName = variant.name
                it.userId = System.getProperty("user.name")
                it.switchs = ext.switchs
            }

            variant.registerJavaGeneratingTask(task, output)
        }
    }
}

private operator fun <T : Any> ExtensionContainer.get(type: KClass<T>): T =
    getByType(type.java)
