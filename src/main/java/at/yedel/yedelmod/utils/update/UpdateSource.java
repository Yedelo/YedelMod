/*? if forge {*//*
package at.yedel.yedelmod.utils.update;



import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;



public enum UpdateSource {
    MODRINTH("§a§nModrinth"),
    GITHUB("§9§nGitHub");

    public final String coloredName;
    public final String name;

    UpdateSource(String coloredName) {
        this.coloredName = coloredName;
        this.name = UTextComponent.Companion.stripFormatting(coloredName);
    }
}
*//*?}*/