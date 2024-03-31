package at.yedel.yedelmod.update;



import java.io.IOException;

import at.yedel.yedelmod.config.YedelConfig;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.SubCommand;



public class CheckForUpdatesCommand extends Command {
    public CheckForUpdatesCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle() throws IOException {
        UpdateManager.instance.checkVersion(YedelConfig.getUpdateSource(), "chat");
    }

    @SubCommand(value = "modrinth", description = "Searches Modrinth for a mod update.")
    public void handleModrinth() {
        UpdateManager.instance.checkVersion(UpdateSource.MODRINTH, "chat");
    }

    @SubCommand(value = "github", description = "Searches GitHub for a mod update.")
    public void handleGithub() {
        UpdateManager.instance.checkVersion(UpdateSource.GITHUB, "chat");
    }
}
