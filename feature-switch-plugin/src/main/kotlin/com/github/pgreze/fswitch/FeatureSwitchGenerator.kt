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

    @get:OutputDirectory
    var outputDir: File? = null

    @get:Input
    var applicationId: String? = null

    @get:Input
    var variantName: String? = null

    /** From System properties */
    @get:Input
    var userId: String? = null

    @get:Input
    var switchs: Map<String, String> = emptyMap()

    @TaskAction
    fun execute() {
        val switchsFile = TypeSpec
                .classBuilder("Switchs")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

        switchs.entries.forEach { (key, value) ->
            val field = FieldSpec.builder(Boolean::class.java, key)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                // Boolean.parseBoolean is disabling ConstantConditionIf lint
                .initializer("Boolean.parseBoolean(\"${resolveSwitch(value)}\")")
                .build()
            switchsFile.addField(field)
        }

        JavaFile.builder("$applicationId.fswitch", switchsFile.build())
            .build()
            .writeTo(outputDir)
    }

    private fun resolveSwitch(value: String): Boolean =
        value.split("-").any { condition ->
            if (condition.startsWith("@")) {
                return@any condition.substring(1) == userId
            }

            val tokens = condition.camelCaseTokens()
            if (tokens.size > 1) {
                // Match this exact variantName
                condition == variantName
            } else {
                // Match any part of variant including it
                condition in variantName!!.camelCaseTokens()
            }
        }
}

internal fun String.camelCaseTokens(): List<String> =
    // See https://stackoverflow.com/a/14816052
    split("(?<=\\p{Ll})(?=\\p{Lu})|(?<=\\p{L})(?=\\p{Lu}\\p{Ll})".toRegex())
        .map { it.toLowerCase(Locale.US) }
