package at.yedel.yedelmod.utils.update;



import dev.deftu.textile.minecraft.MCTextFormat;

import java.net.URI;



public enum UpdateSource {
	MODRINTH(
		"§a§nModrinth",
		"https://modrinth.com/mod/yedelmod/versions"
	),
	GITHUB(
		"§9§nGitHub",
		"https://github.com/Yedelo/YedelMod/releases"
	);
	public final String coloredName;
	public final String name;
	public final URI uri;

	UpdateSource(String coloredName, String uri) {
		this.coloredName = coloredName;
		this.name = MCTextFormat.strip(coloredName);
		this.uri = URI.create(uri);
	}
}
