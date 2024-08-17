import dev.architectury.pack200.java.Pack200Adapter
import java.nio.file.Files
import java.nio.file.Path


plugins {
    kotlin("jvm")
    id("gg.essential.loom")
    id("dev.architectury.architectury-pack200")
    id("net.kyori.blossom") version "1.3.1"
}

version = properties["mod_version"]!!
val essentialVersion: String by project

val embed: Configuration by configurations.creating
configurations.implementation.get().extendsFrom(embed)

repositories {
    maven("https://repo.essential.gg/repository/maven-public")
    maven("https://repo.spongepowered.org/repository/maven-public")
    maven("https://repo.hypixel.net/repository/Hypixel/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    compileOnly("gg.essential:essential-1.8.9-forge:$essentialVersion")
    embed("gg.essential:loader-launchwrapper:1.1.3")

    compileOnly("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")

    implementation("net.hypixel:mod-api:1.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

blossom {
    replaceTokenIn("src/main/java/at/yedel/yedelmod/YedelMod.java")
    replaceToken("#version#", version)
}

loom {
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
        }
    }

    launchConfigs {
        getByName("client") {
            property(
                "fml.coreMods.load",
                "at.yedel.yedelmod.launch.YedelModLoadingPlugin"
            )
            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
            arg("--mixin", "mixins.yedelmod.json")
            arg("--version", "YedelMod") // UnknownFMLProfile looks pretty bad so replacing it
            // this is just for me to used shared resource packs with other instances
            if (Files.exists(Path.of("../../../Desktop/resourcepackfolder"))) {
                arg(
                    "--resourcePackDir",
                    "../../../../Desktop/resourcepackfolder"
                )
            }
        }
    }

    forge {
        pack200Provider.set(Pack200Adapter())
        mixinConfig("mixins.yedelmod.json")
    }
}

tasks {
    processResources {
        filesMatching("mcmod.info") {
            expand("version" to project.version)
        }
    }

    withType<JavaCompile> {
        options.release.set(8)
    }

    jar {
        from(embed.files.map { zipTree(it) })

        manifest.attributes(
            mapOf(
                "FMLCorePlugin" to "at.yedel.yedelmod.launch.YedelModLoadingPlugin",
                "FMLCorePluginContainsFMLMod" to "fml core plugin does contain an fml mod",
                "ForceLoadAsMod" to "true",
                "Main-Class" to "at.yedel.yedelmod.launch.YedelModWindow",
                "MixinConfigs" to "mixins.yedelmod.json",
                "ModSide" to "CLIENT",
                "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker"
            )
        )
    }

    test {
        useJUnitPlatform()
    }
}


