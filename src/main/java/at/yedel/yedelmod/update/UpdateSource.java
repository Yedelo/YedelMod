package at.yedel.yedelmod.update;



import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;



public enum UpdateSource {
    MODRINTH(
            "Modrinth",
            "https://api.modrinth.com/v2/project/yedelmod/version",
            UpdateManager.getModrinthLink(),
            (ChatComponentText) new ChatComponentText("§2Modrinth!").setChatStyle(
                    new ChatStyle().setChatClickEvent(
                            new ClickEvent(Action.OPEN_URL, UpdateManager.getModrinthLink())
                    )
            )
    ),
    GITHUB(
            "GitHub",
            "https://api.github.com/repos/Yedelo/YedelMod/releases/latest",
            UpdateManager.getGithubLink(),
            (ChatComponentText) new ChatComponentText("§9GitHub!").setChatStyle(
                    new ChatStyle().setChatClickEvent(
                            new ClickEvent(Action.OPEN_URL, UpdateManager.getGithubLink())
                    )
            )

    );

    public final String name;
    public final String apiLink;
    public final String link;
    public final ChatComponentText msg;

    UpdateSource(String name, String apiLink, String link, ChatComponentText msg) {
        this.name = name;
        this.apiLink = apiLink;
        this.link = link;
        this.msg = msg;
    }
}
