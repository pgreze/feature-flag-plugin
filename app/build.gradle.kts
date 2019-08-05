import com.android.build.gradle.AppExtension
import com.github.pgreze.fswitch.FeatureSwitchExtension
import org.jetbrains.kotlin.config.KotlinCompilerVersion

buildscript {
    repositories {
        // Mandatory for feature-switchs transitive dependency
        google()
        jcenter()
    }
}

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("feature-switchs")
}

configure<AppExtension> {
    setCompileSdkVersion(29)
    defaultConfig {
        applicationId = "com.github.pgreze.fswitch"
        setMinSdkVersion(21)
        setTargetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

configure<FeatureSwitchExtension> {
    switchs = mapOf(
        "logs" to "debug",
        "analytics" to "release"
    )
}

dependencies {
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.core:core-ktx:1.0.2")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("com.google.android.material:material:1.0.0")

    testImplementation("junit:junit:4.12")

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
