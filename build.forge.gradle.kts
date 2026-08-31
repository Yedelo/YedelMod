import dev.deftu.gradle.utils.GameSide
import org.gradle.api.tasks.Copy
import org.gradle.internal.Actions.set
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.java
import kotlin.reflect.KProperty

// in stonecutter.gradle.kts
class CommonProperty<T> {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = (rootProject.extra[sc.current.project] as Map<String, Any?>)[property.name] as T
}
val modName by CommonProperty<String>()
val modId by CommonProperty<String>()
val modDescription by CommonProperty<String>()
val modIcon by CommonProperty<String>()
val oneconfigVersion by CommonProperty<String>()
val hypixelModApiVersion by CommonProperty<String>()
val rangedVersion by CommonProperty<Boolean>()
val maxMc by CommonProperty<String?>()
val finalFileName by CommonProperty<String>()
val license: String by project
val javaVersion = JavaVersion.VERSION_1_8

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
        "resources",
        "shadow"
    )) id("dev.deftu.gradle.tools.$tool") version dgt
}

val shadeOptionally = configurations.create("shadeOptionally")
configurations.named("implementation") {
    extendsFrom(shadeOptionally)
}

dependencies {
    shadeOptionally("cc.polyfrost:oneconfig-wrapper-launchwrapper:${sc.properties.getAs<String>("versions.oneconfigwrapper")}")
    compileOnly("cc.polyfrost:oneconfig-${mcData.version}-${mcData.loader}:$oneconfigVersion")
    compileOnly("org.spongepowered:mixin:0.7.11-SNAPSHOT")

    modImplementation("net.hypixel:mod-api-forge:$hypixelModApiVersion")
    shadeOptionally("net.hypixel:mod-api-forge-tweaker:$hypixelModApiVersion")
}

toolkitLoomHelper {
    disableRunConfigs(GameSide.SERVER)

    useTweaker("at.yedel.yedelmod.launch.YedelModTweaker")
    useForgeMixin("legacy.$modId")
    useMixinRefMap("legacy.$modId.refmap")

    useDevAuth(sc.properties.getAs<String>("versions.devauth"))
    useArgument("--version", modName, GameSide.BOTH)
    val resourcePackDir: String? = System.getenv("minecraft.resourcePackDir")
    if (!resourcePackDir.isNullOrBlank()) {
        println("Using resource pack directory $resourcePackDir from environment variable minecraft.resourcePackDir")
        useArgument("--resourcePackDir", resourcePackDir, GameSide.BOTH)
    }
}

tasks {
    processResources {
        fun MutableMap<String, String>.register(key: String, value: String) {
            inputs.property(key, value)
            set(key, value)
        }
        fun target(version: String) = ">=$version"

        exclude("fabric.mod.json")
        exclude("mixins.modern.$modId.json")
        val props = buildMap {
            register("modName", modName)
            register("modId", modId)
            register("modDescription", modDescription)
            register("modIcon", modIcon)
            register("mcVersion", sc.current.version)
            register("version", version.toString())
            register("java", target(javaVersion.majorVersion))
            register("mixinJava", "JAVA_${javaVersion.majorVersion}")
            register("mixinMin", "0.7.11")
        }
        filesMatching(listOf("mcmod.info", "mixins.legacy.$modId.json")) { expand(props) }

        outputs.upToDateWhen { false }
    }

    register<Copy>("buildAndCollect") {
        group = "build"

        from(remapJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs"))
        dependsOn("build")
    }

    remapJar {
        archiveFileName = finalFileName
        manifest.attributes(
            mapOf(
                "Main-Class" to "at.yedel.yedelmod.launch.YedelModWindow",
                "ModSide" to "CLIENT",
            )
        )
    }
    fatJar {
        configurations = listOf(shadeOptionally)
        relocate("net.hypixel.modapi.tweaker", "at.yedel.$modId.launch")
    }
}