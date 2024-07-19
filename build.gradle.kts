import dev.architectury.pack200.java.Pack200Adapter


plugins {
    kotlin("jvm")
    id("gg.essential.loom")
    id("io.github.juuxel.loom-quiltflower")
    id("dev.architectury.architectury-pack200")
    id("com.github.johnrengelman.shadow")
}

group = "at.yedel"
version = "1.1.0"

loom {
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
        }
    }

    launchConfigs {
        getByName("client") {
            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
            arg("--mixin", "mixins.yedelmod.json")
        }
    }

    forge {
        pack200Provider.set(Pack200Adapter())
        mixinConfig("mixins.yedelmod.json")
    }
}

val embed: Configuration by configurations.creating
configurations.implementation.get().extendsFrom(embed)

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    compileOnly("gg.essential:essential-1.8.9-forge:4955+g395141645")
    embed("gg.essential:loader-launchwrapper:1.1.3")

    compileOnly("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

repositories {
    maven("https://repo.essential.gg/repository/maven-public")
    maven("https://repo.spongepowered.org/repository/maven-public")
}

tasks {
    jar {
        from(embed.files.map { zipTree(it) })

        manifest.attributes(
            mapOf(
                "FMLCorePluginContainsCorePlugin" to "Yes, yes it does",
                "ForceLoadAsMod" to "true",
                "Main-Class" to "at.yedel.yedelmod.loader.YedelModWindow",
                "MixinConfigs" to "mixins.yedelmod.json",
                "ModSide" to "CLIENT",
                "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker"
            )
        )
    }

    processResources {
        inputs.property("version", project.version)
        inputs.property("mcversion", "1.8.9")
        filesMatching("mcmod.info") {
            expand("version" to project.version)
        }
    }

    withType<JavaCompile> {
        options.release.set(8)
    }
}

tasks.test {
    useJUnitPlatform()
}
