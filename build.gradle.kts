import dev.deftu.gradle.utils.GameSide

val oneconfigVersion: String by project
val oneconfigWrapperVersion: String by project
val modApiVersion: String by project
val devAuthVersion: String by project

version = properties["mod.version"]!!

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://repo.polyfrost.cc/releases")
    maven("https://repo.spongepowered.org/repository/maven-public")
    maven("https://repo.hypixel.net/repository/Hypixel/")
}

plugins {
    java
    val dgt = "2.73.0"
    id("dev.deftu.gradle.tools") version dgt
    for (tool in listOf(
        "java",
        "minecraft.loom",
        "bloom",
        "ducks",
        "resources",
        "shadow"
    )) id("dev.deftu.gradle.tools.$tool") version dgt
}

val shadeOptionally = configurations.create("shadeOptionally")
configurations.named("implementation") {
    extendsFrom(shadeOptionally)
}

dependencies {
    shadeOptionally("cc.polyfrost:oneconfig-wrapper-launchwrapper:$oneconfigWrapperVersion")
    compileOnly("cc.polyfrost:oneconfig-${mcData.version}-${mcData.loader}:$oneconfigVersion")
    compileOnly("org.spongepowered:mixin:0.7.11-SNAPSHOT")

    modImplementation("net.hypixel:mod-api-forge:$modApiVersion")
    shadeOptionally("net.hypixel:mod-api-forge-tweaker:$modApiVersion")
}

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

tasks {
    jar {
        archiveFileName = "YedelMod-$version+${mcData}.jar"
        manifest.attributes(
            mapOf(
                "Main-Class" to "at.yedel.yedelmod.launch.YedelModWindow",
                "ModSide" to "CLIENT",
            )
        )
    }
    fatJar {
        configurations = listOf(shadeOptionally)
        relocate("net.hypixel.modapi.tweaker", "at.yedel.yedelmod.launch")
    }
}


