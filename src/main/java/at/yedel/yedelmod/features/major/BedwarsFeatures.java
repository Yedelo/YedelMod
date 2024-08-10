package at.yedel.yedelmod.features.major;



import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.DrawSlotEvent;
import at.yedel.yedelmod.events.PacketEvent;
import at.yedel.yedelmod.handlers.HypixelManager;
import at.yedel.yedelmod.mixins.net.minecraft.client.gui.inventory.AccessorGuiChest;
import at.yedel.yedelmod.utils.typeutils.RenderUtils;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class BedwarsFeatures {
	private BedwarsFeatures() {}

	private static final BedwarsFeatures instance = new BedwarsFeatures();

	public static BedwarsFeatures getInstance() {
		return instance;
	}

	private static final Pattern tokenMessagePattern = Pattern.compile("\\+[0-9]+ tokens! .*");
	private static final Pattern slumberTicketMessagePattern = Pattern.compile("\\+[0-9]+ Slumber Tickets! .*");
	private static final List<String> comfyPillowMessages = new ArrayList<>();

	static {
		comfyPillowMessages.add("You are now carrying x1 Comfy Pillows, bring it back to your shop keeper!");
		comfyPillowMessages.add("You cannot return items to another team's Shopkeeper!");
		comfyPillowMessages.add("You cannot carry any more Comfy Pillows!");
		comfyPillowMessages.add("You died while carrying x1 Comfy Pillows!");
	}

	private final int RED = new Color(246, 94, 94, 255).getRGB();
	private boolean hasExperience;

	public boolean hasExperience() {
		return hasExperience;
	}

	private String hudXPText;

	public String getHudXPText() {
		return hudXPText;
	}

	@SubscribeEvent
	public void onExperiencePacket(PacketEvent.ReceiveEvent event) {
		if (event.getPacket() instanceof S1FPacketSetExperience) {
			float experience = ((S1FPacketSetExperience) event.getPacket()).func_149397_c();
			hasExperience = experience > 0;
			int bedwarsXP = (int) (experience * 5000);
			hudXPText = "XP: §b" + TextUtils.commafy(bedwarsXP) + "§7/§a5,000";
		}
	}

	@SubscribeEvent
	public void onRenderRedstones(DrawSlotEvent event) {
		if (!YedelConfig.getInstance().defusalHelper) return;
		ItemStack stack = event.getSlot().getStack();
		if (stack == null) return;
		if (stack.getItem() == Items.redstone) {
			GuiContainer guiContainer = event.getGuiContainer();
			if (guiContainer instanceof GuiChest) {
				if (Objects.equals(((AccessorGuiChest) guiContainer).getLowerChestInventory().getName(), "§cC4 (Click §4§lREDSTONE§c)")) {
					RenderUtils.highlightItem(event.getSlot(), RED);
				}
			}
		}
	}

	@SubscribeEvent
	public void onTokenMessage(ClientChatReceivedEvent event) {
		if (YedelConfig.getInstance().lightGreenTokenMessages && HypixelManager.getInstance().getInBedwars()) {
			String message = event.message.getUnformattedText();
			Matcher matcher = tokenMessagePattern.matcher(message);
			while (matcher.find()) {
				event.message = new ChatComponentText(event.message.getFormattedText().replace("§2", "§a"));
			}
		}
	}

	@SubscribeEvent
	public void onSlumberTicketMessage(ClientChatReceivedEvent event) {
		if (YedelConfig.getInstance().hideSlumberTicketMessages && HypixelManager.getInstance().getInBedwars()) {
			String message = event.message.getUnformattedText();
			Matcher matcher = slumberTicketMessagePattern.matcher(message);
			while (matcher.find()) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onItemPickupMessage(ClientChatReceivedEvent event) {
		if (YedelConfig.getInstance().hideItemPickupMessages && HypixelManager.getInstance().getInBedwars()) {
			if (event.message.getUnformattedText().startsWith("You picked up: ")) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onSilverCoinCountMessage(ClientChatReceivedEvent event) {
		if (YedelConfig.getInstance().hideSilverCoinCount) {
			String message = event.message.getFormattedText();
			if (message.startsWith("§r§aYou purchased §r§6") && message.contains("§r§7(+1 Silver Coin [")) {
				event.message = new ChatComponentText(message.substring(0, message.indexOf(" §r§7(+1 Silver Coin [")));
			}
		}
	}

	@SubscribeEvent
	public void onComfyPillowMessages(ClientChatReceivedEvent event) {
		if (YedelConfig.getInstance().hideComfyPillowMessages) {
			if (comfyPillowMessages.contains(event.message.getUnformattedText())) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onDreamersSoulFragmentMessage(ClientChatReceivedEvent event) {
		if (YedelConfig.getInstance().hideDreamerSoulFragmentMessages && Objects.equals(event.message.getUnformattedText(), "+1 Dreamer's Soul Fragment!")) {
			event.setCanceled(true);
		}
	}
}
