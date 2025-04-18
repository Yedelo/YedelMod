package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.ping.PingSender;
import at.yedel.yedelmod.hud.CustomTextHud;
import at.yedel.yedelmod.launch.YedelModConstants;
import at.yedel.yedelmod.utils.Requests;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateSource;
import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.deftu.omnicore.client.OmniChat;
import dev.deftu.omnicore.client.OmniClient;
import dev.deftu.omnicore.client.OmniClientCommandSource;
import dev.deftu.textile.minecraft.MCHoverEvent;
import dev.deftu.textile.minecraft.MCSimpleTextHolder;
import dev.deftu.textile.minecraft.MCTextFormat;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;
import org.polyfrost.oneconfig.api.commands.v1.CommandManager;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static at.yedel.yedelmod.YedelMod.yedelog;
import static at.yedel.yedelmod.launch.YedelModConstants.logo;
import static org.polyfrost.oneconfig.api.commands.v1.CommandManager.argument;
import static org.polyfrost.oneconfig.api.commands.v1.CommandManager.literal;



public class YedelCommand {
    private YedelCommand() {}

    private static final YedelCommand instance = new YedelCommand();

    public static YedelCommand getInstance() {
        return instance;
    }

    private final String formattingCodes =
        "§cC§6o§el§ao§9r §1c§5o§dd§be§3s§r:" + // "Color codes:" (in rainbow)
            "\n§8Black: §8&0     §4Dark Red: §4&4     §2Dark Green: §2&2     §1Dark Blue: §1&1" +
            "\n§3Dark Aqua: §3&3     §5Dark Purple: §5&5     §6Gold: §6&6     §7Gray: §7&7" +
            "\n§8Dark Gray: §8&8     §9Blue: §9&9     §aGreen: §a&a     §bAqua: §b&b" +
            "\n§cRed: §c&c     §dLight Purple: §d&d     §eYellow: §e&e     §fWhite: §f&f" +
            "\n" +
            "\n§lStyle §ncodes§r:" +
            "\n§kObfuscated§r: &k     §r§lBold: §l&l     §r§mStrikethrough: §m&m" +
            "\n§nUnderline: §n&n§r     §r§oItalic: §o&o    §rReset: §r&r";
    private final MCSimpleTextHolder formattingGuideMessage =
        new MCSimpleTextHolder(logo + " §e§nHover to view the formatting guide.")
            .withHoverEvent(new MCHoverEvent.ShowText(formattingCodes));

    private int main(CommandContext<OmniClientCommandSource> context) {
        YedelConfig.getInstance().openScreen();
        return 1;
    }

    private int cleartext(CommandContext<OmniClientCommandSource> context) {
        CustomTextHud.getInstance().displayText = "";
        CustomTextHud.getInstance().save();
        OmniChat.displayClientMessage(logo + " §eCleared display text!");
        return 1;
    }

    private int formatting(CommandContext<OmniClientCommandSource> context) {
        OmniChat.displayClientMessage(formattingGuideMessage);
        return 1;
    }

    private int limbo(CommandContext<OmniClientCommandSource> context) {
        OmniChat.sendPlayerMessage("§");
        return 1;
    }

    private int limbocreative(CommandContext<OmniClientCommandSource> context) {
        return LimboCreativeCheck.getInstance().checkLimbo();
    }

    private static class Ping {
        private int main(CommandContext<OmniClientCommandSource> context) {
            PingSender.getInstance().defaultMethodPing();
            return 1;
        }

        private int ping(CommandContext<OmniClientCommandSource> context) {
            PingSender.getInstance().pingPing();
            return 1;
        }

        private int command(CommandContext<OmniClientCommandSource> context) {
            PingSender.getInstance().commandPing();
            return 1;
        }

        private int tab(CommandContext<OmniClientCommandSource> context) {
            PingSender.getInstance().tabPing();
            return 1;
        }

        private int stats(CommandContext<OmniClientCommandSource> context) {
            PingSender.getInstance().statsPing();
            return 1;
        }

        private int list(CommandContext<OmniClientCommandSource> context) {
            PingSender.getInstance().serverListPing();
            return 1;
        }

        private int hypixel(CommandContext<OmniClientCommandSource> context) {
            PingSender.getInstance().hypixelPing();
            return 1;
        }
    }

    private final Ping ping = new Ping();

    private int playtime(CommandContext<OmniClientCommandSource> context) {
        int minutes = YedelConfig.getInstance().playtimeMinutes;
        OmniChat.displayClientMessage(logo + " §ePlaytime: §6" + minutes / 60 + " hours §eand §6" + minutes % 60 + " minutes");
        return 1;
    }

    private int setnick(CommandContext<OmniClientCommandSource> context) {
        String nick = StringArgumentType.getString(context, "nick");
        OmniChat.displayClientMessage("§6§l- BountyHunting - §eSet nick to " + nick + "§e!");
        YedelConfig.getInstance().currentNick = nick;
        YedelConfig.getInstance().save();
        return 1;
    }

    private int settext(CommandContext<OmniClientCommandSource> context) {
        String text = StringArgumentType.getString(context, "text");
        String displayText = MCTextFormat.translateAlternateColorCodes('&', text);
        CustomTextHud.getInstance().displayText = displayText;
        CustomTextHud.getInstance().save();
        OmniChat.displayClientMessage(logo + " §eSet displayed text to \"§r" + displayText + "§e\"!");
        return 1;
    }

    private int settitle(CommandContext<OmniClientCommandSource> context) {
        String title = StringArgumentType.getString(context, "title");
        Display.setTitle(title);
        OmniChat.displayClientMessage(logo + " §eSet display title to \"§f" + title + "§e\"!");
        return 1;
    }

    private int simulatechat(CommandContext<OmniClientCommandSource> context) {
        String text = StringArgumentType.getString(context, "text");
        OmniClient.getNetworkHandler().handleChat(new S02PacketChat(new ChatComponentText((MCTextFormat.translateAlternateColorCodes('&', text)))));
        return 1;
    }

    private static class Update {
        private int main(CommandContext<OmniClientCommandSource> context) {
            UpdateManager.getInstance().checkForUpdates(YedelConfig.getInstance().getUpdateSource(), UpdateManager.FeedbackMethod.CHAT);
            return 1;
        }

        private int modrinth(CommandContext<OmniClientCommandSource> context) {
            UpdateManager.getInstance().checkForUpdates(UpdateSource.MODRINTH, UpdateManager.FeedbackMethod.CHAT);
            return 1;
        }

        private int github(CommandContext<OmniClientCommandSource> context) {
            UpdateManager.getInstance().checkForUpdates(UpdateSource.GITHUB, UpdateManager.FeedbackMethod.CHAT);
            return 1;
        }
    }

    private final Update update = new Update();

    private int yedelmessage(CommandContext<OmniClientCommandSource> context) {
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
        return 1;
    }

    public void register() {
        // @formatter:off
        LiteralArgumentBuilder<OmniClientCommandSource> mainCommandBuilder =
            literal("yedel").executes(this::main)
            .then(literal("cleartext").executes(this::cleartext))
            .then(literal("formatting").executes(this::formatting))
            .then(literal("limbo").executes(this::limbo))
                .then(literal("li").executes(this::limbo))
            .then(literal("limbocreative").executes(this::limbocreative))
                .then(literal("limbogmc").executes(this::limbocreative))
                .then(literal("lgmc").executes(this::limbocreative))
            .then(
                literal("ping").executes(ping::main)
                .then(literal("ping").executes(ping::ping))
                    .then(literal("p").executes(ping::ping))
                .then(literal("command").executes(ping::command))
                    .then(literal("c").executes(ping::command))
                .then(literal("tab").executes(ping::tab))
                    .then(literal("t").executes(ping::tab))
                .then(literal("stats").executes(ping::stats))
                    .then(literal("s").executes(ping::stats))
                .then(literal("list").executes(ping::list))
                    .then(literal("l").executes(ping::list))
                .then(literal("hypixel").executes(ping::hypixel))
                    .then(literal("h").executes(ping::hypixel))
            )
            .then(literal("playtime").executes(this::playtime))
                .then(literal("pt").executes(this::playtime))
            .then(literal("setnick").then(stringArgument("nick").executes(this::setnick)))
            .then(literal("settext").then(greedyStringArgument("text").executes(this::settext)))
            .then(literal("settitle").then(greedyStringArgument("title").executes(this::settitle)))
            .then(literal("simulatechat").then(greedyStringArgument("text").executes(this::simulatechat)))
                .then(literal("simc").then(greedyStringArgument("text").executes(this::simulatechat)))
            .then(
                literal("update").executes(update::main)
                .then(literal("modrinth").executes(update::modrinth))
                .then(literal("github").executes(update::github))
            )
            .then(literal("yedelmessage").executes(this::yedelmessage))
                .then(literal("message").executes(this::yedelmessage))
        ;

        LiteralCommandNode<OmniClientCommandSource> mainCommandNode = mainCommandBuilder.build();
        CommandManager.register(mainCommandBuilder);
        CommandManager.register(literal(YedelModConstants.MOD_ID).redirect(mainCommandNode));
        // @formatter:on
    }

    private RequiredArgumentBuilder<OmniClientCommandSource, String> stringArgument(String name) {
        return argument(name, StringArgumentType.string());
    }

    private RequiredArgumentBuilder<OmniClientCommandSource, String> greedyStringArgument(String name) {
        return argument(name, StringArgumentType.greedyString());
    }
}
