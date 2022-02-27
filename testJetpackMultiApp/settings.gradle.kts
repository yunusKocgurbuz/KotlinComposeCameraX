pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "testJetpackMultiApp"
include(":androidApp")
include(":shared")