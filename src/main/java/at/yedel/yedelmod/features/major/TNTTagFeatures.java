package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.hud.BountyHuntingHud;
import at.yedel.yedelmod.mixins.net.minecraft.client.renderer.entity.InvokerRender;
import at.yedel.yedelmod.utils.Constants;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.RankColor;
import dev.deftu.omnicore.client.OmniChat;
import dev.deftu.omnicore.client.OmniClient;
import dev.deftu.omnicore.client.OmniClientPlayer;
import dev.deftu.omnicore.client.OmniClientSound;
import net.hypixel.data.type.GameType;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.events.HypixelLocationEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.utils.v1.Multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class TNTTagFeatures {
    private static final TNTTagFeatures instance = new TNTTagFeatures();

    public static TNTTagFeatures getInstance() {
        return instance;
    }

    private final ArrayList<String> players = new ArrayList<>();
    private final Pattern youTaggedPersonRegex = Pattern.compile("You tagged (?<personThatYouTagged>[a-zA-Z0-9_]*)!");
    private final Pattern personIsItRegex = Pattern.compile("(?<personThatIsIt>[a-zA-Z0-9_]*) is IT!");
    private final Pattern personBlewUpRegex = Pattern.compile("(?<personThatBlewUp>[a-zA-Z0-9_]*) blew up!");

    private String target;
    private String targetRanked;
    private boolean whoCheck;
    private boolean fightingTarget;
    private RankColor targetRankColor = RankColor.GRAY; // Prevents it from trying to render with a null color code
    private boolean dead;
    private String playerName;

    private final List<String> displayLines = new ArrayList<String>();

    private TNTTagFeatures() {
        displayLines.add("§c§lBounty §f§lHunting");
        displayLines.add("§a" + YedelConfig.getInstance().bountyHuntingPoints + " points");
        displayLines.add("§a" + YedelConfig.getInstance().bountyHuntingKills + " kills");
        displayLines.add("");
    }

    @Subscribe
    public void onTNTTagJoin(HypixelLocationEvent event) {
        if (YedelConfig.getInstance().bountyHunting) {
            playerName = OmniClientPlayer.getName();
            dead = false;
            target = null;
            setDisplayLine(0, "§c§lBounty §f§lHunting");
            setDisplayLine(1, "§a" + YedelConfig.getInstance().bountyHuntingPoints + " points");
            setDisplayLine(2, "§a" + YedelConfig.getInstance().bountyHuntingKills + " kills");
            setDisplayLine(3, "");
            if (YedelConfig.getInstance().firstTimeBountyHunting) {
                OmniChat.displayClientMessage("§6§l[BountyHunting] §eIf this is your first time using this mod and you're nicked, or you've changed your nick, you will have to set your currentNick with §n/setnick§r§3.");
                YedelConfig.getInstance().firstTimeBountyHunting = false;
                YedelConfig.getInstance().save();
            }
        }
    }

    @Subscribe
    public void handleRoundStarted(ChatEvent.Receive event) {
        if (!YedelConfig.getInstance().bountyHunting || Functions.isInGame(GameType.TNTGAMES) || !event.getFullyUnformattedMessage().endsWith("has started!"))
            return;
        players.clear();
        for (NetworkPlayerInfo playerInfo : OmniClient.getNetworkHandler().getPlayerInfoMap()) {
            players.add(playerInfo.getGameProfile().getName());
        }
        players.remove(playerName);
        players.remove(YedelConfig.getInstance().currentNick);
        target = players.get((int) Math.floor(Math.random() * players.size()));
        whoCheck = true;
        OmniChat.sendPlayerMessage("/who");
        if (YedelConfig.getInstance().playHuntingSounds) {
            OmniClientSound.play(Constants.plingSound, 1, 0.8F);
        }
        setDisplayLine(1, "§a" + YedelConfig.getInstance().bountyHuntingPoints + " points");
        setDisplayLine(2, "§a" + YedelConfig.getInstance().bountyHuntingKills + " kills");
    }

    @Subscribe
    public void handleWhoMessage(ChatEvent.Receive event) {
        String msg = event.getFullyUnformattedMessage();
        if (!msg.startsWith("ONLINE: ") || !whoCheck) return;
        whoCheck = false;
        event.cancelled = true;
        String[] playersArray = msg.substring(14).split("§r§7, ");
        for (String player : playersArray) {
            if (player.contains(target)) {
                targetRanked = player;
            }
        }
        setDisplayLine(3, "§cYour next target is " + targetRanked + ".");
        if (targetRanked.startsWith("§r§7")) targetRankColor = RankColor.GRAY;
        else if (targetRanked.startsWith("§r§a")) targetRankColor = RankColor.GREEN;
        else if (targetRanked.startsWith("§r§b")) targetRankColor = RankColor.AQUA;
        else if (targetRanked.startsWith("§r§6")) targetRankColor = RankColor.GOLD;
        else targetRankColor = RankColor.RED;
    }

    @Subscribe
    public void handleFightMessages(ChatEvent.Receive event) {
        String msg = event.getFullyUnformattedMessage();
        Matcher tagOtherMatcher = youTaggedPersonRegex.matcher(msg);
        while (tagOtherMatcher.find()) {
            if (Objects.equals(tagOtherMatcher.group("personThatYouTagged"), target)) {
                fightingTarget = true;
            }
        }

        Matcher personIsItMatcher = personIsItRegex.matcher(msg);
        while (personIsItMatcher.find()) {
            if (Objects.equals(personIsItMatcher.group("personThatIsIt"), target) && !dead) {
                fightingTarget = false;
            }
        }
    }

    @SubscribeEvent
    public void handleAttackTarget(AttackEntityEvent event) {
        if (Objects.equals(event.target.getName(), target) && !dead) {
            fightingTarget = true;
        }
    }

    @SubscribeEvent
    public void renderTargetLabel(RenderPlayerEvent.Pre event) {
        if (!YedelConfig.getInstance().bountyHunting || !YedelConfig.getInstance().highlightTargetAndShowDistance)
            return;
        EntityPlayer targetPlayer = event.entityPlayer;
        EntityPlayerSP player = OmniClientPlayer.getInstance();
        if (
            Objects.equals(targetPlayer.getName(), target)
                &&
                player.canEntityBeSeen(event.entityPlayer)
                &&
                !targetPlayer.isInvisible()
        ) {
            double sneakingInc = targetPlayer.isSneaking() ? 0.0 : 0.3;
            String text = targetRankColor.colorCode + "Distance: " + (int) Math.floor(player.getDistanceToEntity(targetPlayer)) + " blocks";
            ((InvokerRender) event.renderer).yedelmod$invokeRenderLabel(targetPlayer, text, event.x, event.y + sneakingInc, event.z, 64);
        }
    }

    @Subscribe
    public void onBlastRadiusDeath(ChatEvent.Receive event) {
        if (event.getFullyUnformattedMessage().startsWith("You were blown up by")) {
            target = null;
            dead = true;
            setDisplayLine(3, "");
        }
    }

    @Subscribe
    public void onRoundEnd(ChatEvent.Receive event) {
        if (!YedelConfig.getInstance().bountyHunting) return;
        String msg = event.getFullyUnformattedMessage();
        Matcher peopleDeathMatcher = personBlewUpRegex.matcher(msg);
        while (peopleDeathMatcher.find()) {
            String personDied = peopleDeathMatcher.group("personThatBlewUp");
            if (Objects.equals(personDied, playerName)) {
                dead = true;
                target = null;
                setDisplayLine(3, "");
            }
            if (Objects.equals(personDied, target) && fightingTarget) {
                Multithreading.schedule(() -> {
                    int pointIncrease = (int) Math.ceil(players.size() * 0.8);
                    if (dead) pointIncrease /= 2;
                        YedelConfig.getInstance().bountyHuntingPoints += pointIncrease;
                        YedelConfig.getInstance().bountyHuntingKills += 1;
                    setDisplayLine(1, "§a" + YedelConfig.getInstance().bountyHuntingPoints + " points (+" + pointIncrease + ")");
                    setDisplayLine(2, "§a" + YedelConfig.getInstance().bountyHuntingKills + " kills (+1)");
                    setDisplayLine(3, "§cYou killed your target!");
                        if (YedelConfig.getInstance().playHuntingSounds) {
                            OmniClientSound.play(Constants.plingSound, 1, 1.04F);
                        }
                    YedelConfig.getInstance().save();
                    }, 500, TimeUnit.MILLISECONDS
                );
            }
        }
    }

    @Subscribe
    public void onNickChange(ChatEvent.Receive event) {
        if (Objects.equals(event.getFullyUnformattedMessage(), "Processing request. Please wait...") && YedelConfig.getInstance().bountyHunting) {
            OmniChat.displayClientMessage("§6§l- BountyHunting - §ePlease set your nick with /setnick or in the config.");
        }
    }

    public void setDisplayLine(int index, String line) {
        displayLines.set(index, line);
        BountyHuntingHud.getInstance().string.append(String.join("\n", displayLines));
        BountyHuntingHud.getInstance().relogic();
    }
}
