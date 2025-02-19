package at.yedel.yedelmod.utils.update;



import java.net.URI;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;



public enum UpdateSource {
	MODRINTH(
		"§aModrinth",
		"Modrinth",
		new ChatComponentText("§aModrinth").setChatStyle(
			new ChatStyle().setChatClickEvent(
				new ClickEvent(
					Action.OPEN_URL,
					"https://modrinth.com/mod/yedelmod/versions"
				)
			)
		),
		URI.create("https://modrinth.com/mod/yedelmod/versions")
	),
	GITHUB(
		"§9GitHub",
		"GitHub",
		new ChatComponentText("§9GitHub").setChatStyle(
			new ChatStyle().setChatClickEvent(
				new ClickEvent(
					Action.OPEN_URL,
					"https://github.com/Yedelo/YedelMod/releases"
				)
			)
		),
		URI.create("https://github.com/Yedelo/YedelMod/releases")
	);
	public final String name;
	public final String seriousName;
	public final IChatComponent chatComponent;
	public final URI uri;

	UpdateSource(String notificationString, String seriousName, IChatComponent chatComponent, URI uri) {
		this.name = notificationString;
		this.seriousName = seriousName;
		this.chatComponent = chatComponent;
		this.uri = uri;
	}
}
