package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.event.events.DrawSlotEvent;
import at.yedel.yedelmod.hud.BedwarsXPHud;
import at.yedel.yedelmod.hud.MagicMilkTimeHud;
import at.yedel.yedelmod.mixins.net.minecraft.client.gui.inventory.AccessorGuiChest;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.typeutils.RenderUtils;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import dev.deftu.omnicore.common.OmniColor;
import dev.deftu.textile.SimpleTextHolder;
import net.hypixel.data.type.GameType;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.events.PacketEvent;
import org.polyfrost.oneconfig.api.event.v1.events.TickEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class BedwarsFeatures {
	private BedwarsFeatures() {}

	private static final BedwarsFeatures instance = new BedwarsFeatures();

	public static BedwarsFeatures getInstance() {
		return instance;
	}

	private int magicMilkTime;

	public void decrementMagicMilkTime() {
		magicMilkTime--;
	}

    private final int RED = OmniColor.Rgba.getRgba(246, 94, 94, 255);

	private static final Pattern tokenMessagePattern = Pattern.compile("\\+[0-9]+ tokens! .*");
	private static final Pattern slumberTicketMessagePattern = Pattern.compile("\\+[0-9]+ Slumber Tickets! .*");
	private static final List<String> comfyPillowMessages = new ArrayList<>();

	static {
		comfyPillowMessages.add("You are now carrying x1 Comfy Pillows, bring it back to your shop keeper!");
		comfyPillowMessages.add("You cannot return items to another team's Shopkeeper!");
		comfyPillowMessages.add("You cannot carry any more Comfy Pillows!");
		comfyPillowMessages.add("You died while carrying x1 Comfy Pillows!");
	}

	@Subscribe
	public void setBedwarsExperience(PacketEvent.Receive event) {
		if (event.getPacket() instanceof S1FPacketSetExperience) {
			float experience = ((S1FPacketSetExperience) event.getPacket()).func_149397_c();
			int bedwarsXP = (int) (experience * 5000);
			BedwarsXPHud.getInstance().string.append("§b").append(TextUtils.commafy(bedwarsXP)).append("§7/§a5,000");
			BedwarsXPHud.getInstance().relogic();
		}
	}

	@SubscribeEvent
	public void resetMagicMilkTime(PlayerUseItemEvent.Finish event) {
        if (Functions.isInGame(GameType.BEDWARS) && event.item.getItem() == Items.milk_bucket) {
			magicMilkTime = 30;
			MagicMilkTimeHud.getInstance().string.append("§b30§as");
			MagicMilkTimeHud.getInstance().relogic();
		}
	}

	private int ticks;

	@Subscribe
	public void decrementMagicMilkTime(TickEvent.Start event) {
		if (ticks % 20 == 0) {
			decrementMagicMilkTime();
			MagicMilkTimeHud.getInstance().string.append("§b").append(magicMilkTime).append("§as");
			MagicMilkTimeHud.getInstance().relogic();
		}
		ticks++;
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
	public void lightgreenifyTokenMessage(ChatEvent.Receive event) {
        if (YedelConfig.getInstance().lightGreenTokenMessages && Functions.isInGame(GameType.BEDWARS)) {
			String message = event.getFullyUnformattedMessage();
			Matcher matcher = tokenMessagePattern.matcher(message);
			while (matcher.find()) {
				event.setMessage(new SimpleTextHolder(event.getMessage().asString().replace("§2", "§a")));
			}
		}
	}

	@Subscribe
	public void hideSlumberTicketMessage(ChatEvent.Receive event) {
        if (YedelConfig.getInstance().hideSlumberTicketMessages && Functions.isInGame(GameType.BEDWARS)) {
			String message = event.getFullyUnformattedMessage();
			Matcher matcher = slumberTicketMessagePattern.matcher(message);
			while (matcher.find()) {
				event.cancelled = true;
			}
		}
	}

	@Subscribe
	public void hideItemPickupMessage(ChatEvent.Receive event) {
        if (YedelConfig.getInstance().hideItemPickupMessages && Functions.isInGame(GameType.BEDWARS)) {
			if (event.getFullyUnformattedMessage().startsWith("You picked up: ")) {
				event.cancelled = true;
			}
		}
	}

	@Subscribe
	public void hideSilverCoinCountMessage(ChatEvent.Receive event) {
		if (YedelConfig.getInstance().hideSilverCoinCount) {
			String message = event.getMessage().asString();
			if (message.startsWith("§r§aYou purchased §r§6") && message.contains("§r§7(+1 Silver Coin [")) {
				event.setMessage(new SimpleTextHolder(message.substring(0, message.indexOf(" §r§7(+1 Silver Coin ["))));
			}
		}
	}

	@Subscribe
	public void hideComfyPillowMessage(ChatEvent.Receive event) {
		if (YedelConfig.getInstance().hideComfyPillowMessages) {
			if (comfyPillowMessages.contains(event.getFullyUnformattedMessage())) {
				event.cancelled = true;
			}
		}
	}

	@Subscribe
	public void hideDreamersSoulFragmentMessage(ChatEvent.Receive event) {
		if (YedelConfig.getInstance().hideDreamerSoulFragmentMessages && Objects.equals(event.getFullyUnformattedMessage(), "+1 Dreamer's Soul Fragment!")) {
			event.cancelled = true;
		}
	}
}
