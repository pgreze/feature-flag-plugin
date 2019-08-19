package com.github.pgreze.fflag

import com.android.build.gradle.*
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import kotlin.reflect.KClass

class FeatureFlagPlugin : Plugin<Project> {

    internal companion object {
        const val EXTENSION_NAME = "featureFlags"
    }

    override fun apply(project: Project) {
        // Init extension task
        project.extensions
            .create(EXTENSION_NAME, FeatureFlagExtension::class.java)
            // Init collection
            .flags = project.container(FeatureFlag::class.java)

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
        val ext = project.extensions.getByType(FeatureFlagExtension::class.java)

        variants.all { variant ->
            val output = project.buildDir
                .resolve("generated/source/fflag/${variant.dirName}")
            // TODO: investigate running after process*Manifest still before generate*Sources
            val packageId = ext.packageId
                ?: variant.mergedFlavor.applicationId
                ?: throw NullPointerException("Missing packageId in $EXTENSION_NAME")

            val task = project.tasks.create(
                "generate${variant.name.capitalize()}FeatureFlags",
                FeatureFlagGenerator::class.java
            ) {
                it.outputDir = output
                // Don't consider variant.buildType.applicationIdSuffix,
                // if package is not constant it will not compile for all variants.
                it.packageId = packageId
                it.variantName = variant.name
                it.userId = System.getProperty("user.name")
                it.flags = ext.flags.asMap.values.toSet()
            }

            variant.registerJavaGeneratingTask(task, output)
        }
    }
}

private operator fun <T : Any> ExtensionContainer.get(type: KClass<T>): T =
    getByType(type.java)
