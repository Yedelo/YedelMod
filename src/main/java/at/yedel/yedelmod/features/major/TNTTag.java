package at.yedel.yedelmod.features.major;



import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.mixins.net.minecraft.client.renderer.entity.InvokerRender;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.RankColor;
import at.yedel.yedelmod.utils.ThreadManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class TNTTag {
    private static final TNTTag instance = new TNTTag();

    public static TNTTag getInstance() {
        return instance;
    }

    private final ArrayList<String> players = new ArrayList<>();
    private final ArrayList<String> lines = new ArrayList<>(4); // Display
    private final Pattern youTaggedPersonRegex = Pattern.compile("You tagged (?<personThatYouTagged>[a-zA-Z0-9_]*)!");
    private final Pattern personIsItRegex = Pattern.compile("(?<personThatIsIt>[a-zA-Z0-9_]*) is IT!");
    private final Pattern personBlewUpRegex = Pattern.compile("(?<personThatBlewUp>[a-zA-Z0-9_]*) blew up!");
    private boolean playingTag;
    private String target;
    private String targetRanked;
    private boolean whoCheck;
    private boolean fightingTarget;
    private RankColor targetRankColor = RankColor.GRAY; // Prevents it from trying to render with a null color code
    private boolean dead;
    private String playerName;

    private TNTTag() {
        lines.add("§c§lBounty §f§lHunting");
        lines.add("§a" + YedelConfig.getInstance().points + " points");
        lines.add("§a" + YedelConfig.getInstance().kills + " kills");
        lines.add("");
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (!playingTag || !YedelConfig.getInstance().bountyHunting) return;
        int y = YedelConfig.getInstance().bhDisplayY;
        FontRenderer fontRenderer = minecraft.fontRendererObj;
        for (String line: lines) {
            fontRenderer.drawStringWithShadow(line, YedelConfig.getInstance().bhDisplayX, y, 16777215);
            y += fontRenderer.FONT_HEIGHT + 2;
        }
    }

    public void onTNTTagJoin() {
        playingTag = true;
        if (!YedelConfig.getInstance().bountyHunting) return;
        playerName = minecraft.thePlayer.getName();
        dead = false;
        target = null;
        lines.set(0, "§c§lBounty §f§lHunting");
        lines.set(1, "§a" + YedelConfig.getInstance().points + " points");
        lines.set(2, "§a" + YedelConfig.getInstance().kills + " kills");
        lines.set(3, "");
        if (YedelConfig.getInstance().bhFirst) {
            Chat.display(Messages.firstTime);
            YedelConfig.getInstance().bhFirst = false;
            YedelConfig.getInstance().save();
        }
    }

    @SubscribeEvent
    public void onRoundStarted(ClientChatReceivedEvent event) {
        if (!YedelConfig.getInstance().bountyHunting || !playingTag || !event.message.getUnformattedText().endsWith("has started!"))
            return;
        players.clear();
        for (NetworkPlayerInfo playerInfo: minecraft.getNetHandler().getPlayerInfoMap()) {
            players.add(playerInfo.getGameProfile().getName());
        }
        players.remove(playerName);
        players.remove(YedelConfig.getInstance().nick);
        target = players.get((int) Math.floor(Math.random() * players.size()));
        whoCheck = true;
        Chat.command("who");
		if (YedelConfig.getInstance().bhSounds) Functions.safelyPlaySound("random.successful_hit", 10, 0.8F);
        lines.set(1, "§a" + YedelConfig.getInstance().points + " points");
        lines.set(2, "§a" + YedelConfig.getInstance().kills + " kills");
    }

    @SubscribeEvent
    public void onWhoMessage(ClientChatReceivedEvent event) {
        String msg = event.message.getFormattedText();
        if (!event.message.getUnformattedText().startsWith("ONLINE: ") || !whoCheck || !YedelConfig.getInstance().bountyHunting || !playingTag)
            return;
        whoCheck = false;
        event.setCanceled(true);
        String[] playersArray = msg.substring(14).split("§r§7, ");
        for (String player: playersArray) {
            if (player.contains(target)) {
                targetRanked = player;
            }
        }
        lines.set(3, "§cYour next target is " + targetRanked + ".");
        if (targetRanked.startsWith("§r§7")) targetRankColor = RankColor.GRAY;
        else if (targetRanked.startsWith("§r§a")) targetRankColor = RankColor.GREEN;
        else if (targetRanked.startsWith("§r§b")) targetRankColor = RankColor.AQUA;
        else if (targetRanked.startsWith("§r§6")) targetRankColor = RankColor.GOLD;
        else targetRankColor = RankColor.RED;
    }

    @SubscribeEvent
    public void onFightMessages(ClientChatReceivedEvent event) {
        if (!playingTag || !YedelConfig.getInstance().bountyHunting) return;
        String msg = event.message.getUnformattedText();
        Matcher tagOtherMatcher = youTaggedPersonRegex.matcher(msg);
        while (tagOtherMatcher.find()) {
            if (Objects.equals(tagOtherMatcher.group("personThatYouTagged"), target)) {
                fightingTarget = true;
            }
        }

        Matcher personIsItMatcher = personIsItRegex.matcher(msg);
        while (personIsItMatcher.find()) {
            if (Objects.equals(personIsItMatcher.group("personThatIsIt"), target) && ! dead) {
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
        if (!playingTag || !YedelConfig.getInstance().bountyHunting || !YedelConfig.getInstance().bhDisplay) return;
        EntityPlayer targetPlayer = event.entityPlayer;
        EntityPlayerSP player = minecraft.thePlayer;
        if (
                Objects.equals(targetPlayer.getName(), target)
                        && player.canEntityBeSeen(event.entityPlayer)
                        && !targetPlayer.isInvisible()
        ) {
            double sneakingInc = targetPlayer.isSneaking() ? 0.0 : 0.3;
            String text = targetRankColor.colorCode + "Distance: " + (int) Math.floor(player.getDistanceToEntity(targetPlayer)) + " blocks";
            ((InvokerRender) event.renderer).yedelmod$invokeRenderLabel(targetPlayer, text, event.x, event.y + sneakingInc, event.z, 64); // make this 64 later change
        }
    }

    @SubscribeEvent
    public void onBlastRadiusDeath(ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText().startsWith("You were blown up by") && playingTag) {
            target = null;
            dead = true;
            lines.set(3, "");
        }
    }

    @SubscribeEvent
    public void onRoundEnd(ClientChatReceivedEvent event) {
        if (!playingTag || !YedelConfig.getInstance().bountyHunting) return;
        String msg = event.message.getUnformattedText();
        Matcher peopleDeathMatcher = personBlewUpRegex.matcher(msg);
        while (peopleDeathMatcher.find()) {
            String personDied = peopleDeathMatcher.group("personThatBlewUp");
            if (Objects.equals(personDied, playerName)) {
                dead = true;
                target = null;
                lines.set(3, "");
            }
            if (Objects.equals(personDied, target) && fightingTarget) {
                ThreadManager.scheduleOnce(() -> {
                    // Half points if you died while killing your target
                    int pointIncrease = (int) Math.ceil(dead ? players.size() * 0.8 : players.size() * 0.8 / 2);
                    YedelConfig.getInstance().points += pointIncrease;
                    YedelConfig.getInstance().kills += 1;
                    lines.set(1, "§a" + YedelConfig.getInstance().points + " points (+" + pointIncrease + ")");
                    lines.set(2, "§a" + YedelConfig.getInstance().kills + " kills (+1)");
                    lines.set(3, "§cYou killed your target!");
                    if (YedelConfig.getInstance().bhSounds)
						Functions.safelyPlaySound("random.successful_hit", 10, 1.04F);
                    YedelConfig.getInstance().save();
                }, 500);
            }
        }
    }

    @SubscribeEvent
    public void onNickChange(ClientChatReceivedEvent event) {
        if (Objects.equals(event.message.getUnformattedText(), "Processing request. Please wait...") && YedelConfig.getInstance().bountyHunting) {
            Chat.display(Messages.pleaseChangeNick);
        }
    }

    @SubscribeEvent
    public void onLeaveTag(WorldEvent.Unload event) {
        lines.set(0, "");
        lines.set(1, "");
        lines.set(2, "");
        lines.set(3, "");
    }
}
