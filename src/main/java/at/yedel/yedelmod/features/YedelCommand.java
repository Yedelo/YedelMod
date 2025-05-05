package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.ping.PingSender;
import at.yedel.yedelmod.launch.YedelModConstants;
import at.yedel.yedelmod.utils.Requests;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateSource;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UPacket;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.commands.annotations.*;
import com.google.gson.JsonObject;
import net.minecraft.event.HoverEvent;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static at.yedel.yedelmod.YedelMod.yedelog;
import static at.yedel.yedelmod.launch.YedelModConstants.yedelogo;



@Command(
    value = "yedel",
    aliases = "yedelmod",
    description = "The main command of YedelMod",
    chatColor = ChatColor.BLUE
)
public class YedelCommand {
    private static final YedelCommand INSTANCE = new YedelCommand();

    public static YedelCommand getInstance() {
        return INSTANCE;
    }

    private static final String FORMATTING_CODES =
        "§cC§6o§el§ao§9r §1c§5o§dd§be§3s§r:" + // "Color codes:" (in rainbow)
            "\n§8Black: §8&0     §4Dark Red: §4&4     §2Dark Green: §2&2     §1Dark Blue: §1&1" +
            "\n§3Dark Aqua: §3&3     §5Dark Purple: §5&5     §6Gold: §6&6     §7Gray: §7&7" +
            "\n§8Dark Gray: §8&8     §9Blue: §9&9     §aGreen: §a&a     §bAqua: §b&b" +
            "\n§cRed: §c&c     §dLight Purple: §d&d     §eYellow: §e&e     §fWhite: §f&f" +
            "\n" +
            "\n§lStyle §ncodes§r:" +
            "\n§kObfuscated§r: &k     §r§lBold: §l&l     §r§mStrikethrough: §m&m" +
            "\n§nUnderline: §n&n§r     §r§oItalic: §o&o    §rReset: §r&r";
    private static final UTextComponent FORMATTING_GUIDE_MESSAGE =
        new UTextComponent(yedelogo + " §e§nHover to view the formatting guide.").setHover(HoverEvent.Action.SHOW_TEXT, FORMATTING_CODES);

    private YedelCommand() {}

    @Main(
        description = "The main command, hosting all subcommands. When used with no arguments, opens the config screen."
    )
    public void main() {
        YedelConfig.getInstance().openGui();
    }

    @SubCommand(description = "Clears the currently set display text.")
    public void cleartext() {
        YedelConfig.getInstance().customTextHud.displayText = "";
        YedelConfig.getInstance().save();
        UChat.chat(yedelogo + " §eCleared display text!");
    }

    @SubCommand(description = "Shows mod constants and build information such as the project version.")
    public void constants() {
        try {
            UChat.chat(yedelogo + " §eConstants:");
            for (Field field : YedelModConstants.class.getDeclaredFields()) {
                // this makes a cool arrow
                // i can't really think of anything cleaner
                // - YedelMod -> MC_VERSION: 1.8.9
                UChat.chat(yedelogo + "§e> " + field.getName() + ": §r" + field.get(null));
            }
        }
        catch (IllegalAccessException e) {
            UChat.chat(yedelogo + " §cCouldn't get mod constants!");
            yedelog.error("Couldn't get mod constants!", e);

        }
    }

    @SubCommand(description = "Shows a formatting guide with color and style codes.")
    public void formatting() {
        UChat.chat(FORMATTING_GUIDE_MESSAGE);
    }

    @SubCommand(
        description = "Sends an illegal chat character, which disconnects you on most servers and sends you to limbo-like areas on some. No longer works on Hypixel, use /limbo instead.",
        aliases = "li"
    )
    public void limbo() {
        UChat.say("§");
    }

    @SubCommand(
        description = "Gives you creative mode in Hypixel's limbo, given certain checks are passed.",
        aliases = {"limbogmc", "lgmc"}
    )
    public void limbocreative() {
        LimboCreative.getInstance().checkLimbo();
    }

    @SubCommand(
        description = "Shows your total playtime (while playing on servers) in hours and minutes.",
        aliases = "pt"
    )
    public void playtime() {
        int minutes = YedelConfig.getInstance().playtimeMinutes;
        UChat.chat(yedelogo + " §ePlaytime: §6" + minutes / 60 + " hours §eand §6" + minutes % 60 + " minutes");
    }

    @SubCommand(description = "Sets your nick for Bounty Hunting to not select yourself as the target.")
    public void setnick(String nick) {
        UChat.chat("§6§l- BountyHunting - §eSet nick to " + nick + "§e!");
        YedelConfig.getInstance().currentNick = nick;
        YedelConfig.getInstance().save();
    }

    @SubCommand(description = "Sets the display text, supporting color codes with ampersands (&).")
    public void settext(@Greedy String text) {
        String displayText = UChat.addColor(text);
        YedelConfig.getInstance().customTextHud.displayText = displayText;
        YedelConfig.getInstance().save();
        UChat.chat(yedelogo + " §eSet displayed text to \"§r" + displayText + "§e\"!");
    }

    @SubCommand(description = "Sets the title of the game window.")
    public void settitle(@Greedy String title) {
        Display.setTitle(title);
        UChat.chat(yedelogo + " §eSet display title to \"§f" + title + "§e\"!");
    }

    @SubCommand(
        description = "Simulates a chat message, also supports color codes with ampersands (&).",
        aliases = "simc"
    )
    private void simulatechat(@Greedy String text) {
        String message = UChat.addColor(text);
        UPacket.sendChatMessage(new UTextComponent(message));
    }

    @SubCommand(
        description = "Shows messages from me about the mod. These can be anything from tips to bug notices.",
        aliases = "message"
    )
    public void yedelmessage() {
        new Thread(() -> {
            try {
                JsonObject messageObject =
                    Requests.getJsonObject(new URL("https://yedelo.github.io/yedelmod.json")).getAsJsonObject();
                String yedelMessage = messageObject.get("yedelmod-message-formatted").getAsString();

                String lastUpdatedTimeString = "?";

                try {
                    long lastUpdatedTime = messageObject.get("last-updated-time").getAsLong();

                    ZonedDateTime dateTime =
                        ZonedDateTime.ofInstant(Instant.ofEpochSecond(lastUpdatedTime), ZoneId.systemDefault());
                    Locale userLocale = Locale.getDefault();
                    DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy h:mm a z", userLocale);
                    lastUpdatedTimeString = dateTime.format(formatter);
                }
                catch (IllegalStateException e) {
                    yedelog.error("Couldn't get last updatted date/time", e);
                }

                UChat.chat(yedelogo + " §eMessage from Yedel (last updated §f" + lastUpdatedTimeString + "§e):");
                UChat.chat(yedelMessage);
            }
            catch (IOException e) {
                UChat.chat(yedelogo + " §cCouldn't get mod message!");
                e.printStackTrace();
            }
        }, "YedelMod Message"
        ).start();
    }

    @SubCommandGroup("ping")
    public static class Ping {
        @Main
        public void main() {
            PingSender.getInstance().defaultMethodPing();
        }

        @SubCommand(description = "Does /ping command. Works on very few servers.", aliases = "p")
        public void ping() {
            PingSender.getInstance().pingPing();
        }

        @SubCommand(
            description = "Enters a random command and waits for the unknown command response. Works on almost all servers.",
            aliases = "c"
        )
        public void command() {
            PingSender.getInstance().commandPing();
        }

        @SubCommand(
            description = "Sends a tab completion packet and waits for the response. Works on all servers.",
            aliases = "t"
        )
        public void tab() {
            PingSender.getInstance().tabPing();
        }

        @SubCommand(
            description = "Sends a statistics packet and waits for the response. Works on all servers.",
            aliases = "s"
        )
        public void stats() {
            PingSender.getInstance().statsPing();
        }

        @SubCommand(
            description = "Gets the ping displayed previously on the server list. Doesn't work on singleplayer or if you used Direct Connect.",
            aliases = "l"
        )
        public void list() {
            PingSender.getInstance().serverListPing();
        }

        @SubCommand(
            description = "Uses the Hypixel ping packet and waits for the response. Only works on Hypixel.",
            aliases = "h"
        )
        public void hypixel() {
            PingSender.getInstance().hypixelPing();
        }
    }

    @SubCommandGroup("update")
    public static class Update {
        @Main
        public void main() {
            UpdateManager.getInstance().checkForUpdates(YedelConfig.getInstance().getUpdateSource(), UpdateManager.FeedbackMethod.CHAT);
        }

        @SubCommand
        public void modrinth() {
            UpdateManager.getInstance().checkForUpdates(UpdateSource.MODRINTH, UpdateManager.FeedbackMethod.CHAT);
        }

        @SubCommand
        public void github() {
            UpdateManager.getInstance().checkForUpdates(UpdateSource.GITHUB, UpdateManager.FeedbackMethod.CHAT);
        }
    }
}
