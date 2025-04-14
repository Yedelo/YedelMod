package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.ping.PingSender;
import at.yedel.yedelmod.hud.CustomTextHud;
import at.yedel.yedelmod.launch.YedelModConstants;
import at.yedel.yedelmod.utils.Requests;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateSource;
import com.google.gson.JsonObject;
import dev.deftu.omnicore.client.OmniChat;
import dev.deftu.omnicore.client.OmniClient;
import dev.deftu.textile.minecraft.MCHoverEvent;
import dev.deftu.textile.minecraft.MCSimpleTextHolder;
import dev.deftu.textile.minecraft.MCTextFormat;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.oneconfig.utils.v1.dsl.ScreensKt;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static at.yedel.yedelmod.YedelMod.yedelog;
import static at.yedel.yedelmod.launch.YedelModConstants.logo;



@Command(
    value = {"yedel", YedelModConstants.MOD_ID},
    description = "The main command of YedelMod"
)
public class YedelCommand {
    private YedelCommand() {}

    private static final YedelCommand instance = new YedelCommand();

    public static YedelCommand getInstance() {
        return instance;
    }

    private static final String formattingCodes =
        "§cC§6o§el§ao§9r §1c§5o§dd§be§3s§r:" + // "Color codes:" (in rainbow)
            "\n§8Black: §8&0     §4Dark Red: §4&4     §2Dark Green: §2&2     §1Dark Blue: §1&1" +
            "\n§3Dark Aqua: §3&3     §5Dark Purple: §5&5     §6Gold: §6&6     §7Gray: §7&7" +
            "\n§8Dark Gray: §8&8     §9Blue: §9&9     §aGreen: §a&a     §bAqua: §b&b" +
            "\n§cRed: §c&c     §dLight Purple: §d&d     §eYellow: §e&e     §fWhite: §f&f" +
            "\n" +
            "\n§lStyle §ncodes§r:" +
            "\n§kObfuscated§r: &k     §r§lBold: §l&l     §r§mStrikethrough: §m&m" +
            "\n§nUnderline: §n&n§r     §r§oItalic: §o&o    §rReset: §r&r";
    private static final MCSimpleTextHolder formattingGuideMessage =
        new MCSimpleTextHolder(logo + " §e§nHover to view the formatting guide.") {}
            .withHoverEvent(new MCHoverEvent.ShowText(formattingCodes));

    @Command(
        description = "The main command, hosting all Commands. When used with no arguments, opens the config screen."
    )
    public void main() {
        ScreensKt.openUI(YedelConfig.getInstance());
    }

    @Command(description = "Clears the currently set display text.")
    public void cleartext() {
        CustomTextHud.getInstance().displayText = "";
        CustomTextHud.getInstance().save();
        OmniChat.displayClientMessage(logo + " §eCleared display text!");
    }

    @Command(description = "Shows a formatting guide with color and style codes.")
    public void formatting() {
        OmniChat.displayClientMessage(formattingGuideMessage);
    }

    @Command(
        description = "Sends an illegal chat character, which disconnects you on most servers and sends you to limbo-like areas on some. No longer works on Hypixel, use /limbo instead.",
        value = "li"
    )
    public void limbo() {
        OmniChat.sendPlayerMessage("§");
    }

    @Command(
        description = "Gives you creative mode in Hypixel's limbo, given certain checks are passed.",
        value = {"limbogmc", "lgmc"}
    )
    public void limbocreative() {
        LimboCreativeCheck.getInstance().checkLimbo();
    }

    @Command("ping")
    public static class Ping {
        @Command
        public void main() {
            PingSender.getInstance().defaultMethodPing();
        }

        @Command(description = "Does /ping command. Works on very few servers.", value = "p")
        public void ping() {
            PingSender.getInstance().pingPing();
        }

        @Command(
            description = "Enters a random command and waits for the unknown command response. Works on almost all servers.",
            value = "c"
        )
        public void command() {
            PingSender.getInstance().commandPing();
        }

        @Command(
            description = "Sends a tab completion packet and waits for the response. Works on all servers.",
            value = "t"
        )
        public void tab() {
            PingSender.getInstance().tabPing();
        }

        @Command(
            description = "Sends a statistics packet and waits for the response. Works on all servers.",
            value = "s"
        )
        public void stats() {
            PingSender.getInstance().statsPing();
        }

        @Command(
            description = "Gets the ping displayed previously on the server list. Doesn't work on singleplayer or if you used Direct Connect.",
            value = "l"
        )
        public void list() {
            PingSender.getInstance().serverListPing();
        }

        @Command(
            description = "Uses the Hypixel ping packet and waits for the response. Only works on Hypixel.",
            value = "h"
        )
        public void hypixel() {
            PingSender.getInstance().hypixelPing();
        }
    }

    @Command(
        description = "Shows your total playtime (while playing on servers) in hours and minutes.",
        value = "pt"
    )
    public void playtime() {
        int minutes = YedelConfig.getInstance().playtimeMinutes;
        OmniChat.displayClientMessage(logo + " §ePlaytime: §6" + minutes / 60 + " hours §eand §6" + minutes % 60 + " minutes");
    }

    @Command(description = "Sets your nick for Bounty Hunting to not select yourself as the target.")
    public void setnick(String nick) {
        OmniChat.displayClientMessage("§6§l- BountyHunting - §eSet nick to " + nick + "§e!");
        YedelConfig.getInstance().currentNick = nick;
        YedelConfig.getInstance().save();
    }

    @Command(description = "Sets the display text, supporting color codes with ampersands (&).")
    public void settext(String[] text) {
        String displayText = MCTextFormat.translateAlternateColorCodes('&', joinArgs(text));
        CustomTextHud.getInstance().displayText = displayText;
        CustomTextHud.getInstance().save();
        OmniChat.displayClientMessage(logo + " §eSet displayed text to \"§r" + displayText + "§e\"!");
    }

    @Command(description = "Sets the title of the game window.")
    public void settitle(String[] title) {
        String displayTitle = joinArgs(title);
        Display.setTitle(displayTitle);
        OmniChat.displayClientMessage(logo + " §eSet display title to \"§f" + displayTitle + "§e\"!");
    }

    @Command(
        description = "Simulates a chat message, also supports color codes with ampersands (&).",
        value = "simc"
    )
    private void simulatechat(String[] text) {
        // js pipe functions would be really nice here
        // text |> joinArgs |> TextUtils.format |> new ChatComponentText |> new S02PacketChat |> OmniClient.getInstance().getNetHandler().handleChat;
        OmniClient.getInstance().getNetHandler().handleChat(new S02PacketChat(new ChatComponentText((MCTextFormat.translateAlternateColorCodes('&', joinArgs(text))))));
    }

    @Command("update")
    public static class Update {
        @Command
        public void main() {
            UpdateManager.getInstance().checkForUpdates(YedelConfig.getInstance().getUpdateSource(), UpdateManager.FeedbackMethod.CHAT);
        }

        @Command
        public void modrinth() {
            UpdateManager.getInstance().checkForUpdates(UpdateSource.MODRINTH, UpdateManager.FeedbackMethod.CHAT);
        }

        @Command
        public void github() {
            UpdateManager.getInstance().checkForUpdates(UpdateSource.GITHUB, UpdateManager.FeedbackMethod.CHAT);
        }
    }

    @Command(
        description = "Shows messages from me about the mod. These can be anything from tips to bug notices.",
        value = "message"
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

                OmniChat.displayClientMessage(logo + " §eMessage from Yedel (last updated §f" + lastUpdatedTimeString + "§e):");
                OmniChat.displayClientMessage(yedelMessage);
            }
            catch (IOException e) {
                OmniChat.displayClientMessage(logo + " §cCouldn't get mod message!");
                e.printStackTrace();
            }
        }, "YedelMod Message"
        ).start();
    }

    private String joinArgs(String[] args) {
        return String.join(" ", args);
    }
}
