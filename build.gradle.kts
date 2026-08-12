val modDescription: String by project
val license: String by project
val fabricLoaderVersion: String by project
val oneconfigVersion: String by project
val fabricApiVersion: String by project
val modMenuVersion: String by project

val javaVersion = JavaVersion.VERSION_25

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://repo.hypixel.net/repository/Hypixel/")
    maven("https://maven.fabricmc.net/releases")
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
            register("minecraft", sc.properties.get<String>("minecraftDependency"))
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
        archiveFileName = "YedelMod-$version+${sc.current.project}.jar"
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

