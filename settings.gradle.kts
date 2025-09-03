pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Add Maven repo for AndroidX components
        maven { url = uri("https://androidx.dev/snapshots/builds") }
    }
}
rootProject.name = "Dreams TV"
include(":app")

