package at.yedel.yedelmod.utils.update;



import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;

import java.net.URI;



public enum UpdateSource {
	MODRINTH(
		"§a§nModrinth",
		URI.create("https://modrinth.com/mod/yedelmod/versions")
	),
	GITHUB(
		"§9§nGitHub",
		URI.create("https://github.com/Yedelo/YedelMod/releases")
	);
	public final String coloredName;
	public final String name;
	public final URI uri;

	UpdateSource(String coloredName, URI uri) {
		this.coloredName = coloredName;
		this.name = UTextComponent.Companion.stripFormatting(coloredName);
		this.uri = uri;
	}
}
