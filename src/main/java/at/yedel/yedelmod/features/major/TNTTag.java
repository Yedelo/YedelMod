package at.yedel.yedelmod.features.major;



import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.GameJoinEvent;
import at.yedel.yedelmod.mixins.net.minecraft.client.renderer.entity.InvokerRender;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.messages;
import at.yedel.yedelmod.utils.RankColor;
import at.yedel.yedelmod.utils.ThreadManager;
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
    private static final TNTTag instance = new TNTTag();

    public static TNTTag getInstance() {
        return instance;
    }

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
        lines.add("§a" + YedelConfig.getInstance().points + " points");
        lines.add("§a" + YedelConfig.getInstance().kills + " kills");
        lines.add("");
    }

    @SubscribeEvent
    public void onRenderTagLayoutEdtior(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!(event.gui instanceof GuiContainer)) return;
        if (((GuiContainer) event.gui).inventorySlots.getSlot(0).inventory.getName().equals("Layout Editor - TNT Tag") && YedelConfig.getInstance().bhClickables) {
            int width = event.gui.width;
            FontRenderer fontRenderer = minecraft.fontRendererObj;
            fontRenderer.drawStringWithShadow("§r§b§lPlay Again §r§7(Right Click) §r| Slot " + YedelConfig.getInstance().playAgainItem, (float) (width - 165) / 2, 30, 16777215);
            fontRenderer.drawStringWithShadow("§r§c§lReturn To Lobby §r§7(Right Click) §r| Slot " + YedelConfig.getInstance().returnToLobbyItem, (float) (width - 203) / 2, 30 + fontRenderer.FONT_HEIGHT + 2, 16777215);
            if (YedelConfig.getInstance().playAgainItem == YedelConfig.getInstance().returnToLobbyItem) {
                fontRenderer.drawStringWithShadow("§c§lYour clickables are conflicting! Change them in the config.", (float) (width - 351) / 2, 30 + 2 * (fontRenderer.FONT_HEIGHT + 2), 16777215);
            }
        }
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

    @SubscribeEvent
    public void onTNTTagJoin(GameJoinEvent.TNTJoinEvent event) {
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
            Chat.display(messages.firstTime);
            YedelConfig.getInstance().bhFirst = false;
            YedelConfig.getInstance().save();
        }
    }

    @SubscribeEvent
    public void onRoundStarted(ClientChatReceivedEvent event) {
        if (!YedelConfig.getInstance().bountyHunting || !playingTag || !event.message.getUnformattedText().endsWith("has started!"))
            return;
        players.clear();
        if (YedelConfig.getInstance().bhClickables) {
            minecraft.thePlayer.inventory.setInventorySlotContents(YedelConfig.getInstance().playAgainItem - 1, playItemStack);
            minecraft.thePlayer.inventory.setInventorySlotContents(YedelConfig.getInstance().returnToLobbyItem - 1, leaveGameStack);
        }
        for (NetworkPlayerInfo playerInfo: minecraft.getNetHandler().getPlayerInfoMap()) {
            players.add(playerInfo.getGameProfile().getName());
        }
        players.remove(playerName);
        players.remove(YedelConfig.getInstance().nick);
        target = players.get((int) Math.floor(Math.random() * players.size()));
        whoCheck = true;
        Chat.command("who");
        if (YedelConfig.getInstance().bhSounds) minecraft.thePlayer.playSound("random.successful_hit", 10, 0.8F);
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
        String msg = event.message.getUnformattedText();
        if (!playingTag || !YedelConfig.getInstance().bountyHunting) return;
        Matcher tagOtherMatcher = tagOtherRegex.matcher(msg);
        while (tagOtherMatcher.find()) {
            if (tagOtherMatcher.group(1).equals(target)) {
                fightingTarget = true;
            }
        }

        Matcher personIsItMatcher = personIsItRegex.matcher(msg);
        while (personIsItMatcher.find()) {
            if (personIsItMatcher.group(1).equals(target) && !dead) {
                fightingTarget = false;
            }
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.target.getName().equals(target) && !dead) {
            fightingTarget = true;
        }
    }

    @SubscribeEvent
    public void onRenderTarget(RenderPlayerEvent.Pre event) {
        if (!playingTag || !YedelConfig.getInstance().bountyHunting || !YedelConfig.getInstance().bhDisplay) return;
        EntityPlayer targetPlayer = event.entityPlayer;
        EntityPlayerSP player = minecraft.thePlayer;
        if (
                targetPlayer.getName().equals(target)
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
        if (!playingTag || !YedelConfig.getInstance().bhClickables) return;
        ItemStack item = minecraft.thePlayer.getHeldItem();
        if (item == null) return;
        String itemName = item.getDisplayName();
        if (itemName.equals("§b§lPlay Again §r§7(Right Click)")) {
            Chat.command("play tnt_tntag");
            event.setCanceled(true);
        }
        else if (itemName.equals("§r§c§lReturn To Lobby §r§7(Right Click)")) {
            Chat.command("lobby");
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
        if (!playingTag || !YedelConfig.getInstance().bountyHunting) return;
        Matcher peopleDeathMatcher = peopleDeathPattern.matcher(msg);
        while (peopleDeathMatcher.find()) {
            String personDied = peopleDeathMatcher.group(1);
            if (personDied.equals(playerName)) {
                dead = true;
                target = null;
                lines.set(3, "");
            }
            if (personDied.equals(target) && fightingTarget) {
                ThreadManager.scheduleOnce(() -> {
                    // Half points if you died while killing your target
                    int pointIncrease = (int) Math.ceil(dead ? players.size() * 0.8 : players.size() * 0.8 / 2);
                    YedelConfig.getInstance().points += pointIncrease;
                    YedelConfig.getInstance().kills += 1;
                    lines.set(1, "§a" + YedelConfig.getInstance().points + " points (+" + pointIncrease + ")");
                    lines.set(2, "§a" + YedelConfig.getInstance().kills + " kills (+1)");
                    lines.set(3, "§cYou killed your target!");
                    if (YedelConfig.getInstance().bhSounds)
                        minecraft.thePlayer.playSound("random.successful_hit", 10, 1.04F);
                    YedelConfig.getInstance().save();
                }, 500);
            }
        }
    }

    @SubscribeEvent
    public void onNickChange(ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText().equals("Processing request. Please wait...") && YedelConfig.getInstance().bountyHunting) {
            Chat.display(messages.pleaseChangeNick);
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
