package at.yedel.yedelmod.config;



import at.yedel.yedelmod.features.major.EasyAtlasVerdicts;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.utils.Constants;
    //? if v0 {
/*import at.yedel.yedelmod.hud.CustomTextHud;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.config.elements.OptionPage;
import cc.polyfrost.oneconfig.config.migration.VigilanceMigrator;
import cc.polyfrost.oneconfig.config.migration.VigilanceName;
import cc.polyfrost.oneconfig.libs.universal.UDesktop;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import cc.polyfrost.oneconfig.platform.Platform;
import cc.polyfrost.oneconfig.utils.Notifications;
*///?} else {
import com.mojang.blaze3d.platform.InputConstants;
import org.polyfrost.compose.render.PolyColor;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.*;
import org.polyfrost.oneconfig.api.notifications.v1.Notifications;
import org.polyfrost.oneconfig.api.platform.v1.DesktopHelper;
import org.polyfrost.oneconfig.api.ui.v1.keybind.KeybindHelper;
import org.polyfrost.oneconfig.api.ui.v1.keybind.OneConfigKeybind;
import org.polyfrost.oneconfig.utils.v1.dsl.ScreensKt;
    //?}
//? if forge {
//import net.minecraftforge.fml.common.Loader;
//?} else
import net.fabricmc.loader.api.FabricLoader;


import java.net.URI;



//~ config_bridge
public class YedelConfig extends Config {
    private static final YedelConfig INSTANCE = new YedelConfig();

    public static YedelConfig getInstance() {
        return INSTANCE;
    }

    private static final URI BOUNTY_HUNTING_VIDEO = URI.create("https://www.youtube.com/watch?v=-z_AZR35ozI");

    private void addDependentOption(String dependent, String option) {
        addDependency(option, dependent);
    }

    private void addDependentOptions(String dependent, String... options) {
        for (String option : options) {
            addDependentOption(dependent, option);
        }
    }

    private YedelConfig() {
        //? if v0 {
        /*super(
            new Mod(
                "YedelMod",
                ModType.UTIL_QOL,
                "/assets/yedelmod/yedelmod.png"
            ),
            "yedelmod.json",
            true,
            true
        );
        initialize();

        registerKeyBind(insufficientEvidenceKeybind, EasyAtlasVerdicts.getInstance()::submitInsufficientEvidenceVerdict);
        registerKeyBind(evidenceWithoutDoubtKeybind, EasyAtlasVerdicts.getInstance()::submitEvidentWithoutDoubtVerdict);

        for (String internalOption : new String[] {
            "playtimeMinutes",
            "firstTime",
            "bountyHuntingPoints",
            "bountyHuntingKills",
            "firstTimeBountyHunting"
        }) {
            hideIf(internalOption, () -> true);
        }
        *///?} else {
        super("yedelmod", "/assets/yedelmod/yedelmod.png", "YedelMod", Category.QOL);
        //?}

        addDependentOption("autoWelcomeGuildMembers", "guildWelcomeMessage");
        addDependentOptions("customHitParticles", "customParticleType", "particleYOffset", "randomParticleType", "onlySpawnCustomParticlesOnPlayers");
        addDependentOption("dropperAutoGG", "autoGGDelay");
        addDependentOption("regexChatFilter", "regexChatFilterPattern");
        addDependentOption("randomPlaceholder", "randomPlaceholderText");
        addDependentOptions("skywarsStrengthIndicators", "strengthColor", "showSelfStrength", "strengthIndicatorOffset");
        addDependentOptions("easyAtlasVerdicts", "insufficientEvidenceKeybind", "evidenceWithoutDoubtKeybind");
        addDependentOptions("bountyHunting", "highlightTargetAndShowDistance", "playHuntingSounds", "playSelection", "playKill", "bountyHuntingHud");
    }

    public void open() {
        //? if v0
        //openGui();
        //? else
        ScreensKt.openUI(this);
    }

    // Start of config
    // Start of visible config

    /* General */

    @Switch(
        title = "Enabled",
        description = "Global toggle for the mod."
    )
    public boolean enabled = true;

    /* Features */

    // Features

    @Switch(
        title = "Regex Chat Filter",
        description = "Use a customizable regular expression to filter chat.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean regexChatFilter = false;
    
    @Text(
        title = "Regex Chat Filter Pattern",
        description = "The pattern to use for regex chat filtering.",
        category = "Features",
        subcategory = "Features"
    )
    public String regexChatFilterPattern = "";

    @Switch(
        title = "Random Placeholder",
        description = "Type a customizable placeholder to replace it with a random string from a UUID.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean randomPlaceholder = false;

    @Text(
        title = "Random Placeholder Text",
        description = "When this is typed in chat, it will be replaced with a random string. Be careful not to use short placeholders to not spam excessively.",
        category = "Features",
        subcategory = "Features",
        placeholder = "//r"
    )
    public String randomPlaceholderText = "//r";

    @Switch(
        title = "Auto Welcome Guild Members",
        description = "Automatically welcomes new guild members with a customizable message.",
        category = "Features",
        subcategory = "Hypixel"
    )
    public boolean autoWelcomeGuildMembers = true;

    @Text(
        title = "Guild Welcome Message",
        description = "Message for new guild members. Use [player] for the new player.",
        category = "Features",
        subcategory = "Hypixel"
    )
    public String guildWelcomeMessage = "Welcome, [player]!";

    @Info(
        title = "This only says gg at the end of the game, not when you finish.",
        category = "Features",
        subcategory = "Hypixel"
    )
    private transient int info$1 = 1;

    @Switch(
        title = "Dropper AutoGG",
        description = "AutoGG for Hypixel dropper.",
        category = "Features",
        subcategory = "Hypixel"
    )
    // very soft dependency that shouldn't mean very much
    public boolean dropperAutoGG = /*? if v0 {*/ /*Loader.isModLoaded("autogg") *//*?} else {*/FabricLoader.getInstance().isModLoaded("autogg")/*?}*/;

    @Slider(
        title = "AutoGG Delay",
        description = "Delay for AutoGG, measured in seconds.",
        category = "Features",
        subcategory = "Hypixel",
        min = 0,
        max = 5,
        step = 1
    )
    public int autoGGDelay = 0;

    @Switch(
        title = "SkyWars Strength Indicators",
        description = "Shows people's strength above their nametags with customizable colors",
        category = "Features",
        subcategory = "Hypixel"
    )
    public boolean skywarsStrengthIndicators = true;

    //? if legacy {
    /*@Dropdown(
        title = "Strength Color",
        description = "Color for strength indicators",
        category = "Features",
        subcategory = "Hypixel",
        options = {
            "Dark Red",
            "Red",
            "Gold",
            "Yellow",
            "Dark Green",
            "Green",
            "Aqua",
            "Dark Aqua",
            "Dark Blue",
            "Blue",
            "Pink",
            "Purple",
            "White",
            "Gray",
            "Dark Gray",
            "Black"
        }
    )
    public int strengthColor = 1;

    *///?} else {
    @Color(
        title = "Strength Color",
        description = "Color for strength indicators",
        category = "Features",
        subcategory = "Hypixel",
        alpha = false
    )
    public PolyColor strengthColor = new PolyColor(0xFF5555);
    //?}

    @Switch(
        title = "Show Self Strength",
        description = "If your own nametag is rendered (PolyNametag), this will show your own strength indicators.",
        category = "Features",
        subcategory = "Hypixel"
    )
    public boolean showSelfStrength = true;

    @Slider(
        title = "Strength Indicator Offset (in hundredths)",
        description = "The Y offset (in hundredths) to render the strength indicator label at.",
        category = "Features",
        subcategory = "Hypixel",
        min = -100,
        max = 100,
        step = 1
    )
    public int strengthIndicatorOffset = 0;

    @Switch(
        title = "Limbo Creative Mode",
        description = "Automatically gives creative mode in Hypixel limbo. Use /yedel lgmc or rejoin if it doesn't work the first time.",
        category = "Features",
        subcategory = "Hypixel"
    )
    public boolean limboCreativeMode = true;

    @Switch(
        title = "Easy Atlas Verdicts",
        description = "Adds hotkeys for submitting Atlas verdicts.",
        category = "Features",
        subcategory = "Hypixel"
    )
    public boolean easyAtlasVerdicts = false;

    @Keybind(
        title = "Insufficient Evidence Verdict",
        description = "Submits an \"Insufficient Evidence\" verdict in Atlas.",
        category = "Features",
        subcategory = "Hypixel"
    )
    public OneConfigKeybind insufficientEvidenceKeybind =
        //? if v0 {
        //new OneConfigKeybind(UKeyboard.KEY_O);
        //?} else
        KeybindHelper.builder().key(InputConstants.KEY_O).action(EasyAtlasVerdicts.getInstance()::submitInsufficientEvidenceVerdict).register();

    @Keybind(
        title = "Evident Without Doubt Verdict",
        description = "Submits an \"Evident Without Doubt\" verdict in Atlas.",
        category = "Features",
        subcategory = "Hypixel"
    )
    public OneConfigKeybind evidentWithoutDoubtKeybind =
        //? if v0 {
        //new OneConfigKeybind(UKeyboard.KEY_P);
        //?} else
        KeybindHelper.builder().key(InputConstants.KEY_P).action(EasyAtlasVerdicts.getInstance()::submitEvidentWithoutDoubtVerdict).register();

    //? if v0 {
    /*@HUD(
        title = "Custom Text HUD",
        category = "Features"
    )
    public CustomTextHud customTextHud = new CustomTextHud();
    *///?}    
    /* Commands */

    //~ if v1 'text' -> 'description' {
    @Info(
        title = "Description of this mod's subcommands, all under /yedel.",
        category = "Commands"
    )
    private transient int header$2 = 1;

    // Index

    @Info(
        title = "Format: - command (any aliases) [arguments]",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int info$4 = 1;

    @Info(
        title = "Example: - simulatechat (simc) [description] -> /yedel simc Hi!",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int info$5 = 1;

    @Info(
        title = "/yedel (/yedelmod)",
        description = "The main command, hosting all subcommands. When used with no arguments, opens this config screen.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$1 = 1;

    @Info(
        title = "- cleardescription",
        description = "Clears the currently set display description.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$2 = 1;

    @Info(
        title = "- constants",
        description = "Shows mod constants such as the project version and build information.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$3 = 1;

    @Info(
        title = "- formatting",
        description = "Shows a formatting guide with color and style codes.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$4 = 1;

    @Info(
        title = "- limbo (li)",
        description = "Sends an illegal chat character, which disconnects you on most servers and sends you to limbo-like areas on some. No longer works on Hypixel, use /limbo instead.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$5 = 1;

    @Info(
        title = "- limbocreative (limbogmc, lgmc)",
        description = "Gives you creative mode in Hypixel's limbo, given certain checks are passed.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$6 = 1;

    @Info(
        title = "- ping [method]",
        description = "Shows your ping to the server in chat, using several methods. Without an argument, uses the default method specified below.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$7 = 1;
    //~}

    @Dropdown(
        title = "Ping Method",
        description =
            "Command: Enters a random command and waits for the unknown command response. Works on almost all servers." +
                "\nTab: Sends a tab completion packet and waits for the response. Works on all servers." +
                "\nStats (default): Sends a statistics packet and waits for the response. Works on all servers." +
                "\nHypixel: Uses the Hypixel ping packet and waits for the response. Only works on Hypixel." +
                "\nServer list: Gets the ping displayed previously on the server list. Doesn't work on singleplayer or if you used Direct Connect.",
        category = "Commands",
        subcategory = "Index",
        options = {"Command", "Tab", "Stats", "Hypixel", "Server list"}
    )
    public int pingMethod = 2;

    //~ if v1 'text' -> 'description' {
    @Info(
        title = "- playtime (pt)",
        description = "Shows your total playtime (while playing on servers) in hours and minutes.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$8 = 1;

    @Info(
        title = "- setnick [nick]",
        description = "Sets your nick for Bounty Hunting to not select yourself as the target.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$9 = 1;

    @Info(
        title = "- setdescription [description]",
        description = "Sets the display description, supporting color codes with ampersands (&).",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$10 = 1;

    @Info(
        title = "- settitle [title]",
        description = "Sets the title of the game window.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$11 = 1;

    @Info(
        title = "- simulatechat (simc) [description]",
        description = "Simulates a chat message, also supports color codes with ampersands (&).",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$12 = 1;

    @Info(
        title = "- yedelmessage (message)",
        description = "Shows messages from me about the mod. These can be anything from tips to bug notices.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$14 = 1;
    //~}

    /* TNT Tag */

    @Info(
        title = "Features relating to TNT Tag, mainly bounty hunting.",
        category = "TNT Tag"
    )
    private transient int header$4 = 1;

    // General

    @Switch(
        title = "Bounty Hunting",
        description = "Adds a bounty hunting minigame to TNT Tag.",
        category = "TNT Tag"
    )
    public boolean bountyHunting = true;

    @Button(
        title = "Reset Stats",
        category = "TNT Tag",
        text = "Reset"
    )
    private void resetConfirmation() {
        Notifications.builder("Bounty Hunting", "Are you sure you want to reset your stats? (press %k)").onClick(() -> {
                bountyHuntingPoints = 0;
                bountyHuntingKills = 0;
                TNTTagFeatures.getInstance().getDisplayLines().set(1, "§c0 points (reset)");
                TNTTagFeatures.getInstance().getDisplayLines().set(2, "§c0 kills (reset)");
            Notifications.send("Bounty Hunting", "Reset stats!");
            }
        ).send();
    }

    @Button(
        title = "Video",
        description = "This is a complicated feature, watch my video if you need help!",
        category = "TNT Tag",
        text = "Open video"
    )
    private void watchVideo() {
        //~ if v1 'UDesktop' -> 'DesktopHelper'
        if (!DesktopHelper.browse(BOUNTY_HUNTING_VIDEO)) {
            Notifications.send("YedelMod", "Couldn't open video!");
        }
    }

    @Text(
        title = "Current Nick",
        description = "If you're playing nicked, set your nick or you might become the target.",
        category = "TNT Tag",
        placeholder = "Replace"
    )
    public String currentNick = "";

    // Features

    @Switch(
        title = "Highlight Target and Show Distance",
        description = "Highlights the target and shows their distance.",
        category = "TNT Tag",
        subcategory = "Features"
    )
    public boolean highlightTargetAndShowDistance = true;

    @Color(
        title = "Distance Label Color",
        description = "The color of the distance label.",
        category = "TNT Tag",
        subcategory = "Features",
        alpha = false
    )
    public PolyColor distanceLabelColor = new PolyColor(0xFF5555);

    @Switch(
        title = "Play Sounds for Target Selections and Kills",
        description = "Use the buttons below to test these sounds.",
        category = "TNT Tag",
        subcategory = "Features"
    )
    public boolean playHuntingSounds = true;

    @Button(
        title = "Target Selection Sound",
        description = "Sound: random.successful_hit at 0.8 pitch.",
        category = "TNT Tag",
        subcategory = "Features",
        text = "Play sound"
    )
    private void playSelection() {
        Constants.playPingSound(1, 0.8f);
    }

    @Button(
        title = "Kill Sound",
        description = "Sound: random.successful_hit at 1.04 pitch.",
        category = "TNT Tag",
        subcategory = "Features",
        text = "Play sound"
    )
    private void playKill() {
        Constants.playPingSound(1, 1.04f);
    }

    // End of visible config

    // Hidden variables for data

    //? if v0
    //@Number(title = "playtimeMinutes", category = "General", subcategory = "", min = 0, max = Integer.MAX_VALUE)
    //? else
    @Include
    public int playtimeMinutes = 0;

    //? if v0
    //@Number(title = "bountyHuntingPoints", category = "General", subcategory = "", min = 0, max = Integer.MAX_VALUE)
    //? else
    @Include
    public int bountyHuntingPoints = 0;

    //? if v0
    //@Number(title = "bountyHuntingKills", category = "General", subcategory = "", min = 0, max = Integer.MAX_VALUE)
    //? else
    @Include
    public int bountyHuntingKills = 0;

    //? if v0
    //@Switch(title = "firstTimeBountyHunting", category = "General", subcategory = "")
    //? else
    @Include
    public boolean firstTimeBountyHunting = true;

    // End of config
}
