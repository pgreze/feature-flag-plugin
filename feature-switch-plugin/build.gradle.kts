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
        create("featureSwitchs") {
            id = "feature-switchs"
            implementationClass = "com.github.pgreze.fswitch.FeatureSwitchPlugin"
        }
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
}
