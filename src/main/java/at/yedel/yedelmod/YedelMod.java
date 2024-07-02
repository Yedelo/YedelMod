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
import at.yedel.yedelmod.update.UpdateManager;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.ScoreboardName;
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

        ClientCommandHandler.instance.registerCommand(new YedelUpdateCommand());
        ClientCommandHandler.instance.registerCommand(new ClearTextCommand());
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

        MinecraftForge.EVENT_BUS.register(AutoGuildWelcome.getInstance());
        MinecraftForge.EVENT_BUS.register(ChangeTitle.getInstance());
        MinecraftForge.EVENT_BUS.register(CustomText.getInstance());
        MinecraftForge.EVENT_BUS.register(DefusalHelper.getInstance());
        MinecraftForge.EVENT_BUS.register(DrawBookBackground.getInstance());
        MinecraftForge.EVENT_BUS.register(DropperGG.getInstance());
        MinecraftForge.EVENT_BUS.register(EasyAtlasVerdicts.getInstance());
        MinecraftForge.EVENT_BUS.register(Functions.getInstance().getEvents());
        MinecraftForge.EVENT_BUS.register(ItemSwings.getInstance());
        MinecraftForge.EVENT_BUS.register(LimboCreativeCheck.getInstance());
        MinecraftForge.EVENT_BUS.register(MarketSearch.getInstance());
        MinecraftForge.EVENT_BUS.register(PingResponse.getInstance());
        MinecraftForge.EVENT_BUS.register(SacrificeDisplay.getInstance());
        MinecraftForge.EVENT_BUS.register(ScoreboardName.getInstance().getEvents());
        MinecraftForge.EVENT_BUS.register(StrengthIndicators.getInstance());
        MinecraftForge.EVENT_BUS.register(TNTTag.getInstance());
        MinecraftForge.EVENT_BUS.register(YedelCheck.instance);

        new PlaytimeSchedule().startSchedule();

        ahSearchKeybind = new KeyBinding("AH search your held item", Keyboard.KEY_K, "YedelMod | Market Searches");
        bzSearchKeybind = new KeyBinding("BZ search your held item", Keyboard.KEY_L, "YedelMod | Market Searches");
        insufficientKeybind = new KeyBinding("Insufficient Evidence", 24, "YedelMod | Atlas");
        sufficientKeybind = new KeyBinding("Evidence Without Doubt", 25, "YedelMod | Atlas");
        ClientRegistry.registerKeyBinding(ahSearchKeybind);
        ClientRegistry.registerKeyBinding(bzSearchKeybind);
        ClientRegistry.registerKeyBinding(insufficientKeybind);
        ClientRegistry.registerKeyBinding(sufficientKeybind);
    }

    @EventHandler
    public void checkForUpdates(FMLLoadCompleteEvent event) {
        if (YedelConfig.getInstance().autoCheckUpdates) {
            UpdateManager.getInstance().checkVersion(YedelConfig.getInstance().getUpdateSource(), "notification");
        }
    }
}
