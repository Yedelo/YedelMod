rootProject.name = "YedelMod"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
    }
    plugins {
        id("gg.essential.loom") version "1.9.26"
    }
}
