buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.2")
        classpath(kotlin("gradle-plugin", version = "1.3.21"))
    }
}

allprojects {
    // Mandatory for custom plugin transitive dependencies
    buildscript {
        repositories {
            google()
            jcenter()
        }
    }

    repositories {
        google()
        jcenter()
    }
}

// Allow to run composite-build task in this project
tasks.register("publishToMavenLocal") {
    dependsOn(gradle.includedBuild("feature-switch-plugin").task(":publishToMavenLocal"))
    group = "Publishing tasks"
    description = "Publishes all Maven publications to the local Maven cache."
}

tasks.register("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}
