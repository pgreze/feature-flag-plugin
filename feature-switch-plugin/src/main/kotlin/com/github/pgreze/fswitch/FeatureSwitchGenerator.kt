package com.github.pgreze.fswitch

import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.*
import javax.lang.model.element.Modifier

open class FeatureSwitchGenerator : DefaultTask() {

    companion object {
        const val CONDITION_SEPARATOR = " "
    }

    @get:OutputDirectory
    lateinit var outputDir: File

    @get:Input
    lateinit var packageId: String

    @get:Input
    lateinit var variantName: String

    /** From System properties */
    @get:Input
    lateinit var userId: String

    @get:Input
    lateinit var switchs: Set<FeatureSwitch>

    @TaskAction
    fun execute() {
        // TODO: add file comments with generated code warnings and lib info
        val switchsFile = TypeSpec
                .classBuilder("Switchs")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

        switchs.forEach { switch ->
            val field = FieldSpec.builder(Boolean::class.java, switch.name)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                // Boolean.parseBoolean is disabling ConstantConditionIf lint
                .initializer("Boolean.parseBoolean(\"${resolveSwitch(switch.conditions!!)}\")")
                .apply { switch.description?.let { addJavadoc("$it\n") } }
                .build()
            switchsFile.addField(field)
        }

        JavaFile.builder("$packageId.fswitch", switchsFile.build())
            .build()
            .writeTo(outputDir)
    }

    private fun resolveSwitch(value: String): Boolean =
        value.split(CONDITION_SEPARATOR).any { condition ->
            if (condition.startsWith("@")) {
                return@any condition.substring(1) == userId
            }

            val tokens = condition.camelCaseTokens()
            if (tokens.size > 1) {
                // Match this exact variantName
                condition == variantName
            } else {
                // Match any part of variant including it
                condition in variantName.camelCaseTokens()
            }
        }
}

internal fun String.camelCaseTokens(): List<String> =
    // See https://stackoverflow.com/a/14816052
    split("(?<=\\p{Ll})(?=\\p{Lu})|(?<=\\p{L})(?=\\p{Lu}\\p{Ll})".toRegex())
        .map { it.toLowerCase(Locale.US) }
