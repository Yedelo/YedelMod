package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.mixins.net.minecraft.client.renderer.entity.InvokerRender;
import at.yedel.yedelmod.utils.typeutils.NumberUtils;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.hypixel.data.type.GameType;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class StrengthIndicators {
    private static final StrengthIndicators instance = new StrengthIndicators();

    public static StrengthIndicators getInstance() {
        return instance;
    }

    private final Map<String, Double> strengthPlayers = new HashMap<>();

    private final Map<Integer, String> colorMap = new HashMap<>(); // Config array values -> color codes

    private StrengthIndicators() {
        HypixelModAPI.getInstance().registerHandler(ClientboundLocationPacket.class, this::handleLocationPacket);

        colorMap.put(0, "§4");
        colorMap.put(1, "§c");
        colorMap.put(2, "§6");
        colorMap.put(3, "§e");
        colorMap.put(4, "§2");
        colorMap.put(5, "§a");
        colorMap.put(6, "§b");
        colorMap.put(7, "§3");
        colorMap.put(8, "§1");
        colorMap.put(9, "§9");
        colorMap.put(10, "§d");
        colorMap.put(11, "§5");
        colorMap.put(12, "§f");
        colorMap.put(13, "§7");
        colorMap.put(14, "§8");
        colorMap.put(15, "§0");
    }

    private boolean inSkywars;

    private double strengthDuration;

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
        else strengthDuration = 0;
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
            for (Pattern killPattern : killPatterns) {
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
        if (!YedelConfig.getInstance().skywarsStrengthIndicators) return;
        EntityPlayer entityPlayer = event.entityPlayer;
        if (entityPlayer.isInvisible()) return;
        String entityName = entityPlayer.getName();
        if (!strengthPlayers.containsKey(entityName)) return;
        String text =
            colorMap.get(YedelConfig.getInstance().strengthColor) + "Strength - " + strengthPlayers.get(entityName) + "s";
        double sneakingInc = entityPlayer.isSneaking() ? -1 : 0;
        ((InvokerRender) event.renderer).yedelmod$invokeRenderLabel(entityPlayer, text, event.x, event.y + 0.55 + sneakingInc, event.z, 64);
    }

    @Subscribe
    public void clearStrengthPlayers(ReceivePacketEvent event) {
        if (event.packet instanceof S01PacketJoinGame) {
            strengthPlayers.clear();
        }
    }

    private static final Pattern[] killPatterns = {
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was killed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was thrown into the void by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was thrown off a cliff by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was shot by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) got rekt by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) took the L to (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) got dabbed on by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) got bamboozled by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was trampled by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was back kicked into the void by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was headbutted off a cliff by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was impaled from a distance by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was struck down by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was turned to dust by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was turned to ash by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was melted by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was filled full of lead by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) met their end by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) lost a drinking contest with (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) lost the draw to (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was given the cold shoulder by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was out of the league of (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*)'s heart was broken by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was struck with Cupid's arrow by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) be sent to Davy Jones' locker by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) be cannonballed to death by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) be voodooed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was turned into space dust by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was sent into orbit by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was retrograded by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was hit by an asteroid from (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was deleted by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was ALT\\+F4'd by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was crashed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was rm -rf by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) died in close combat to (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) fought to the edge with (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) stumbled off a ledge with help by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) fell to the great marksmanship of (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was glazed in BBQ sauce by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) slipped in BBQ sauce of the edge spilled by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was not spicy enough for (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was thrown chili powder at by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was exterminated by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was squashed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was tranquilized by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was mushed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was peeled by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) slipped on (?<killer>[a-zA-Z0-9_]*)'s banana peel off a cliff\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) got banana pistol'd by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was chewed up by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was squeaked off the edge by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was distracted by a rat draggging pizza from (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was squeaked from a distance by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was oinked up by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) slipped into void for (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was distracted by a piglet from (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) got attacked by a carrot from (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was buzzed to death by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was bzzz'd off the edge by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was stung by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was startled from a distance by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was socked by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was KO'd by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) took an uppercut from (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was sent into a daze by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was crusaded by the knight (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was jousted by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was capapulted by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was shot to the knee by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was bit by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) got WOOF'D by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was growled off an edge by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was thrown a frisbee by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was backstabbed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was pushed into the abyss by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was thrown into a ravine by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was brutally shot by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was locked outside during a snow storm by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was shoved down an icy slope by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was made into a snowman by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was hit with a snowball from (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was painted pretty by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was flipped off the edge by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was deviled by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was made sunny side up by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was whacked with a party balloon by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was popped into the void by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was launched like a firework by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was shot with a roman candle by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was wrapped up by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was tied into a bow by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) tripped over a present placed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was glued up by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was crushed into moon dust by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was sent the wrong way by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was blasted to the moon by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was smothered in holiday cheer by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was banished into the ether by (?<killer>[a-zA-Z0-9_]*)'s holiday spirit\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was pushed by (?<killer>[a-zA-Z0-9_]*)'s holiday spirit\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was sniped by a missle of festivity by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) became victim .+? of (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was bow kill .+? of (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was void victim .+? of (?<killer>[a-zA-Z0-9_]*)\\.")
    };
}
