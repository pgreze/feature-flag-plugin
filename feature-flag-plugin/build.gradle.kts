import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    kotlin("jvm") version "1.3.21"
    id("java-gradle-plugin")
    `maven-publish`
}

group = "com.github.pgreze"
// TODO: use environment variable
version = "0.1"

gradlePlugin {
    plugins {
        create("featureFlags") {
            // Follow https://docs.gradle.org/current/userguide/custom_plugins.html#sec:creating_a_plugin_id
            id = "$group.feature-flags"
            implementationClass = "com.github.pgreze.fflag.FeatureFlagPlugin"
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("spek")
    }
}

repositories {
    google()
    jcenter()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

    implementation("com.android.tools.build:gradle:3.4.2")
    implementation("com.squareup:javapoet:1.11.1")

    testImplementation("com.google.truth:truth:1.0")
    testImplementation(gradleTestKit())
    val spekVersion = "2.0.6"
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
    testImplementation(kotlin("reflect", KotlinCompilerVersion.VERSION))
}
