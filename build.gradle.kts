import dev.deftu.gradle.utils.GameSide

val oneconfigVersion: String by project
val oneconfigWrapperVersion: String by project

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://repo.polyfrost.cc/releases")
    maven("https://repo.spongepowered.org/repository/maven-public")
    maven("https://repo.hypixel.net/repository/Hypixel/")
}

plugins {
    kotlin("jvm") version "1.7.10"

    val dgt = "2.29.0"
    id("dev.deftu.gradle.tools.java") version dgt
    id("dev.deftu.gradle.tools.minecraft.loom") version dgt
    id("dev.deftu.gradle.tools.bloom") version dgt
    id("dev.deftu.gradle.tools.shadow") version dgt

    id("io.github.juuxel.loom-quiltflower") version "1.7.3"
}

bloom {
    replacement("#version#", version)
}

dependencies {
    compileOnly("cc.polyfrost:oneconfig-1.8.9-forge:$oneconfigVersion")
    shade("cc.polyfrost:oneconfig-wrapper-launchwrapper:$oneconfigWrapperVersion")

    compileOnly("org.spongepowered:mixin:0.7.11-SNAPSHOT")
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")

    implementation("net.hypixel:mod-api:1.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

toolkitLoomHelper {
    disableRunConfigs(GameSide.SERVER)

    useTweaker("cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
    useCoreMod("at.yedel.yedelmod.launch.YedelModLoadingPlugin")
    useForgeMixin("yedelmod")

    useArgument("--version", "YedelMod", GameSide.BOTH)
    val resourcePackDir: String? = System.getenv("minecraft.resourcePackDir")
    if (!resourcePackDir.isNullOrBlank()) {
        println("Using resource pack directory $resourcePackDir from environment variable minecraft.resourcePackDir")
        useArgument("--resourcePackDir", resourcePackDir, GameSide.BOTH)
    }
}

sourceSets {
    val dummy by creating
    main {
        dummy.compileClasspath += compileClasspath
        compileClasspath += dummy.output
        output.setResourcesDir(java.classesDirectory)
    }
}

tasks {
    processResources {
        filesMatching(listOf("mcmod.info", "fabric.mod.json")) {
            expand("version" to version)
        }
        outputs.upToDateWhen { false }
    }

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


