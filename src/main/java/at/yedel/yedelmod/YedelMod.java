package at.yedel.yedelmod;



import java.io.File;

import at.yedel.yedelmod.commands.YedelCommand;
import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.AutoGuildWelcome;
import at.yedel.yedelmod.features.CustomText;
import at.yedel.yedelmod.features.DrawBookBackground;
import at.yedel.yedelmod.features.DropperGG;
import at.yedel.yedelmod.features.FavoriteServerButton;
import at.yedel.yedelmod.features.PlaytimeSchedule;
import at.yedel.yedelmod.features.major.DefusalHelper;
import at.yedel.yedelmod.features.major.EasyAtlasVerdicts;
import at.yedel.yedelmod.features.major.MarketSearch;
import at.yedel.yedelmod.features.major.StrengthIndicators;
import at.yedel.yedelmod.features.major.TNTTag;
import at.yedel.yedelmod.features.major.ping.PingResponse;
import at.yedel.yedelmod.features.modern.ChangeTitle;
import at.yedel.yedelmod.features.modern.ItemSwings;
import at.yedel.yedelmod.handlers.HypixelManager;
import at.yedel.yedelmod.handlers.YedelModPacketHandler;
import at.yedel.yedelmod.update.UpdateManager;
import at.yedel.yedelmod.utils.Functions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import org.lwjgl.input.Keyboard;



@Mod(
	modid = YedelMod.modid,
	name = YedelMod.name,
	version = YedelMod.version,
	clientSideOnly = true,
	guiFactory = "at.yedel.yedelmod.gui.forgeconfig.GuiFactory" // Overriden by main config (vigilance)
)
public class YedelMod {
	public static final String modid = "yedelmod";
	public static final String name = "YedelMod";
	public static final String version = "1.1.0";
	public static final Minecraft minecraft = Minecraft.getMinecraft();
	@Instance
	private static YedelMod instance;

	public static YedelMod getInstance() {
		return instance;
	}

	private File modConfigurationFactory;

	public File getModConfigurationFactory() {
		return modConfigurationFactory;
	}

	private KeyBinding ahSearchKeybind;

	public KeyBinding getAhSearchKeybind() {
		return ahSearchKeybind;
	}

	private KeyBinding bzSearchKeybind;

	public KeyBinding getBzSearchKeybind() {
		return bzSearchKeybind;
	}

	private KeyBinding insufficientKeybind;

	public KeyBinding getInsufficientKeybind() {
		return insufficientKeybind;
	}

	private KeyBinding sufficientKeybind;

	public KeyBinding getSufficientKeybind() {
		return sufficientKeybind;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		modConfigurationFactory = event.getModConfigurationDirectory();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		YedelConfig.getInstance().preload();

		ClientCommandHandler.instance.registerCommand(new YedelCommand());

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(AutoGuildWelcome.getInstance());
		MinecraftForge.EVENT_BUS.register(ChangeTitle.getInstance());
		MinecraftForge.EVENT_BUS.register(CustomText.getInstance());
		MinecraftForge.EVENT_BUS.register(DefusalHelper.getInstance());
		MinecraftForge.EVENT_BUS.register(DrawBookBackground.getInstance());
		MinecraftForge.EVENT_BUS.register(DropperGG.getInstance());
		MinecraftForge.EVENT_BUS.register(EasyAtlasVerdicts.getInstance());
		MinecraftForge.EVENT_BUS.register(FavoriteServerButton.getInstance());
		MinecraftForge.EVENT_BUS.register(Functions.getInstance().getEvents());
		MinecraftForge.EVENT_BUS.register(ItemSwings.getInstance());
		MinecraftForge.EVENT_BUS.register(MarketSearch.getInstance());
		MinecraftForge.EVENT_BUS.register(PingResponse.getInstance());
		MinecraftForge.EVENT_BUS.register(StrengthIndicators.getInstance());
		MinecraftForge.EVENT_BUS.register(TNTTag.getInstance());
		MinecraftForge.EVENT_BUS.register(YedelCheck.getInstance());

		new PlaytimeSchedule().startSchedule();

		ahSearchKeybind = new KeyBinding("AH search your held item", Keyboard.KEY_K, "YedelMod | Market Searches");
		bzSearchKeybind = new KeyBinding("BZ search your held item", Keyboard.KEY_L, "YedelMod | Market Searches");
		insufficientKeybind = new KeyBinding("Insufficient Evidence", Keyboard.KEY_O, "YedelMod | Atlas");
		sufficientKeybind = new KeyBinding("Evidence Without Doubt", Keyboard.KEY_P, "YedelMod | Atlas");
		ClientRegistry.registerKeyBinding(ahSearchKeybind);
		ClientRegistry.registerKeyBinding(bzSearchKeybind);
		ClientRegistry.registerKeyBinding(insufficientKeybind);
		ClientRegistry.registerKeyBinding(sufficientKeybind);

		HypixelManager.getInstance().setup();
	}

	@EventHandler
	public void checkForUpdates(FMLLoadCompleteEvent event) {
		if (YedelConfig.getInstance().autoCheckUpdates) {
			UpdateManager.getInstance().checkVersion(YedelConfig.getInstance().getUpdateSource(), "notification");
		}
	}

	@SubscribeEvent
	public void onServerConnect(ClientConnectedToServerEvent event) {
		event.manager.channel().pipeline().addBefore("packet_handler", "yedelmod_packet_handler", new YedelModPacketHandler());
	}
}
