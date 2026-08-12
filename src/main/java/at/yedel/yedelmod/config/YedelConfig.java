package at.yedel.yedelmod.config;



import at.yedel.yedelmod.features.major.EasyAtlasVerdicts;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.utils.Constants;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.loader.api.FabricLoader;
import org.polyfrost.compose.render.PolyColor;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.*;
import org.polyfrost.oneconfig.api.notifications.v1.Notifications;
import org.polyfrost.oneconfig.api.platform.v1.DesktopHelper;
import org.polyfrost.oneconfig.api.ui.v1.keybind.KeybindHelper;
import org.polyfrost.oneconfig.api.ui.v1.keybind.OneConfigKeybind;

import java.net.URI;



// @TODO readd custom hit particles
public class YedelConfig extends Config {
    private static final YedelConfig INSTANCE = new YedelConfig();

    public static YedelConfig getInstance() {
        return INSTANCE;
    }

    private static final URI BOUNTY_HUNTING_VIDEO = URI.create("https://www.youtube.com/watch?v=-z_AZR35ozI");

    private YedelConfig() {
        super("yedelmod", "/assets/yedelmod/yedelmod.png", "YedelMod", Category.QOL);
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

    @DependsOn("regexChatFilter")
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

    @DependsOn("randomPlaceholder")
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

    @DependsOn("autoWelcomeGuildMembers")
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
    public boolean dropperAutoGG = FabricLoader.getInstance().isModLoaded("autogg");

    @DependsOn("dropperAutoGG")
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

    @DependsOn("skywarsStrengthIndicators")
    @Color(
        title = "Strength Color",
        description = "Color for strength indicators",
        category = "Features",
        subcategory = "Hypixel",
        alpha = false
    )
    public PolyColor strengthColor = new PolyColor(0xFF5555);

    @DependsOn("skywarsStrengthIndicators")
    @Switch(
        title = "Show Self Strength",
        description = "Whether or not to show your own strength indicators.",
        category = "Features",
        subcategory = "Hypixel"
    )
    public boolean showSelfStrength = true;

    @DependsOn("skywarsStrengthIndicators")
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

    @DependsOn("easyAtlasVerdicts")
    @Keybind(
        title = "Insufficient Evidence Verdict",
        description = "Submits an \"Insufficient Evidence\" verdict in Atlas.",
        category = "Features",
        subcategory = "Hypixel"
    )
    public OneConfigKeybind insufficientEvidenceKeybind =
        KeybindHelper.builder().key(InputConstants.KEY_O).action(EasyAtlasVerdicts.getInstance()::submitInsufficientEvidenceVerdict).register();

    @DependsOn("easyAtlasVerdicts")
    @Keybind(
        title = "Evident Without Doubt Verdict",
        description = "Submits an \"Evident Without Doubt\" verdict in Atlas.",
        category = "Features",
        subcategory = "Hypixel"
    )
    public OneConfigKeybind evidentWithoutDoubtKeybind =
        KeybindHelper.builder().key(InputConstants.KEY_P).action(EasyAtlasVerdicts.getInstance()::submitEvidentWithoutDoubtVerdict).register();

    /* Commands */

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
        title = "Example: - simulatechat (simc) [text] -> /yedel simc Hi!",
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
        title = "- cleartext",
        description = "Clears the currently set display text.",
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

    @Dropdown(
        title = "Ping Method",
        description =
            """
                Command: Enters a random command and waits for the unknown command response. Works on almost all servers.
                Tab: Sends a tab completion packet and waits for the response. Works on all servers.
                Stats (default): Sends a statistics packet and waits for the response. Works on all servers.
                Hypixel: Uses the Hypixel ping packet and waits for the response. Only works on Hypixel.
                Server list: Gets the ping displayed previously on the server list. Doesn't work on singleplayer or if you used Direct Connect.""",
        category = "Commands",
        subcategory = "Index",
        options = {"Command", "Tab", "Stats", "Hypixel", "Server list"}
    )
    public int pingMethod = 2;

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
        title = "- settext [text]",
        description = "Sets the display text, supporting color codes with ampersands (&).",
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
        title = "- simulatechat (simc) [text]",
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

    @DependsOn("bountyHunting")
    @Switch(
        title = "Highlight Target and Show Distance",
        description = "Highlights the target and shows their distance.",
        category = "TNT Tag",
        subcategory = "Features"
    )
    public boolean highlightTargetAndShowDistance = true;

    @DependsOn("bountyHunting")
    @Color(
        title = "Distance Label Color",
        description = "The color of the distance label.",
        category = "TNT Tag",
        subcategory = "Features",
        alpha = false
    )
    public PolyColor distanceLabelColor = new PolyColor(0xFF5555);

    @DependsOn("bountyHunting")
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

    @Include
    public int playtimeMinutes = 0;

    @Include
    public int bountyHuntingPoints = 0;

    @Include
    public int bountyHuntingKills = 0;

    @Include
    public boolean firstTimeBountyHunting = true;

    // End of config
}
