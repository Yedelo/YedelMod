package at.yedel.yedelmod.features.major;



import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.GameJoinEvent;
import at.yedel.yedelmod.mixins.client.renderer.entity.InvokerRender;
import at.yedel.yedelmod.utils.RankColor;
import gg.essential.api.utils.Multithreading;
import gg.essential.universal.UChat;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class TNTTag {
    private final ArrayList<String> players = new ArrayList<>();
    private final ItemStack playItemStack = new ItemStack(Item.getByNameOrId("minecraft:paper")).setStackDisplayName("§b§lPlay Again §r§7(Right Click)");
    private final ItemStack leaveGameStack = new ItemStack(Item.getByNameOrId("minecraft:bed")).setStackDisplayName("§r§c§lReturn To Lobby §r§7(Right Click)");
    private final ArrayList<String> lines = new ArrayList<>(4); // Display
    private final Pattern tagOtherRegex = Pattern.compile("You tagged ([^\\s!]+)!");
    private final Pattern personIsItRegex = Pattern.compile("^(\\w+) is IT!");
    private final Pattern peopleDeathPattern = Pattern.compile("^(\\w+) blew up!");
    private boolean playingTag;
    private String target;
    private String targetRanked;
    private boolean whoCheck;
    private boolean fightingTarget;
    private RankColor targetRankColor = RankColor.GRAY; // Prevents it from trying to render with a null color code
    private boolean dead;
    private String playerName;

    public TNTTag() {
        lines.add("§c§lBounty §f§lHunting");
        lines.add("§a" + YedelConfig.points + " points");
        lines.add("§a" + YedelConfig.kills + " kills");
        lines.add("");
    }

    @SubscribeEvent
    public void onRenderTagLayoutEdtior(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!(event.gui instanceof GuiContainer)) return;
        if (Objects.equals(((GuiContainer) event.gui).inventorySlots.getSlot(0).inventory.getName(), "Layout Editor - TNT Tag") && YedelConfig.bhClickables) {
            int width = event.gui.width;
            FontRenderer fontRenderer = minecraft.fontRendererObj;
            fontRenderer.drawStringWithShadow("§r§b§lPlay Again §r§7(Right Click) §r| Slot " + YedelConfig.playAgainItem, (float) (width - 165) / 2, 30, 16777215);
            fontRenderer.drawStringWithShadow("§r§c§lReturn To Lobby §r§7(Right Click) §r| Slot " + YedelConfig.returnToLobbyItem, (float) (width - 203) / 2, 30 + fontRenderer.FONT_HEIGHT + 2, 16777215);
            if (YedelConfig.playAgainItem == YedelConfig.returnToLobbyItem) {
                fontRenderer.drawStringWithShadow("§c§lYour clickables are conflicting! Change them in the config.", (float) (width - 351) / 2, 30 + 2 * (fontRenderer.FONT_HEIGHT + 2), 16777215);
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (!playingTag || !YedelConfig.bountyHunting) return;
        int y = YedelConfig.bhDisplayY;
        FontRenderer fontRenderer = minecraft.fontRendererObj;
        for (String line: lines) {
            fontRenderer.drawStringWithShadow(line, YedelConfig.bhDisplayX, y, 16777215);
            y += fontRenderer.FONT_HEIGHT + 2;
        }
    }

    @SubscribeEvent
    public void onTNTTagJoin(GameJoinEvent.TNTJoinEvent event) {
        playingTag = true;
        if (!YedelConfig.bountyHunting) return;
        playerName = minecraft.thePlayer.getName();
        dead = false;
        target = null;
        lines.set(0, "§c§lBounty §f§lHunting");
        lines.set(1, "§a" + YedelConfig.points + " points");
        lines.set(2, "§a" + YedelConfig.kills + " kills");
        lines.set(3, "");
        if (YedelConfig.bhFirst) {
            UChat.chat(
                    "&6&l[BountyHunting] §3If this is your first time using this mod and you're nicked, or you've changed your nick, you will have to set your nick with §n/setnick§r§3."
            );
            YedelConfig.bhFirst = false;
            YedelConfig.save();
        }
    }

    @SubscribeEvent
    public void onRoundStarted(ClientChatReceivedEvent event) {
        if (!YedelConfig.bountyHunting || !playingTag || !event.message.getUnformattedText().endsWith("has started!"))
            return;
        players.clear();
        if (YedelConfig.bhClickables) {
            minecraft.thePlayer.inventory.setInventorySlotContents(YedelConfig.playAgainItem - 1, playItemStack);
            minecraft.thePlayer.inventory.setInventorySlotContents(YedelConfig.returnToLobbyItem - 1, leaveGameStack);
        }
        for (NetworkPlayerInfo playerInfo: minecraft.getNetHandler().getPlayerInfoMap()) {
            players.add(playerInfo.getGameProfile().getName());
        }
        players.remove(playerName);
        players.remove(YedelConfig.nick);
        target = players.get((int) Math.floor(Math.random() * players.size()));
        whoCheck = true;
        UChat.say("/who");
        if (YedelConfig.bhSounds) minecraft.thePlayer.playSound("random.successful_hit", 10, 0.8F);
        lines.set(1, "§a" + YedelConfig.points + " points");
        lines.set(2, "§a" + YedelConfig.kills + " kills");
    }

    @SubscribeEvent
    public void onWhoMessage(ClientChatReceivedEvent event) {
        String msg = event.message.getFormattedText();
        if (!event.message.getUnformattedText().startsWith("ONLINE: ") || !whoCheck || !YedelConfig.bountyHunting || !playingTag)
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
        String msg = event.message.getUnformattedText();
        if (!playingTag || !YedelConfig.bountyHunting) return;
        Matcher tagOtherMatcher = tagOtherRegex.matcher(msg);
        while (tagOtherMatcher.find()) {
            if (Objects.equals(tagOtherMatcher.group(1), target)) {
                fightingTarget = true;
            }
        }

        Matcher personIsItMatcher = personIsItRegex.matcher(msg);
        while (personIsItMatcher.find()) {
            if (Objects.equals(personIsItMatcher.group(1), target) && !dead) {
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
        if (!playingTag || !YedelConfig.bountyHunting || !YedelConfig.bhDisplay) return;
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
    public void onInteractHoldingClickables(PlayerInteractEvent event) {
        if (!playingTag || !YedelConfig.bhClickables) return;
        ItemStack item = minecraft.thePlayer.getHeldItem();
        if (item == null) return;
        String itemName = item.getDisplayName();
        if (Objects.equals(itemName, "§b§lPlay Again §r§7(Right Click)")) {
            UChat.say("/play tnt_tntag");
            event.setCanceled(true);
        }
        else if (Objects.equals(itemName, "§r§c§lReturn To Lobby §r§7(Right Click)")) {
            UChat.say("/lobby");
            event.setCanceled(true);
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
        String msg = event.message.getUnformattedText();
        if (!playingTag || !YedelConfig.bountyHunting) return;
        Matcher peopleDeathMatcher = peopleDeathPattern.matcher(msg);
        while (peopleDeathMatcher.find()) {
            String personDied = peopleDeathMatcher.group(1);
            if (Objects.equals(personDied, playerName)) {
                dead = true;
                target = null;
                lines.set(3, "");
            }
            if (Objects.equals(personDied, target) && fightingTarget) {
                Multithreading.schedule(() -> {
                    // Half points if you died while killing your target
                    int pointIncrease = (int) Math.ceil(dead ? players.size() * 0.8 : players.size() * 0.8 / 2);
                    YedelConfig.points += pointIncrease;
                    YedelConfig.kills += 1;
                    lines.set(1, "§a" + YedelConfig.points + " points (+" + pointIncrease + ")");
                    lines.set(2, "§a" + YedelConfig.kills + " kills (+1)");
                    lines.set(3, "§cYou killed your target!");
                    if (YedelConfig.bhSounds) minecraft.thePlayer.playSound("random.successful_hit", 10, 1.04F);
                    YedelConfig.save();
                }, 500, TimeUnit.MILLISECONDS);
            }
        }
    }

    @SubscribeEvent
    public void onNickChange(ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText() == "Processing request. Please wait..." && YedelConfig.bountyHunting) {
            UChat.chat("&6&l[BountyHunting] §ePlease set your nick with /setnick or in the config.");
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
