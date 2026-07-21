val fabricLoaderVersion: String by project
val oneconfigVersion: String by project
val modMenuVersion: String by project

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://repo.hypixel.net/repository/Hypixel/") {
        content {
            includeGroup("net.hypixel")
        }
    }
    maven("https://maven.fabricmc.net/releases") {
        content {
            includeGroup("net.fabricmc")
        }
    }
}

plugins {
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT"
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    implementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    implementation("org.polyfrost.oneconfig:${sc.current.version}-fabric:$oneconfigVersion")

    api("com.terraformersmc:modmenu:$modMenuVersion")
}

loom {
    runConfigs.remove(runConfigs["server"])

    runConfigs.all {
        runDir = "../../run"
        val resourcePackDir: String? = System.getenv("minecraft.resourcePackDir")
        if (!resourcePackDir.isNullOrBlank()) {
            println("Using resource pack directory $resourcePackDir from environment variable minecraft.resourcePackDir")
            programArgs("--resourcePackDir", resourcePackDir)
        }
    }
}
/*
toolkitLoomHelper {
    disableRunConfigs(GameSide.SERVER)

    useTweaker("at.yedel.yedelmod.launch.YedelModTweaker")
    useForgeMixin("yedelmod")
    useMixinRefMap("yedelmod")

    useDevAuth(devAuthVersion)
    useArgument("--version", "YedelMod", GameSide.BOTH)
    val resourcePackDir: String? = System.getenv("minecraft.resourcePackDir")
    if (!resourcePackDir.isNullOrBlank()) {
        println("Using resource pack directory $resourcePackDir from environment variable minecraft.resourcePackDir")
        useArgument("--resourcePackDir", resourcePackDir, GameSide.BOTH)
    }
}
 */

tasks {
    jar {
        archiveFileName = "YedelMod-${sc.current.project}.jar"
        manifest.attributes(
            mapOf(
                "Main-Class" to "at.yedel.yedelmod.launch.YedelModWindow"
            )
        )
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

