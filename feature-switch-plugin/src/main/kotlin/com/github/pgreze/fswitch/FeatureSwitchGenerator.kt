package com.github.pgreze.fswitch

import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.lang.model.element.Modifier

open class FeatureSwitchGenerator : DefaultTask() {

    @get:OutputDirectory
    var outputDir: File? = null

    @get:Input
    var variantName: String? = null

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
                .initializer((variantName == value).toString())
                .build()
            switchsFile.addField(field)
        }

        JavaFile.builder("generated", switchsFile.build())
            .build()
            .writeTo(outputDir)
    }
}
