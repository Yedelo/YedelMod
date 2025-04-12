import dev.deftu.gradle.utils.GameSide

val oneconfigVersion: String by project
val oneconfigWrapperVersion: String by project
val devAuthVersion: String by project

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://repo.polyfrost.cc/releases")
    maven("https://repo.spongepowered.org/repository/maven-public")
    maven("https://repo.hypixel.net/repository/Hypixel/")
}

plugins {
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    for (tool in listOf(
        "java",
        "minecraft.loom",
        "bloom",
        "ducks",
        "resources",
        "shadow"
    )) id("dev.deftu.gradle.tools.$tool")
}

dependencies {
    compileOnly("cc.polyfrost:oneconfig-${mcData.version}-${mcData.loader}:$oneconfigVersion")
    listOf(
        "implementation",
        "shade"
    ).forEach { it("cc.polyfrost:oneconfig-wrapper-launchwrapper:$oneconfigWrapperVersion") }

    compileOnly("org.spongepowered:mixin:0.7.11-SNAPSHOT")

    implementation("net.hypixel:mod-api:1.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

toolkitLoomHelper {
    disableRunConfigs(GameSide.SERVER)

    // Remove this in the run config if you're using other OneConfig mods, since those mods will not load with the tweaker argument.
    useTweaker("cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")

    if (mcData.isLegacyForge) {
        useCoreMod("at.yedel.yedelmod.launch.YedelModLoadingPlugin")
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


