rootProject.name = "YedelMod"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://maven.deftu.dev/releases")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.deftu.dev/snapshots")
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.10-alpha.6"
}

stonecutter {
    create(rootProject) {
        fun registerProject(versionString: String, loader: String) {
            version("$versionString-$loader", versionString).buildscript("build.$loader.gradle.kts")
        }

        registerProject("26.2", "fabric")
        registerProject("26.1", "fabric")
        registerProject("1.8.9", "forge")
        vcsVersion = "26.1-fabric"
    }
}