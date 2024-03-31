package at.yedel.yedelmod.commands;



import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.Greedy;
import gg.essential.universal.UChat;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class SimulateChatCommand extends Command {
    public SimulateChatCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle(@Greedy String message) {
        minecraft.getNetHandler().handleChat(new S02PacketChat(new ChatComponentText(message), (byte) 1));
    }
}
