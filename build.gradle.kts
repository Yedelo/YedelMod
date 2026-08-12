val modDescription: String by project
val license: String by project
val fabricLoaderVersion = sc.properties.get<String>("versions.fabricloader")
val oneconfigVersion = sc.properties.get<String>("versions.oneconfig")
val fabricApiVersion = sc.properties.get<String>("versions.fabricapi")
val modMenuVersion = sc.properties.get<String>("versions.modmenu")
val javaVersion = JavaVersion.VERSION_25

val loader = sc.current.project.split("-")[1]
val rangedVersion = sc.properties.get<String>("versioning") == "range"
val maxMc = if (rangedVersion) sc.properties.get<String>("mc.max") else null

repositories {
    fun scopedMaven(url: String, vararg groups: String, includeSubgroups: Boolean = false) = maven(url) {
        content { for (group in groups) if (!includeSubgroups) includeGroup(group) else includeGroupAndSubgroups(group) }
    }

    mavenCentral()
    gradlePluginPortal()
    google()
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://repo.hypixel.net/repository/Hypixel/")
    maven("https://maven.fabricmc.net/releases")
    scopedMaven("https://central.sonatype.com/repository/maven-snapshots/", "net.kyori")
}

plugins {
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT"
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    implementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    implementation("org.polyfrost.oneconfig:${sc.current.version}-fabric:$oneconfigVersion")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
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

tasks {
    processResources {
        fun MutableMap<String, String>.register(key: String, value: String) {
            inputs.property(key, value)
            set(key, value)
        }

        fun target(version: String) = ">=$version"
        val props = buildMap {
            register("description", modDescription)
            register("license", license)
            register("version", version.toString())
            register("java", target(javaVersion.majorVersion))
            register("fabricLoader", target(fabricLoaderVersion))
            val minecraftDependency =
                if (rangedVersion) ">=${sc.current.version} <=${maxMc}" else sc.current.version
            register("minecraft", minecraftDependency)
            register("oneconfigv1", target(oneconfigVersion))
        }
        filesMatching(listOf("fabric.mod.json")) { expand(props) }

        val mixinJava = "JAVA_${javaVersion.majorVersion}"
        filesMatching("mixins.yedelmod.json") { expand("mixinJava" to mixinJava) }

        outputs.upToDateWhen { false }
    }

    register<Copy>("buildAndCollect") {
        group = "build"

        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs"))
        dependsOn("build")
    }

    jar {
        val minecraftTarget = if (rangedVersion) "${sc.current.version}-$maxMc" else sc.current.version
        val finalFileName = "YedelMod-$version+$minecraftTarget-$loader.jar"
        archiveFileName = finalFileName
        manifest.attributes(
            mapOf(
                "Main-Class" to "at.yedel.yedelmod.launch.YedelModWindow"
            )
        )
    }
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

