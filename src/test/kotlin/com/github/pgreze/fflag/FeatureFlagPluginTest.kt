package com.github.pgreze.fflag

import com.google.common.truth.Truth.assertThat
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object FeatureFlagPluginTest : Spek({

    describe("A project") {

        val project: Project = ProjectBuilder.builder().build()

        describe("only apply the plugin") {
            before {
                project.pluginManager.apply(FeatureFlagPlugin::class.java)
            }

            it("should inject extension task") {
                assertThat(project.extensions.getByName(FeatureFlagPlugin.EXTENSION_NAME))
                    .isInstanceOf(FeatureFlagExtension::class.java)
            }
        }

        xdescribe("apply android-ap and fflag plugins") {
            before {
                project.pluginManager.apply("com.android.application")
                project.pluginManager.apply("com.github.pgreze.feature-flags")
            }
        }
    }
})
