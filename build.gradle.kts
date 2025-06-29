import dev.deftu.gradle.utils.GameSide

val oneconfigVersion: String by project
val oneconfigLoaderVersion: String by project
val polyMixinVersion: String by project
val devAuthVersion: String by project

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://repo.polyfrost.cc/releases")
    maven("https://repo.polyfrost.cc/snapshots")
    maven("https://repo.spongepowered.org/repository/maven-public")
}

plugins {
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    for (tool in listOf(
        "java",
        "minecraft.loom",
        "bloom",
        "resources",
        "shadow"
    )) id("dev.deftu.gradle.tools.$tool")
}

toolkitLoomHelper {
    disableRunConfigs(GameSide.SERVER)
    val _polyMixinVersion = polyMixinVersion
    /*
    useOneConfig {
        version = oneconfigVersion
        loaderVersion = oneconfigLoaderVersion

        usePolyMixin = true
        polyMixinVersion = _polyMixinVersion

        applyLoaderTweaker = true

        +"commands"
        +"config"
        +"config-impl"
        +"events"
        +"hud"
        +"ui"
        +"utils"
        +"internal"
    }
     */
    useTweaker("org.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")

    if (mcData.isLegacyForge) {
        useForgeMixin(modData.id)
        useMixinRefMap(modData.id)
    }

    useDevAuth(devAuthVersion)
    useArgument("--version", "YedelMod", GameSide.BOTH)
    val resourcePackDir: String? = System.getenv("minecraft.resourcePackDir")
    if (!resourcePackDir.isNullOrBlank()) {
        println("Using resource pack directory $resourcePackDir from environment variable minecraft.resourcePackDir")
        useArgument("--resourcePackDir", resourcePackDir, GameSide.BOTH)
    }
}

dependencies {
    implementation("org.polyfrost.oneconfig:stage0:$oneconfigLoaderVersion:all")
    compileOnly("org.polyfrost.oneconfig:stage0:$polyMixinVersion")

    for (module in arrayOf("commands", "config", "config-impl", "events", "hud", "ui", "utils", "internal")) {
        compileOnly("org.polyfrost.oneconfig:$module:$oneconfigVersion")
    }
    compileOnly("org.polyfrost.oneconfig:1.8.9-forge:$oneconfigVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

tasks {
    jar {
        manifest.attributes(
            mapOf(
                "Main-Class" to "at.yedel.yedelmod.launch.YedelModWindow",
                "ModSide" to "CLIENT",
            )
        )
    }

    test {
        useJUnitPlatform()
    }
}


