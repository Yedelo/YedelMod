package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.TextUtils;
import com.google.common.collect.ImmutableList;
import net.hypixel.data.type.GameType;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import org.polyfrost.compose.render.PolyColor;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.events.PacketEvent;
import org.polyfrost.oneconfig.api.event.v1.events.TickEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

import java.util.Objects;
import java.util.regex.Pattern;



public class BedwarsFeatures {
	private static final BedwarsFeatures INSTANCE = new BedwarsFeatures();

	public static BedwarsFeatures getInstance() {
		return INSTANCE;
	}

	// 246, 94, 94, 255
	private static final int RED = new PolyColor(-631202).getArgb();
	private static final Pattern TOKEN_MESSAGE_PATTERN = Pattern.compile("\\+\\d+ tokens! \\(.*\\)");
	private static final Pattern BEDWARS_XP_MESSAGE_PATTERN = Pattern.compile("\\+\\d+ Bed Wars XP \\(.*\\)");
	private static final Pattern PUNCH_DEPOSIT_MESSAGE_PATTERN = Pattern.compile("Deposited x\\d+ (.*) into (Ender|Team) Chest! \\(\\d+ Total\\)");
	private static final Pattern SLUMBER_TICKET_MESSAGE_PATTERN = Pattern.compile("\\+\\d+ Slumber Tickets \\(.*\\)");
	private static final ImmutableList<String> COMFY_PILLOW_MESSAGES = ImmutableList.<String>builder()
		.add("You are now carrying x1 Comfy Pillows, bring it back to your shop keeper!")
		.add("You cannot return items to another team's Shopkeeper!")
		.add("You cannot carry any more Comfy Pillows!")
		.add("You died while carrying x1 Comfy Pillows!")
		.build();

	private boolean inBedwars;
	private boolean hasExperience;
	private String hudXPText;
	private int magicMilkTime;
	private String magicMilkTimeText;
	private int ticks;

	private BedwarsFeatures() {
		HypixelModAPI.getInstance().registerHandler(ClientboundLocationPacket.class, this::handleLocationPacket);
	}

	private void handleLocationPacket(ClientboundLocationPacket packet) {
		inBedwars = packet.getServerType().isPresent() && packet.getServerType().get() == GameType.BEDWARS && !packet.getLobbyName().isPresent();
	}

	@Subscribe
	public void setBedwarsExperience(PacketEvent.Receive event) {
		if (event.getPacket() instanceof ClientboundSetExperiencePacket packet) {
			float experience = packet.getExperienceProgress();
			hasExperience = experience > 0;
			int bedwarsXP = (int) (experience * 5000);
			hudXPText = "§b" + TextUtils.commafy(bedwarsXP) + "§7/§a5,000";
		}
	}

	public void handleMilk() {
		if (inBedwars) {
			magicMilkTime = 30;
			magicMilkTimeText = "§b30§as";
		}
	}

	@Subscribe
	public void decrementMagicMilkTime(TickEvent.Start event) {
		if (ticks % 20 == 0) {
			decrementMagicMilkTime();
			magicMilkTimeText = magicMilkTime + "s";
		}
		ticks++;
	}

	//@TODO readd defusal helper
	//	@Subscribe
	//	public void renderRedstoneHighlights(DrawSlotEvent event) {
	//		if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().bedwarsDefusalHelper) {
	//			ItemStack stack = event.getSlot().getStack();
	//			if (stack == null) {
	//				return;
	//			}
	//			if (stack.getItem() == Items.redstone) {
	//				GuiContainer guiContainer = event.getGuiContainer();
	//				if (guiContainer instanceof GuiChest) {
	//					if (Objects.equals(((AccessorGuiChest) guiContainer).getLowerChestInventory().getName(), "§cC4 (Click §4§lREDSTONE§c)")) {
	//						RenderUtils.highlightItem(event.getSlot(), RED);
	//					}
	//				}
	//			}
	//		}
	//	}

	@Subscribe
	public void modifyBedwarsChat(ChatEvent.Receive event) {
		if (YedelConfig.getInstance().enabled && inBedwars) {
			String message = event.getFullyUnformattedMessage();

			if (YedelConfig.getInstance().hideTokenMessages && Objects.equals(message, "Tokens just earned DOUBLED as a Guild Level Reward!")) {
				event.cancelled = true;
			}
			if (TOKEN_MESSAGE_PATTERN.matcher(message).find()) {
				if (YedelConfig.getInstance().hideTokenMessages) {
					event.cancelled = true;
				}
				else if (YedelConfig.getInstance().lightGreenTokenMessages) {
					//@TODO light green token messages
					// event.message = event.message.getFormattedText().replace("§2", "§a"));
				}
			}

			hideOnPattern(event, message, YedelConfig.getInstance().hideBedwarsXPMessages, BEDWARS_XP_MESSAGE_PATTERN);
			hideOnPattern(event, message, YedelConfig.getInstance().hidePunchDepositMessages, PUNCH_DEPOSIT_MESSAGE_PATTERN);
			hideOnPattern(event, message, YedelConfig.getInstance().hideSlumberTicketMessages, SLUMBER_TICKET_MESSAGE_PATTERN);

			if (message.startsWith("You purchased")) {
				if (YedelConfig.getInstance().hideItemPurchaseMessages) {
					event.cancelled = true;
				}
				else if (YedelConfig.getInstance().hideSilverCoinCount && message.contains("(+1 Silver Coin [")) {
					// @TODO bro this kyori component stuff sucks
					// event.message = new UTextComponent(message.substring(0, message.indexOf(" (+1 Silver Coin [")));
				}
			}

			if (YedelConfig.getInstance().hideComfyPillowMessages && COMFY_PILLOW_MESSAGES.contains(message)) {
				event.cancelled = true;
			}

			if (YedelConfig.getInstance().hideDreamerSoulFragmentMessages && message.equals("+1 Dreamer's Soul Fragment!")) {
				event.cancelled = true;
			}
		}
	}

	private void hideOnPattern(ChatEvent.Receive event, String message, boolean configOption, Pattern pattern) {
		if (configOption && pattern.matcher(message).find()) {
			event.cancelled = true;
		}
	}

	public boolean isInBedwars() {
		return inBedwars;
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
}
