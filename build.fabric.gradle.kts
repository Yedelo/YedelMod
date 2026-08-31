import org.gradle.api.tasks.Copy
import org.gradle.internal.Actions.set
import org.gradle.kotlin.dsl.invoke
import kotlin.reflect.KProperty

// in stonecutter.gradle.kts
class CommonProperty<T> {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = (rootProject.extra[sc.current.project] as Map<String, Any?>)[property.name] as T
}
val modName by CommonProperty<String>()
val modId by CommonProperty<String>()
val modDescription by CommonProperty<String>()
val modIcon by CommonProperty<String>()
val fabricLoaderVersion by CommonProperty<String>()
val oneconfigVersion by CommonProperty<String>()
val hypixelModApiVersion by CommonProperty<String>()
val rangedVersion by CommonProperty<Boolean>()
val maxMc by CommonProperty<String?>()
val finalFileName by CommonProperty<String>()
val license: String by project
val javaVersion = JavaVersion.VERSION_25
val fabricApiVersion = sc.properties.getAs<String>("versions.fabricapi")
val modMenuVersion = sc.properties.getAs<String>("versions.modmenu")

repositories {
    fun scopedMaven(url: String, vararg groups: String, includeSubgroups: Boolean = false) = maven(url) {
        content { for (group in groups) if (!includeSubgroups) includeGroup(group) else includeGroupAndSubgroups(group) }
    }

    mavenCentral()
    gradlePluginPortal()
    google()
    maven("https://repo.polyfrost.cc/releases")
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://repo.hypixel.net/repository/Hypixel/")
    maven("https://maven.fabricmc.net/releases")
    scopedMaven("https://central.sonatype.com/repository/maven-snapshots/", "net.kyori")
    maven("https://api.modrinth.com/maven") {
        content { includeGroup("maven.modrinth") }
    }
}

plugins {
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT"
    id("dev.deftu.gradle.tools.bloom") version "2.73.0"
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    implementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    implementation("org.polyfrost.oneconfig:${sc.current.version}-fabric:$oneconfigVersion")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    // oneconfig provides hypixel mod api for now
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

bloom {
    replacement("@MC_VERSION@", sc.current.version)
    replacement("@MOD_LOADER@", "fabric")
    replacement("@FORMATTED_MOD_LOADER@", "Fabric")
}

tasks {
    processResources {
        fun MutableMap<String, String>.register(key: String, value: String) {
            inputs.property(key, value)
            set(key, value)
        }
        fun target(version: String) = ">=$version"

        exclude("mcmod.info", "yedelmod_keystore.jks")
        exclude("mixins.legacy.$modId.json")
        val props = buildMap {
            register("modName", modName)
            register("modId", modId)
            register("modDescription", modDescription)
            register("modIcon", modIcon)
            register("license", license)
            register("version", version.toString())
            register("java", target(javaVersion.majorVersion))
            register("fabricLoader", target(fabricLoaderVersion))
            val minecraftDependency =
                if (rangedVersion) ">=${sc.current.version} <=${maxMc}" else sc.current.version
            register("minecraft", minecraftDependency)
            register("oneconfigv1", target(oneconfigVersion))
            register("hypixelmodapi", target(hypixelModApiVersion))
            register("mixinJava", "JAVA_${javaVersion.majorVersion}")
            register("mixinMin", "0.8")
        }
        filesMatching(listOf("fabric.mod.json", "mixins.modern.$modId.json")) { expand(props) }

        outputs.upToDateWhen { false }
    }

    register<Copy>("buildAndCollect") {
        group = "build"

        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs"))
        dependsOn("build")
    }

    jar {
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