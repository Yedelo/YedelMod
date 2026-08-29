import kotlin.reflect.KProperty

plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.1-fabric"

stonecutter parameters {
    val loader = current.project.split("-")[1]
    val v0 = properties.getAs<String>("versions.oneconfig").startsWith("0")
    val v1 = !v0
    val legacy = current.parsed <= "1.8.9"
    val modern = !legacy

    constants {
        match(loader, "forge", "fabric")
        this["v0"] = v0
        this["v1"] = v1
        this["legacy"] = legacy
        this["modern"] = modern
    }

    replacements {
        string(v1, "config_bridge") {
            replace("name =", "title =")
            replace("allowAlpha", "alpha")
            replace("OneColor", "PolyColor")
            replace("getRGB", "getArgb")
            replace("@KeyBind", "@Keybind")
            replace("OneKeyBind", "OneConfigKeybind")
        }

        string(v1, "texthud_bridge") {
            replace("SingleTextHud", "TextHud")
            replace("protected String getText(boolean example)", "protected String getText()")
            replace("if (example)", "if (!isReal() || HudManager.INSTANCE.isEditing())")
        }

        string(v1, "command_bridge") {
            replace("@SubCommand", "@Handler")
            replace("aliases = ", "value =")
        }

        string(v1) {
            replace("UChat.chat", "Platform.compatibility().displayChatMessage")
            replace("ReceiveChatEvent", "ChatEvent.Receive")
            replace("UTextComponent.Companion.stripFormatting(event.message.getUnformattedText())", "event.getFullyUnformattedMessage()")
        }
    }

    val shared = mutableMapOf<String, Any?>()
    extra[current.project] = shared

    class Declare<T>(private val value: T) {
        operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): Declare<T> {
            shared[property.name] = value
            return this
        }

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    }
    val modName by Declare(extra["mod.name"])
    val modId by Declare(extra["mod.id"])
    val modDescription by Declare(extra["mod.description"])
    val modIcon by Declare(extra["modIcon"])
    val fabricLoaderVersion by Declare(properties.getAs<String>("versions.fabricloader"))
    val oneconfigVersion by Declare(properties.getAs<String>("versions.oneconfig"))
    val rangedVersion by Declare(properties.getAs<String>("versioning") == "range")
    val maxMc by Declare(if (rangedVersion) properties.getAs<String>("mc.max") else null)
    val minecraftTarget by Declare(if (rangedVersion) "${current.version}-$maxMc" else current.version)
    val finalFileName by Declare("$modName-$version+$minecraftTarget-$loader.jar")
}