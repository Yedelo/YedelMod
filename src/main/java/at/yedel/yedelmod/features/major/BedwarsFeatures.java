package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.event.events.DrawSlotEvent;
import at.yedel.yedelmod.mixins.AccessorGuiChest;
import at.yedel.yedelmod.utils.typeutils.RenderUtils;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import net.hypixel.data.type.GameType;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class BedwarsFeatures {
	private static final BedwarsFeatures INSTANCE = new BedwarsFeatures();
	private static final int RED = new OneColor(246, 94, 94, 255).getRGB();
	private static final Pattern TOKEN_MESSAGE_PATTERN = Pattern.compile("\\+[0-9]+ tokens! .*");
	private static final Pattern SLUMBER_TICKET_MESSAGE_PATTERN = Pattern.compile("\\+[0-9]+ Slumber Tickets! .*");
	private static final List<String> COMFY_PILLOW_MESSAGES = new ArrayList<>();

	static {
		COMFY_PILLOW_MESSAGES.add("You are now carrying x1 Comfy Pillows, bring it back to your shop keeper!");
		COMFY_PILLOW_MESSAGES.add("You cannot return items to another team's Shopkeeper!");
		COMFY_PILLOW_MESSAGES.add("You cannot carry any more Comfy Pillows!");
		COMFY_PILLOW_MESSAGES.add("You died while carrying x1 Comfy Pillows!");
	}

	private boolean inBedwars;
	private boolean hasExperience;
	private String hudXPText;
	private int magicMilkTime;
	private String magicMilkTimeText;
	private int ticks;

	private BedwarsFeatures() {
		HypixelModAPI.getInstance().registerHandler(ClientboundLocationPacket.class, this::handleLocationPacket);
	}

	public static BedwarsFeatures getInstance() {
		return INSTANCE;
	}

	public boolean isInBedwars() {
		return inBedwars;
	}

	private void handleLocationPacket(ClientboundLocationPacket packet) {
		inBedwars =
			packet.getServerType().isPresent() && packet.getServerType().get() == GameType.BEDWARS && !packet.getLobbyName().isPresent();
	}

	public boolean hasExperience() {
		return hasExperience;
	}

	public String getHudXPText() {
		return hudXPText;
	}

	public int getMagicMilkTime() {
		return magicMilkTime;
	}

	public void decrementMagicMilkTime() {
		magicMilkTime--;
	}

	public String getMagicMilkTimeText() {
		return magicMilkTimeText;
	}

	@Subscribe
	public void setBedwarsExperience(ReceivePacketEvent event) {
		if (event.packet instanceof S1FPacketSetExperience) {
			float experience = ((S1FPacketSetExperience) event.packet).func_149397_c();
			hasExperience = experience > 0;
			int bedwarsXP = (int) (experience * 5000);
			hudXPText = "§b" + TextUtils.commafy(bedwarsXP) + "§7/§a5,000";
		}
	}

	@SubscribeEvent
	public void resetMagicMilkTime(PlayerUseItemEvent.Finish event) {
		if (event.item.getItem() == Items.milk_bucket && inBedwars) {
			magicMilkTime = 30;
			magicMilkTimeText = "§b30§as";
		}
	}

	@Subscribe
	public void decrementMagicMilkTime(TickEvent event) {
		if (event.stage == Stage.START) {
			if (ticks % 20 == 0) {
				decrementMagicMilkTime();
				magicMilkTimeText = "§b" + magicMilkTime + "§as";
			}
			ticks++;
		}

	}

	@Subscribe
	public void renderRedstoneHighlights(DrawSlotEvent event) {
		if (!YedelConfig.getInstance().bedwarsDefusalHelper) return;
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

	@Subscribe
	public void lightgreenifyTokenMessage(ChatReceiveEvent event) {
		if (YedelConfig.getInstance().lightGreenTokenMessages && inBedwars) {
			String message = event.message.getUnformattedText();
			Matcher matcher = TOKEN_MESSAGE_PATTERN.matcher(message);
			while (matcher.find()) {
				event.message = new UTextComponent(event.message.getFormattedText().replace("§2", "§a"));
			}
		}
	}

	@Subscribe
	public void hideSlumberTicketMessage(ChatReceiveEvent event) {
		if (YedelConfig.getInstance().hideSlumberTicketMessages && inBedwars) {
			String message = event.message.getUnformattedText();
			Matcher matcher = SLUMBER_TICKET_MESSAGE_PATTERN.matcher(message);
			while (matcher.find()) {
				event.isCancelled = true;
			}
		}
	}

	@Subscribe
	public void hideItemPickupMessage(ChatReceiveEvent event) {
		if (YedelConfig.getInstance().hideItemPickupMessages && inBedwars) {
			if (event.message.getUnformattedText().startsWith("You picked up: ")) {
				event.isCancelled = true;
			}
		}
	}

	@Subscribe
	public void hideSilverCoinCountMessage(ChatReceiveEvent event) {
		if (YedelConfig.getInstance().hideSilverCoinCount) {
			String message = event.message.getFormattedText();
			if (message.startsWith("§r§aYou purchased §r§6") && message.contains("§r§7(+1 Silver Coin [")) {
				event.message = new UTextComponent(message.substring(0, message.indexOf(" §r§7(+1 Silver Coin [")));
			}
		}
	}

	@Subscribe
	public void hideComfyPillowMessage(ChatReceiveEvent event) {
		if (YedelConfig.getInstance().hideComfyPillowMessages) {
			if (COMFY_PILLOW_MESSAGES.contains(event.message.getUnformattedText())) {
				event.isCancelled = true;
			}
		}
	}

	@Subscribe
	public void hideDreamersSoulFragmentMessage(ChatReceiveEvent event) {
		if (YedelConfig.getInstance().hideDreamerSoulFragmentMessages && Objects.equals(event.message.getUnformattedText(), "+1 Dreamer's Soul Fragment!")) {
			event.isCancelled = true;
		}
	}
}
