// Top-level build configuration
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    // Remove these two lines:
    // alias(libs.plugins.googleServices) apply false
    // alias(libs.plugins.firebaseCrashlytics) apply false
}

allprojects {
    // Optimize task configuration and execution phase
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
            freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }

    // Speed up Java compilation tasks
    tasks.withType<JavaCompile>().configureEach {
        options.isFork = true
    }
}

// Speed up clean builds by specifying exactly what to delete
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}