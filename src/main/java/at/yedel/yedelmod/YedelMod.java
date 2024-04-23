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
import at.yedel.yedelmod.update.CheckForUpdatesCommand;
import at.yedel.yedelmod.update.UpdateManager;
import at.yedel.yedelmod.utils.ScoreboardName;
import gg.essential.api.EssentialAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
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
    public static final String version = "1.0.2";
    public static final String logo = "§8§l- §9§lYedel§7§lMod §8§l-";
    public static final Minecraft minecraft = Minecraft.getMinecraft();
    @Mod.Instance
    public static YedelMod instance;
    public static File modConfigurationFactory;

    public static KeyBinding ahSearch;
    public static KeyBinding bzSearch;
    public static KeyBinding insufficient;
    public static KeyBinding sufficient;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        modConfigurationFactory = event.getModConfigurationDirectory();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        YedelConfig.instance.preload();

        EssentialAPI.getCommandRegistry().registerCommand(new CheckForUpdatesCommand("yedelupdate"));
        EssentialAPI.getCommandRegistry().registerCommand(new ClearTextCommand("cleartext"));
        EssentialAPI.getCommandRegistry().registerCommand(new LimboCommand("yedelli"));
        EssentialAPI.getCommandRegistry().registerCommand(new LimboCommand("yli"));
        EssentialAPI.getCommandRegistry().registerCommand(new LimboCommand("li"));
        EssentialAPI.getCommandRegistry().registerCommand(new LimboCreativeCommand("yedellimbocreative"));
        EssentialAPI.getCommandRegistry().registerCommand(new LimboCreativeCommand("limbogmc"));
        EssentialAPI.getCommandRegistry().registerCommand(new LimboCreativeCommand("lgmc"));
        EssentialAPI.getCommandRegistry().registerCommand(new MoveTextCommand("movetext"));
        EssentialAPI.getCommandRegistry().registerCommand(new MoveHuntingTextCommand("movehuntingtext"));
        EssentialAPI.getCommandRegistry().registerCommand(new PingCommand("yping"));
        EssentialAPI.getCommandRegistry().registerCommand(new PingCommand("yp"));
        EssentialAPI.getCommandRegistry().registerCommand(new PlaytimeCommand("yedelplaytime"));
        EssentialAPI.getCommandRegistry().registerCommand(new PlaytimeCommand("yedelpt"));
        EssentialAPI.getCommandRegistry().registerCommand(new PlaytimeCommand("ypt"));
        EssentialAPI.getCommandRegistry().registerCommand(new SetNickCommand("setnick"));
        EssentialAPI.getCommandRegistry().registerCommand(new SetTextCommand("settext"));
        EssentialAPI.getCommandRegistry().registerCommand(new SetTitleCommand("settitle"));
        EssentialAPI.getCommandRegistry().registerCommand(new SimulateChatCommand("simulatechat"));
        EssentialAPI.getCommandRegistry().registerCommand(new SimulateChatCommand("simc"));
        EssentialAPI.getCommandRegistry().registerCommand(new YedelCommand("yedel"));
        EssentialAPI.getCommandRegistry().registerCommand(new YedelMessageCommand("yedelmessage"));

        MinecraftForge.EVENT_BUS.register(AutoGuildWelcome.instance);
        MinecraftForge.EVENT_BUS.register(ChangeTitle.instance);
        MinecraftForge.EVENT_BUS.register(CustomText.instance);
        MinecraftForge.EVENT_BUS.register(DefusalHelper.instance);
        MinecraftForge.EVENT_BUS.register(DrawBookBackground.instance);
        MinecraftForge.EVENT_BUS.register(DropperGG.instance);
        MinecraftForge.EVENT_BUS.register(EasyAtlasVerdicts.instance);
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
