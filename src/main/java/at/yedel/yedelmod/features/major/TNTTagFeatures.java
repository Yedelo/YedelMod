package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.handlers.HypixelManager;
import at.yedel.yedelmod.hud.BountyHuntingHud;
import at.yedel.yedelmod.mixins.net.minecraft.client.renderer.entity.InvokerRender;
import at.yedel.yedelmod.utils.Constants;
import at.yedel.yedelmod.utils.RankColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.libs.universal.USound;
import cc.polyfrost.oneconfig.libs.universal.wrappers.UPlayer;
import cc.polyfrost.oneconfig.utils.Multithreading;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static at.yedel.yedelmod.launch.YedelModConstants.logo;



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

    private TNTTagFeatures() {
        BountyHuntingHud.getInstance().getLines().add("§c§lBounty §f§lHunting");
        BountyHuntingHud.getInstance().getLines().add("§a" + YedelConfig.getInstance().points + " points");
        BountyHuntingHud.getInstance().getLines().add("§a" + YedelConfig.getInstance().kills + " kills");
        BountyHuntingHud.getInstance().getLines().add("");
    }

    public void onTNTTagJoin() {
        if (YedelConfig.getInstance().bountyHunting) {
            playerName = UPlayer.getPlayer().getName();
            dead = false;
            target = null;
            BountyHuntingHud.getInstance().getLines().set(0, "§c§lBounty §f§lHunting");
            BountyHuntingHud.getInstance().getLines().set(1, "§a" + YedelConfig.getInstance().points + " points");
            BountyHuntingHud.getInstance().getLines().set(2, "§a" + YedelConfig.getInstance().kills + " kills");
            BountyHuntingHud.getInstance().getLines().set(3, "");
            if (YedelConfig.getInstance().bhFirst) {
                UChat.chat(logo + " §6§l[BountyHunting] §eIf this is your first time using this mod and you're nicked, or you've changed your nick, you will have to set your nick with §n/setnick§r§3.");
                YedelConfig.getInstance().bhFirst = false;
                YedelConfig.getInstance().save();
            }
        }
    }

    @SubscribeEvent
    public void onRoundStarted(ClientChatReceivedEvent event) {
        if (!YedelConfig.getInstance().bountyHunting || !HypixelManager.getInstance().isInTNTTag() || !event.message.getUnformattedText().endsWith("has started!"))
            return;
        players.clear();
        for (NetworkPlayerInfo playerInfo : UMinecraft.getNetHandler().getPlayerInfoMap()) {
            players.add(playerInfo.getGameProfile().getName());
        }
        players.remove(playerName);
        players.remove(YedelConfig.getInstance().nick);
        target = players.get((int) Math.floor(Math.random() * players.size()));
        whoCheck = true;
        UChat.say("/who");
        if (YedelConfig.getInstance().bhSounds) {
            USound.INSTANCE.playSoundStatic(Constants.plingSoundLocation, 1, 0.8F);
        }
        BountyHuntingHud.getInstance().getLines().set(1, "§a" + YedelConfig.getInstance().points + " points");
        BountyHuntingHud.getInstance().getLines().set(2, "§a" + YedelConfig.getInstance().kills + " kills");
    }

    @SubscribeEvent
    public void onWhoMessage(ClientChatReceivedEvent event) {
        String msg = event.message.getFormattedText();
        if (!event.message.getUnformattedText().startsWith("ONLINE: ") || !whoCheck) return;
        whoCheck = false;
        event.setCanceled(true);
        String[] playersArray = msg.substring(14).split("§r§7, ");
        for (String player: playersArray) {
            if (player.contains(target)) {
                targetRanked = player;
            }
        }
        BountyHuntingHud.getInstance().getLines().set(3, "§cYour next target is " + targetRanked + ".");
        if (targetRanked.startsWith("§r§7")) targetRankColor = RankColor.GRAY;
        else if (targetRanked.startsWith("§r§a")) targetRankColor = RankColor.GREEN;
        else if (targetRanked.startsWith("§r§b")) targetRankColor = RankColor.AQUA;
        else if (targetRanked.startsWith("§r§6")) targetRankColor = RankColor.GOLD;
        else targetRankColor = RankColor.RED;
    }

    @SubscribeEvent
    public void onFightMessages(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
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
    public void onAttackEntity(AttackEntityEvent event) {
        if (Objects.equals(event.target.getName(), target) && !dead) {
            fightingTarget = true;
        }
    }

    @SubscribeEvent
    public void onRenderTarget(RenderPlayerEvent.Pre event) {
        if (!YedelConfig.getInstance().bountyHunting || !YedelConfig.getInstance().bhDisplay) return;
        EntityPlayer targetPlayer = event.entityPlayer;
        EntityPlayerSP player = UPlayer.getPlayer();
        if (
            Objects.equals(targetPlayer.getName(), target)
                && player.canEntityBeSeen(event.entityPlayer)
                && !targetPlayer.isInvisible()
        ) {
            double sneakingInc = targetPlayer.isSneaking() ? 0.0 : 0.3;
            String text = targetRankColor.colorCode + "Distance: " + (int) Math.floor(player.getDistanceToEntity(targetPlayer)) + " blocks";
            ((InvokerRender) event.renderer).yedelmod$invokeRenderLabel(targetPlayer, text, event.x, event.y + sneakingInc, event.z, 64);
        }
    }

    @SubscribeEvent
    public void onBlastRadiusDeath(ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText().startsWith("You were blown up by")) {
            target = null;
            dead = true;
            BountyHuntingHud.getInstance().getLines().set(3, "");
        }
    }

    @SubscribeEvent
    public void onRoundEnd(ClientChatReceivedEvent event) {
        if (!YedelConfig.getInstance().bountyHunting) return;
        String msg = event.message.getUnformattedText();
        Matcher peopleDeathMatcher = personBlewUpRegex.matcher(msg);
        while (peopleDeathMatcher.find()) {
            String personDied = peopleDeathMatcher.group("personThatBlewUp");
            if (Objects.equals(personDied, playerName)) {
                dead = true;
                target = null;
                BountyHuntingHud.getInstance().getLines().set(3, "");
            }
            if (Objects.equals(personDied, target) && fightingTarget) {
                Multithreading.schedule(() -> {
                    int pointIncrease = (int) Math.ceil(players.size() * 0.8);
                    if (dead) pointIncrease /= 2;
                    YedelConfig.getInstance().points += pointIncrease;
                    YedelConfig.getInstance().kills += 1;
                    BountyHuntingHud.getInstance().getLines().set(1, "§a" + YedelConfig.getInstance().points + " points (+" + pointIncrease + ")");
                    BountyHuntingHud.getInstance().getLines().set(2, "§a" + YedelConfig.getInstance().kills + " kills (+1)");
                    BountyHuntingHud.getInstance().getLines().set(3, "§cYou killed your target!");
                    if (YedelConfig.getInstance().bhSounds) {
                        USound.INSTANCE.playSoundStatic(Constants.plingSoundLocation, 1, 1.04F);
                    }
                    YedelConfig.getInstance().save();
                }, 500, TimeUnit.MILLISECONDS);
            }
        }
    }

    @SubscribeEvent
    public void onNickChange(ClientChatReceivedEvent event) {
        if (Objects.equals(event.message.getUnformattedText(), "Processing request. Please wait...") && YedelConfig.getInstance().bountyHunting) {
            UChat.chat("§6§l[BountyHunting] §ePlease set your nick with /setnick or in the config.");
        }
    }
}
