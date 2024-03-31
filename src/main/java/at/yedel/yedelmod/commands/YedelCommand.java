package at.yedel.yedelmod.commands;



import java.util.Objects;

import at.yedel.yedelmod.config.YedelConfig;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.utils.GuiUtil;



public class YedelCommand extends Command {
    public YedelCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle() {
        GuiUtil.open(Objects.requireNonNull(YedelConfig.instance.gui()));
    }
}
