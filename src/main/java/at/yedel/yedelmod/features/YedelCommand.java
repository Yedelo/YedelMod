package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.ping.PingCommandInterface;
import at.yedel.yedelmod.features.ping.PingMethod;
import at.yedel.yedelmod.hud.CustomTextHud;
import at.yedel.yedelmod.launch.YedelModConstants;
import at.yedel.yedelmod.utils.Requests;
import at.yedel.yedelmod.utils.TextUtils;
//? if v0 {
/*import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.commands.annotations.*;
    *///?} else {
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Handler;
import org.polyfrost.oneconfig.api.hud.v1.HudManager;
import org.polyfrost.oneconfig.api.platform.v1.Platform;
//?}
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
//? if legacy {
/*import net.minecraft.event.HoverEvent;
import org.lwjgl.opengl.Display;
*///?}


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
    //? if v0
    //value = "yedel", aliases = "yedelmod",
    //? else
    value = {"yedel", "yedelmod"},
    description = "The main command of YedelMod"
)
    //~ command_bridge
public class YedelCommand {
    private static final YedelCommand INSTANCE = new YedelCommand();

    public static YedelCommand getInstance() {
        return INSTANCE;
    }

    //~ if v1 'String FORMATTING_CODES = ' -> 'Component FORMATTING_CODES = Component.text'
    private static final Component FORMATTING_CODES = Component.text(
        "§cC§6o§el§ao§9r §1c§5o§dd§be§3s§r:" + // "Color codes:" (in rainbow)
            "\n§8Black: §8&0     §4Dark Red: §4&4     §2Dark Green: §2&2     §1Dark Blue: §1&1" +
            "\n§3Dark Aqua: §3&3     §5Dark Purple: §5&5     §6Gold: §6&6     §7Gray: §7&7" +
            "\n§8Dark Gray: §8&8     §9Blue: §9&9     §aGreen: §a&a     §bAqua: §b&b" +
            "\n§cRed: §c&c     §dLight Purple: §d&d     §eYellow: §e&e     §fWhite: §f&f" +
            "\n" +
            "\n§lStyle §ncodes§r:" +
            "\n§kObfuscated§r: &k     §r§lBold: §l&l     §r§mStrikethrough: §m&m" +
            "\n§nUnderline: §n&n§r     §r§oItalic: §o&o    §rReset: §r&r"
    );
    //? if v0 {
    /*private static final UTextComponent FORMATTING_GUIDE_MESSAGE =
        new UTextComponent(yedelogo + " §e§nHover to view the formatting guide.").setHover(HoverEvent.Action.SHOW_TEXT, FORMATTING_CODES);
    *///?} else {
    private static final Component FORMATTING_GUIDE_MESSAGE =
        Component.text(yedelogo + " §e§nHover to view the formatting guide.").hoverEvent(HoverEvent.showText(FORMATTING_CODES));
    //?}

    //? if modern {
    private String displayTitle;

    public String getDisplayTitle() {
        return displayTitle;
    }
    //?}

    private YedelCommand() {}

    //~ if v1 '@Main' -> '@org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Handler'
    @Handler(description = "The main command, hosting all subcommands. When used with no arguments, opens the config screen.")
    public void main() {
        YedelConfig.getInstance().open();
    }

    @Handler(description = "Clears the currently set display text.")
    public void cleartext() {
        setDisplayText("");
        Platform.compatibility().displayChatMessage(yedelogo + " §eCleared display text!");
    }

    @Handler(description = "Shows mod constants and build information such as the project version.")
    public void constants() {
        try {
            Platform.compatibility().displayChatMessage(yedelogo + " §eConstants:");
            for (Field field : YedelModConstants.class.getDeclaredFields()) {
                Platform.compatibility().displayChatMessage(yedelogo + "§e> " + field.getName() + ": §r" + field.get(null));
            }
        }
        catch (IllegalAccessException e) {
            Platform.compatibility().displayChatMessage(yedelogo + " §cCouldn't get mod constants!");
            yedelog.error("Couldn't get mod constants!", e);

        }
    }

    @Handler(description = "Shows a formatting guide with color and style codes.")
    public void formatting() {
        Platform.compatibility().displayChatMessage(FORMATTING_GUIDE_MESSAGE);
    }

    @Handler(
        value = {"li"},
        description = "Sends an illegal chat character, which disconnects you on most servers and sends you to limbo-like areas on some. No longer works on Hypixel, use /limbo instead."
    )
    public void limbo() {
        //? if v0 {
        //Minecraft.getInstance().player.connection.sendChat("§");
        //?} else
        Minecraft.getInstance().player.connection.sendChat("§");
    }

    @Handler(
        value = {"limbocreative", "limbogmc", "lgmc"},
        description = "Gives you creative mode in Hypixel's limbo, given certain checks are passed."
    )
    public void limbocreative() {
        LimboCreative.getInstance().awardLimboCreative();
    }

    @Handler(
        value = {"playtime", "pt"},
        description = "Shows your total playtime (while playing on servers) in hours and minutes."
    )
    public void playtime() {
        int minutes = YedelConfig.getInstance().playtimeMinutes;
        Platform.compatibility().displayChatMessage(yedelogo + " §ePlaytime: §6" + minutes / 60 + " hours §eand §6" + minutes % 60 + " minutes");
    }

    @Handler(description = "Sets your nick for Bounty Hunting to not select yourself as the target.")
    public void setnick(String nick) {
        Platform.compatibility().displayChatMessage("§6§l- BountyHunting - §eSet nick to \"§f" + nick + "\"§e!");
        YedelConfig.getInstance().currentNick = nick;
        YedelConfig.getInstance().save();
    }

    @Handler(description = "Sets the display text, supporting color codes with ampersands (&).")
    public void settext(/*? if v0 {*//*@Greedy *//*?}*/String text) {
        String displayText = TextUtils.replaceAmpersand(text);
        setDisplayText(displayText);
        Platform.compatibility().displayChatMessage(yedelogo + " §eSet displayed text to \"§r" + displayText + "§e\"!");
    }

    @Handler(description = "Sets the title of the game window.")
    public void settitle(/*? if v0 {*//*@Greedy *//*?}*/String title) {
        //? if v0
        //Display.setTitle(title);
        //? else
        this.displayTitle = title;
        Platform.compatibility().displayChatMessage(yedelogo + " §eSet display title to \"§f" + title + "§e\"!");
    }

    @Handler(
        value = {"simulatechat", "simc"},
        description = "Simulates a chat message, supports color codes with ampersands (&)."
    )
    private void simulatechat(/*? if v0 {*//*@Greedy *//*?}*/String text) {
        String message = TextUtils.replaceAmpersand(text);
        Platform.compatibility().displayChatMessage(message);
    }

    @Handler(
        value = {"yedelmessage", "message"},
        description = "Shows messages from me about the mod. These can be anything from tips to bug notices."
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

                Platform.compatibility().displayChatMessage(yedelogo + " §eMessage from Yedel (last updated §f" + lastUpdatedTimeString + "§e):");
                Platform.compatibility().displayChatMessage(yedelMessage);
            }
            catch (IOException e) {
                Platform.compatibility().displayChatMessage(yedelogo + " §cCouldn't get mod message!");
                e.printStackTrace();
            }
        }, "YedelMod Message"
        ).start();
    }

    @Handler
    public void dp() {
        PingCommandInterface.getInstance().queuePing(PingMethod.values()[YedelConfig.getInstance().pingMethod]);
    }

    private void setDisplayText(String text) {
        //? if v0 {
        /*YedelConfig.getInstance().customTextHud.displayText = text;
        YedelConfig.getInstance().save();
        *///?} else {
        for (CustomTextHud hud : HudManager.INSTANCE.getHudsOfType(CustomTextHud.class)) {
            hud.displayText = text;
            hud.save();
        }
        //?}
    }

    //@TODO this does not work, dp is just a replacement
    //~ if v1 '@SubCommandGroup' -> '@Command'
    @Command("ping")
    public static class Ping {
        //~ if v1 '@Main' -> '@org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Handler'
        @Handler
        public void main() {
            PingCommandInterface.getInstance().queuePing(PingMethod.values()[YedelConfig.getInstance().pingMethod]);
        }

        @Handler(
            value = {"command", "c"},
            description = "Enters a random command and waits for the unknown command response. Works on almost all servers."
        )
        public void command() {
            PingCommandInterface.getInstance().queuePing(PingMethod.COMMAND_RESPONSE);
        }

        @Handler(
            value = {"tab", "t"},
            description = "Sends a tab completion packet and waits for the response. Works on all servers."
        )
        public void tab() {
            PingCommandInterface.getInstance().queuePing(PingMethod.TAB_PACKET);
        }

        @Handler(
            value = {"stats", "s"},
            description = "Sends a statistics packet and waits for the response. Works on all servers."
        )
        public void stats() {
            PingCommandInterface.getInstance().queuePing(PingMethod.STATS_PACKET);
        }

        @Handler(
            value = {"list", "l"},
            description = "Gets the ping displayed previously on the server list. Doesn't work on singleplayer or if you used Direct Connect."
        )
        public void list() {
            PingCommandInterface.getInstance().queuePing(PingMethod.SERVER_LIST_PING);
        }

        @Handler(
            value = {"hypixel", "h"},
            description = "Uses the Hypixel ping packet and waits for the response. Only works on Hypixel."
        )
        public void hypixel() {
            PingCommandInterface.getInstance().queuePing(PingMethod.HYPIXEL_PING);
        }
    }
}
