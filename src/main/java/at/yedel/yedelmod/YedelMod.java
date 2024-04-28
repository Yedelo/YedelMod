package at.yedel.yedelmod;



import java.io.File;

import at.yedel.yedelmod.commands.ClearTextCommand;
import at.yedel.yedelmod.commands.LimboCommand;
import at.yedel.yedelmod.commands.LimboCreativeCommand;
import at.yedel.yedelmod.commands.MoveHuntingTextCommand;
import at.yedel.yedelmod.commands.MoveTextCommand;
import at.yedel.yedelmod.commands.PingCommand;
import at.yedel.yedelmod.commands.PlaytimeCommand;
import at.yedel.yedelmod.commands.SetNickCommand;
import at.yedel.yedelmod.commands.SetTextCommand;
import at.yedel.yedelmod.commands.SetTitleCommand;
import at.yedel.yedelmod.commands.SimulateChatCommand;
import at.yedel.yedelmod.commands.YedelCommand;
import at.yedel.yedelmod.commands.YedelMessageCommand;
import at.yedel.yedelmod.commands.YedelUpdateCommand;
import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.AutoGuildWelcome;
import at.yedel.yedelmod.features.CustomText;
import at.yedel.yedelmod.features.DrawBookBackground;
import at.yedel.yedelmod.features.DropperGG;
import at.yedel.yedelmod.features.LimboCreativeCheck;
import at.yedel.yedelmod.features.PlaytimeSchedule;
import at.yedel.yedelmod.features.SacrificeDisplay;
import at.yedel.yedelmod.features.major.DefusalHelper;
import at.yedel.yedelmod.features.major.EasyAtlasVerdicts;
import at.yedel.yedelmod.features.major.MarketSearch;
import at.yedel.yedelmod.features.major.StrengthIndicators;
import at.yedel.yedelmod.features.major.TNTTag;
import at.yedel.yedelmod.features.major.ping.PingResponse;
import at.yedel.yedelmod.features.modern.ChangeTitle;
import at.yedel.yedelmod.features.modern.ItemSwings;
import at.yedel.yedelmod.mixins.net.minecraft.client.AccessorMinecraft;
import at.yedel.yedelmod.update.UpdateManager;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.ScoreboardName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    public static final String logo = "§8§l- §9§lYedel§7§lMod §8§l-";
    public static final Minecraft minecraft = Minecraft.getMinecraft();
    @Mod.Instance
    public static YedelMod instance;
    public static Logger logger = LogManager.getLogger("YedelMod");
    public static File modConfigurationFactory;

    public static KeyBinding ahSearch;
    public static KeyBinding bzSearch;
    public static KeyBinding insufficient;
    public static KeyBinding sufficient;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        modConfigurationFactory = event.getModConfigurationDirectory();
        String serverName = System.getProperty("yedelmod.server.name");
        String serverPort = System.getProperty("yedelmod.server.port");
        if (serverName != null) {
            AccessorMinecraft minecraft$accessible = ((AccessorMinecraft) minecraft);
            logger.info("Loaded server name from JVM argument yedelmod.server.name: " + serverName);
            minecraft$accessible.setServerName(serverName);
            if (serverPort != null) {
                logger.info("Loaded server port from JVM argument yedelmod.server.port: " + serverPort);
                minecraft$accessible.setServerPort(Integer.parseInt(serverPort));
            }
            else minecraft$accessible.setServerPort(25565);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        YedelConfig.instance.preload();

        ClientCommandHandler.instance.registerCommand(new YedelUpdateCommand());
        ClientCommandHandler.instance.registerCommand(new ClearTextCommand());
        // ClientCommandHandler.instance.registerCommand(new HotSwapCommand());
        ClientCommandHandler.instance.registerCommand(new LimboCommand());
        ClientCommandHandler.instance.registerCommand(new LimboCreativeCommand());
        ClientCommandHandler.instance.registerCommand(new MoveHuntingTextCommand());
        ClientCommandHandler.instance.registerCommand(new MoveTextCommand());
        ClientCommandHandler.instance.registerCommand(new PingCommand());
        ClientCommandHandler.instance.registerCommand(new PlaytimeCommand());
        ClientCommandHandler.instance.registerCommand(new SetNickCommand());
        ClientCommandHandler.instance.registerCommand(new SetTextCommand());
        ClientCommandHandler.instance.registerCommand(new SetTitleCommand());
        ClientCommandHandler.instance.registerCommand(new SimulateChatCommand());
        ClientCommandHandler.instance.registerCommand(new YedelCommand());
        ClientCommandHandler.instance.registerCommand(new YedelMessageCommand());

        MinecraftForge.EVENT_BUS.register(AutoGuildWelcome.instance);
        MinecraftForge.EVENT_BUS.register(ChangeTitle.instance);
        MinecraftForge.EVENT_BUS.register(CustomText.instance);
        MinecraftForge.EVENT_BUS.register(DefusalHelper.instance);
        MinecraftForge.EVENT_BUS.register(DrawBookBackground.instance);
        MinecraftForge.EVENT_BUS.register(DropperGG.instance);
        MinecraftForge.EVENT_BUS.register(EasyAtlasVerdicts.instance);
        MinecraftForge.EVENT_BUS.register(Functions.instance);
        MinecraftForge.EVENT_BUS.register(ItemSwings.instance);
        MinecraftForge.EVENT_BUS.register(LimboCreativeCheck.instance);
        MinecraftForge.EVENT_BUS.register(MarketSearch.instance);
        MinecraftForge.EVENT_BUS.register(PingResponse.instance);
        MinecraftForge.EVENT_BUS.register(SacrificeDisplay.instance);
        MinecraftForge.EVENT_BUS.register(ScoreboardName.instance);
        MinecraftForge.EVENT_BUS.register(StrengthIndicators.instance);
        MinecraftForge.EVENT_BUS.register(TNTTag.instance);
        MinecraftForge.EVENT_BUS.register(YedelCheck.instance);

        new PlaytimeSchedule().startSchedule();

        ahSearch = new KeyBinding("AH search your held item", Keyboard.KEY_K, "YedelMod | Market Searches");
        bzSearch = new KeyBinding("BZ search your held item", Keyboard.KEY_L, "YedelMod | Market Searches");
        insufficient = new KeyBinding("Insufficient Evidence", 24, "YedelMod | Atlas");
        sufficient = new KeyBinding("Evidence Without Doubt", 25, "YedelMod | Atlas");
        ClientRegistry.registerKeyBinding(ahSearch);
        ClientRegistry.registerKeyBinding(bzSearch);
        ClientRegistry.registerKeyBinding(insufficient);
        ClientRegistry.registerKeyBinding(sufficient);
    }

    @Mod.EventHandler
    public void checkForUpdates(FMLLoadCompleteEvent event) {
        if (YedelConfig.autoCheckUpdates) {
            UpdateManager.instance.checkVersion(YedelConfig.getUpdateSource(), "notification");
        }
    }

}
