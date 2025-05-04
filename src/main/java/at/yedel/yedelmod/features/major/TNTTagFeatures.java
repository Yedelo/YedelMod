package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.mixins.InvokerRender;
import at.yedel.yedelmod.utils.Constants;
import at.yedel.yedelmod.utils.RankColor;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.libs.universal.USound;
import cc.polyfrost.oneconfig.libs.universal.wrappers.UPlayer;
import cc.polyfrost.oneconfig.utils.Multithreading;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class TNTTagFeatures {
    private static final TNTTagFeatures INSTANCE = new TNTTagFeatures();

    public static TNTTagFeatures getInstance() {
        return INSTANCE;
    }

    private final ArrayList<String> players = new ArrayList<>();
    private static final Pattern YOU_TAGGED_PERSON_REGEX =
        Pattern.compile("You tagged (?<personThatYouTagged>[a-zA-Z0-9_]*)!");
    private static final Pattern PERSON_IS_IT_REGEX = Pattern.compile("(?<personThatIsIt>[a-zA-Z0-9_]*) is IT!");
    private static final Pattern PERSON_BLEW_UP_REGEX = Pattern.compile("(?<personThatBlewUp>[a-zA-Z0-9_]*) blew up!");

    private String target;
    private String targetRanked;
    private boolean whoCheck;
    private boolean fightingTarget;
    private RankColor targetRankColor = RankColor.GRAY; // Prevents it from trying to render with a null color code
    private boolean dead;
    private String playerName;

    private final List<String> displayLines = new ArrayList<String>();

    public List<String> getDisplayLines() {
        return displayLines;
    }

    private TNTTagFeatures() {
        HypixelModAPI.getInstance().registerHandler(ClientboundLocationPacket.class, this::handleLocationPacket);

        displayLines.add("§c§lBounty §f§lHunting");
        displayLines.add("§a" + YedelConfig.getInstance().bountyHuntingPoints + " points");
        displayLines.add("§a" + YedelConfig.getInstance().bountyHuntingKills + " kills");
        displayLines.add("");
    }

    private boolean inTNTTag;

    public boolean isInTNTTag() {
        return inTNTTag;
    }

    private void handleLocationPacket(ClientboundLocationPacket packet) {
        inTNTTag = packet.getMode().isPresent() && packet.getMode().get().equals("TNTAG");
        if (inTNTTag) {
            onTNTTagJoin();
        }
    }

    public void onTNTTagJoin() {
        if (YedelConfig.getInstance().bountyHunting) {
            playerName = UPlayer.getPlayer().getName();
            dead = false;
            target = null;
            displayLines.set(0, "§c§lBounty §f§lHunting");
            displayLines.set(1, "§a" + YedelConfig.getInstance().bountyHuntingPoints + " points");
            displayLines.set(2, "§a" + YedelConfig.getInstance().bountyHuntingKills + " kills");
            displayLines.set(3, "");
            if (YedelConfig.getInstance().firstTimeBountyHunting) {
                UChat.chat("§6§l[BountyHunting] §eIf this is your first time using this mod and you're nicked, or you've changed your nick, you will have to set your currentNick with §n/setnick§r§3.");
                YedelConfig.getInstance().firstTimeBountyHunting = false;
                YedelConfig.getInstance().save();
            }
        }
    }

    @Subscribe
    public void handleRoundStarted(ChatReceiveEvent event) {
        if (!YedelConfig.getInstance().bountyHunting || !inTNTTag || !event.message.getUnformattedText().endsWith("has started!")) {
            return;
        }
        players.clear();
        for (NetworkPlayerInfo playerInfo : UMinecraft.getNetHandler().getPlayerInfoMap()) {
            players.add(playerInfo.getGameProfile().getName());
        }
        players.remove(playerName);
        players.remove(YedelConfig.getInstance().currentNick);
        target = players.get((int) Math.floor(Math.random() * players.size()));
        whoCheck = true;
        UChat.say("/who");
        if (YedelConfig.getInstance().playHuntingSounds) {
            USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, 0.8F);
        }
        displayLines.set(1, "§a" + YedelConfig.getInstance().bountyHuntingPoints + " points");
        displayLines.set(2, "§a" + YedelConfig.getInstance().bountyHuntingKills + " kills");
    }

    @Subscribe
    public void handleWhoMessage(ChatReceiveEvent event) {
        String msg = event.message.getFormattedText();
        if (!event.message.getUnformattedText().startsWith("ONLINE: ") || !whoCheck) return;
        whoCheck = false;
        event.isCancelled = true;
        String[] playersArray = msg.substring(14).split("§r§7, ");
        for (String player: playersArray) {
            if (player.contains(target)) {
                targetRanked = player;
            }
        }
        displayLines.set(3, "§cYour next target is " + targetRanked + ".");
        if (targetRanked.startsWith("§r§7")) {
            targetRankColor = RankColor.GRAY;
        }
        else if (targetRanked.startsWith("§r§a")) {
            targetRankColor = RankColor.GREEN;
        }
        else if (targetRanked.startsWith("§r§b")) {
            targetRankColor = RankColor.AQUA;
        }
        else if (targetRanked.startsWith("§r§6")) {
            targetRankColor = RankColor.GOLD;
        }
        else {
            targetRankColor = RankColor.RED;
        }
    }

    @Subscribe
    public void handleFightMessages(ChatReceiveEvent event) {
        String msg = event.message.getUnformattedText();
        Matcher tagOtherMatcher = YOU_TAGGED_PERSON_REGEX.matcher(msg);
        while (tagOtherMatcher.find()) {
            if (Objects.equals(tagOtherMatcher.group("personThatYouTagged"), target)) {
                fightingTarget = true;
            }
        }

        Matcher personIsItMatcher = PERSON_IS_IT_REGEX.matcher(msg);
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
        EntityPlayerSP player = UPlayer.getPlayer();
        if (Objects.equals(targetPlayer.getName(), target) && player.canEntityBeSeen(event.entityPlayer) && !targetPlayer.isInvisible()) {
            double sneakingInc = targetPlayer.isSneaking() ? 0.0 : 0.3;
            String text = targetRankColor.colorCode + "Distance: " + (int) Math.floor(player.getDistanceToEntity(targetPlayer)) + " blocks";
            ((InvokerRender) event.renderer).yedelmod$invokeRenderLabel(targetPlayer, text, event.x, event.y + sneakingInc, event.z, 64);
        }
    }

    @Subscribe
    public void onBlastRadiusDeath(ChatReceiveEvent event) {
        if (event.message.getUnformattedText().startsWith("You were blown up by")) {
            target = null;
            dead = true;
            displayLines.set(3, "");
        }
    }

    @Subscribe
    public void onRoundEnd(ChatReceiveEvent event) {
        if (!YedelConfig.getInstance().bountyHunting) return;
        String msg = event.message.getUnformattedText();
        Matcher peopleDeathMatcher = PERSON_BLEW_UP_REGEX.matcher(msg);
        while (peopleDeathMatcher.find()) {
            String personDied = peopleDeathMatcher.group("personThatBlewUp");
            if (Objects.equals(personDied, playerName)) {
                dead = true;
                target = null;
                displayLines.set(3, "");
            }
            if (Objects.equals(personDied, target) && fightingTarget) {
                Multithreading.schedule(() -> {
                    int pointIncrease = (int) Math.ceil(players.size() * 0.8);
                    if (dead) {
                        pointIncrease /= 2;
                    }
                    YedelConfig.getInstance().bountyHuntingPoints += pointIncrease;
                    YedelConfig.getInstance().bountyHuntingKills += 1;
                    displayLines.set(1, "§a" + YedelConfig.getInstance().bountyHuntingPoints + " points (+" + pointIncrease + ")");
                    displayLines.set(2, "§a" + YedelConfig.getInstance().bountyHuntingKills + " kills (+1)");
                    displayLines.set(3, "§cYou killed your target!");
                    if (YedelConfig.getInstance().playHuntingSounds) {
                        USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, 1.04F);
                    }
                    YedelConfig.getInstance().save();
                    }, 500, TimeUnit.MILLISECONDS
                );
            }
        }
    }

    @Subscribe
    public void onNickChange(ChatReceiveEvent event) {
        if (Objects.equals(event.message.getUnformattedText(), "Processing request. Please wait...") && YedelConfig.getInstance().bountyHunting) {
            UChat.chat("§6§l- BountyHunting - §ePlease set your nick with /setnick or in the config.");
        }
    }
}
