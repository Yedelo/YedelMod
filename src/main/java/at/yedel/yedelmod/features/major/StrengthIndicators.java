package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.mixins.InvokerRender;
import at.yedel.yedelmod.utils.NumberUtils;
import at.yedel.yedelmod.utils.RenderUtils;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.wrappers.UPlayer;
import com.google.common.collect.ImmutableMap;
import net.hypixel.data.type.GameType;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class StrengthIndicators {
    private static final StrengthIndicators INSTANCE = new StrengthIndicators();

    public static StrengthIndicators getInstance() {
        return INSTANCE;
    }

    private static final ImmutableMap<Integer, String> COLOR_MAP = ImmutableMap.<Integer, String>builder()
        .put(0, "§4")
        .put(1, "§c")
        .put(2, "§6")
        .put(3, "§e")
        .put(4, "§2")
        .put(5, "§a")
        .put(6, "§b")
        .put(7, "§3")
        .put(8, "§1")
        .put(9, "§9")
        .put(10, "§d")
        .put(11, "§5")
        .put(12, "§f")
        .put(13, "§7")
        .put(14, "§8")
        .put(15, "§0")
        .build();
    private static final String USERNAME_PATTERN = "(?<player>[1-9a-zA-Z_]{3,16})";
    private static final String NUMBER_WITH_COMMAS_PATTERN = "[\\d,]+";

    private final Map<String, Double> strengthPlayers = new HashMap<>();
    private boolean inSkywars;
    private double strengthDuration;

    private StrengthIndicators() {
        HypixelModAPI.getInstance().registerHandler(ClientboundLocationPacket.class, this::handleLocationPacket);
    }

    private void handleLocationPacket(ClientboundLocationPacket packet) {
        inSkywars = packet.getServerType().isPresent() && packet.getServerType().get() == GameType.SKYWARS;
        if (packet.getMode().isPresent()) {
            switch (packet.getMode().get()) {
                case "solo_normal":
                case "solo_insane":
                    strengthDuration = 5;
                    break;
                case "teams_normal":
                    strengthDuration = 2;
                    break;
                case "mini_normal":
                case "mega_doubles":
                case "solo_insane_lucky":
                case "teams_insane_lucky":
                default:
                    strengthDuration = 0;
            }
        }
        else {
            strengthDuration = 0;
        }
    }

    @Subscribe
    public void downtickStrengthPlayers(TickEvent event) {
        if (event.stage == Stage.START) {
            Set<Map.Entry<String, Double>> strengthPlayerSet = strengthPlayers.entrySet();
            for (Map.Entry<String, Double> entry : strengthPlayerSet) {
                String player = entry.getKey();
                Double seconds = entry.getValue();
                strengthPlayers.put(player, NumberUtils.round(seconds - 0.05, 2));
            }
            strengthPlayerSet.removeIf(strengthPlayer -> strengthPlayer.getValue() <= 0);
        }
    }

    @Subscribe
    public void handleKillMessage(ChatReceiveEvent event) {
        if (inSkywars && strengthDuration != 0) {
            String message = event.message.getUnformattedText();
            for (Pattern killPattern : KILL_PATTERNS) {
                Matcher messageMatcher = killPattern.matcher(message);
                if (messageMatcher.find()) {
                    strengthPlayers.put(messageMatcher.group("killer"), NumberUtils.round(strengthDuration, 2));
                    strengthPlayers.put(messageMatcher.group("killed"), 0.0D);
                }
            }
        }
    }

    @SubscribeEvent
    public void renderStrengthIndicator(RenderPlayerEvent.Pre event) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().skywarsStrengthIndicators) {
            EntityPlayer entityPlayer = event.entityPlayer;
            if (!YedelConfig.getInstance().showSelfStrength && entityPlayer == UPlayer.getPlayer()) {
                return;
            }
            if (entityPlayer.isInvisible()) {
                return;
            }
            String entityName = entityPlayer.getName();
            if (!strengthPlayers.containsKey(entityName)) {
                return;
            }
            net.minecraft.client.renderer.entity.RenderPlayer
            String text =
                COLOR_MAP.get(YedelConfig.getInstance().strengthColor) + "Strength - " + strengthPlayers.get(entityName) + "s";
            double sneakingInc = entityPlayer.isSneaking() ? -0.125 : 0;
            int expectedLines = RenderUtils.shouldRenderSubinfo(entityPlayer) ? 2 : 1;
            ((InvokerRender) event.renderer).yedelmod$renderLivingLabel(entityPlayer, text, event.x, event.y + (expectedLines * 0.274) + sneakingInc, event.z, 64);
        }
    }

    @Subscribe
    public void clearStrengthPlayers(ReceivePacketEvent event) {
        if (event.packet instanceof S01PacketJoinGame) {
            strengthPlayers.clear();
        }
    }

    private static final Pattern[] KILL_PATTERNS = Arrays.stream(new String[] {
        USERNAME_PATTERN.replace("player", "killed") + " was killed by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was thrown into the void by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was thrown off a cliff by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was shot by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was struck down by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was turned to dust by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was turned to ash by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was melted by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was filled full of lead by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " met their end by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " lost a drinking contest with " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " lost the draw to " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was given the cold shoulder by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was out of the league of " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + "'s heart was broken by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was struck with Cupid's arrow by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " be sent to Davy Jones' locker by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " be cannonballed to death by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " be voodooed by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " be shot and killed by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was turned into space dust by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was sent into orbit by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was retrograded by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was hit by an asteroid from " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was deleted by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was ALT\\+F4'd by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was crashed by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was rm -rf by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " died in close combat to " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " fought to the edge with " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " stumbled off a ledge with help by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " fell to the great marksmanship of " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " became victim #" + NUMBER_WITH_COMMAS_PATTERN + " of " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was void victim #" + NUMBER_WITH_COMMAS_PATTERN + " of " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was bow kill #" + NUMBER_WITH_COMMAS_PATTERN + " of " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was glazed in BBQ sauce by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " slipped in BBQ sauce off the edge spilled by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was not spicy enough for " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was thrown chili powder at by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was exterminated by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was scared off an edge by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was squashed by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was tranquilized by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was mushed by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was peeled by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " slipped on " + USERNAME_PATTERN.replace("player", "killer") + "'s banana peel off a cliff\\.",
        USERNAME_PATTERN.replace("player", "killed") + " got banana pistol'd by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was chewed up by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was squeaked off the edge by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was distracted by a rat dragging pizza from " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was squeaked from a distance by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was oinked by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " slipped into void for " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was distracted by a piglet from " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " got attacked by a carrot from " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was buzzed to death by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was bzzz'd off the edge by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was stung by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was startled from a distance by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was socked by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was KO'd by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " took an uppercut from " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was sent into a daze by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was trampled by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was back kicked into the void by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was headbutted off a cliff by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was impaled from a distance by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was crusaded by the knight " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was jousted by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was catapulted by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was shot to the knee by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was bit by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " got WOOF'D by " + USERNAME_PATTERN.replace("player", "killer") + " into the void\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was growled off an edge by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was thrown a frisbee by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " got rekt by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " took the L to " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " got dabbed on by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " got bamboozled by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was backstabbed by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was pushed into the abyss by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was thrown into a ravine by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was brutally shot by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was locked outside during a snow storm by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was shoved down an icy slope by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was made into a snowman by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was hit with a snowball from " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was painted pretty by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was flipped off the edge by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was deviled by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was made sunny side up by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was whacked with a party balloon by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was popped into the void by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was launched like a firework by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was shot with a roman candle by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was wrapped up by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was tied into a bow by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " tripped over a present placed by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was glued up by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was crushed into moon dust by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was sent the wrong way by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was blasted to the moon by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was smothered in holiday cheer by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was banished into the ether by " + USERNAME_PATTERN.replace("player", "killer") + "'s holiday spirit\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was pushed by " + USERNAME_PATTERN.replace("player", "killer") + "'s holiday spirit\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was sniped by a missile of festivity by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was ripped to shreds by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was charged by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was ripped and thrown by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was pounced on by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was put on the naughty list by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was pushed down the chimney by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was traded in for milk and cookies by " + USERNAME_PATTERN.replace("player", "killer") + "\\.",
        USERNAME_PATTERN.replace("player", "killed") + " was turned to gingerbread by " + USERNAME_PATTERN.replace("player", "killer") + "\\."
    }).map(Pattern::compile).toArray(Pattern[]::new);
}
