repositories {
    google()
    jcenter()
}

plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}

gradlePlugin {
    plugins {
        create("featureSwitchs") {
            id = "feature-switchs"
            implementationClass = "com.github.pgreze.fswitch.FeatureSwitchPlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())

    implementation("com.android.tools.build:gradle:3.4.2")
    implementation("com.squareup:javapoet:1.11.1")
}
