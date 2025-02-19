package at.yedel.yedelmod.utils.update;



import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import net.minecraft.event.ClickEvent.Action;

import java.net.URI;



public enum UpdateSource {
	MODRINTH(
		"§aModrinth",
		"Modrinth",
        new UTextComponent("§aModrinth").setClick(Action.OPEN_URL, "https://modrinth.com/mod/yedelmod/versions"),
		URI.create("https://modrinth.com/mod/yedelmod/versions")
	),
	GITHUB(
		"§9GitHub",
		"GitHub",
        new UTextComponent("§9GitHub").setClick(Action.OPEN_URL, "https://github.com/Yedelo/YedelMod/releases"),
		URI.create("https://github.com/Yedelo/YedelMod/releases")
	);
	public final String name;
	public final String seriousName;
    public final UTextComponent textComponent;
	public final URI uri;

    UpdateSource(String notificationString, String seriousName, UTextComponent textComponent, URI uri) {
		this.name = notificationString;
		this.seriousName = seriousName;
        this.textComponent = textComponent;
		this.uri = uri;
	}
}
