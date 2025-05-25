package at.yedel.yedelmod.config;



import at.yedel.yedelmod.features.major.EasyAtlasVerdicts;
import at.yedel.yedelmod.features.major.MarketSearch;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.hud.BedwarsXPHud;
import at.yedel.yedelmod.hud.BountyHuntingHud;
import at.yedel.yedelmod.hud.CustomTextHud;
import at.yedel.yedelmod.hud.MagicMilkTimeHud;
import at.yedel.yedelmod.utils.ClickNotifications;
import at.yedel.yedelmod.utils.Constants;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateSource;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.config.elements.OptionPage;
import cc.polyfrost.oneconfig.config.migration.VigilanceMigrator;
import cc.polyfrost.oneconfig.config.migration.VigilanceName;
import cc.polyfrost.oneconfig.libs.universal.UDesktop;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import cc.polyfrost.oneconfig.libs.universal.USound;
import cc.polyfrost.oneconfig.platform.Platform;
import cc.polyfrost.oneconfig.utils.Notifications;
import net.minecraft.util.EnumParticleTypes;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.Objects;



public class YedelConfig extends Config {
    private static final YedelConfig INSTANCE = new YedelConfig();

    public static YedelConfig getInstance() {
        return INSTANCE;
    }

    private static final transient URI BOUNTY_HUNTING_VIDEO = URI.create("https://www.youtube.com/watch?v=-z_AZR35ozI");

    private YedelConfig() {
        super(new Mod("YedelMod", ModType.UTIL_QOL, "assets/yedelmod/yedelmod.png", 255, 255, new VigilanceMigrator("./config/YedelMod.toml")), "yedelmod.json", true, true);
        initialize();

        registerKeyBind(clickNotificationKeybind, ClickNotifications.getInstance()::clickNotification);
        registerKeyBind(ahSearchKeybind, MarketSearch.getInstance()::ahSearch);
        registerKeyBind(bzSearchKeybind, MarketSearch.getInstance()::bzSearch);
        registerKeyBind(insufficientEvidenceKeybind, EasyAtlasVerdicts.getInstance()::submitInsufficientEvidenceVerdict);
        registerKeyBind(evidenceWithoutDoubtKeybind, EasyAtlasVerdicts.getInstance()::submitEvidenceWithoutDoubtVerdict);

        addDependency("guildWelcomeMessage", "autoWelcomeGuildMembers");
        addDependency("customParticleType", "customHitParticles");
        addDependency("particleYOffset", "customHitParticles");
        addDependency("randomParticleType", "customHitParticles");
        addDependency("onlySpawnCustomParticlesOnPlayers", "customHitParticles");
        addDependency("autoGGDelay", "dropperAutoGG");
        addDependency("regexChatFilterPattern", "regexChatFilter");
        addDependency("randomPlaceholderText", "randomPlaceholder");
        addDependency("strengthColor", "skywarsStrengthIndicators");
        addDependency("rotateSwordInThirdPerson", "clientSideAutoBlock");
        addDependency("specifiedServer", "favoriteServerButton");
        addDependency("damageTiltStrength", "damageTilt");
        addDependency("highlightTargetAndShowDistance", "bountyHunting");
        addDependency("playHuntingSounds", "bountyHunting");
        addDependency("playSelection", "bountyHunting");
        addDependency("playKill", "bountyHunting");
        addDependency("bountyHuntingHud", "bountyHunting");

        for (String internalOption : new String[] {
            "oldDamageTiltStrength",
            "playtimeMinutes",
            "firstTime",
            "bountyHuntingPoints",
            "bountyHuntingKills",
            "firstTimeBountyHunting"
        }) {
            hideIf(internalOption, () -> true);
        }
    }

    @Override
    protected BasicOption getCustomOption(Field field, CustomOption annotation, OptionPage page, Mod mod, boolean migrate) {
        BasicOption option = null;
        if (Objects.equals(annotation.id(), "empty")) {
            Empty empty = ConfigUtils.findAnnotation(field, Empty.class);
            option = new EmptyOption(field, this, empty.name(), empty.description(), empty.category(), empty.subcategory(), empty.size());
            ConfigUtils.getSubCategory(page, empty.category(), empty.subcategory()).options.add(option);
        }
        return option;
    }

    // Start of config
    // Start of visible config

    /* General */

    @KeyBind(
        name = "Click Notification Keybind",
        description = "Clicks on the latest notification.",
        size = 2
    )
    public OneKeyBind clickNotificationKeybind = new OneKeyBind(Keyboard.KEY_SEMICOLON);

    // Updates

    @VigilanceName(name = "Update source", category = "General", subcategory = "Updates")
    @Dropdown(
        name = "Update Source",
        description = "Where to get updates from. Use GitHub for earlier releases and Modrinth for more stable releases.",
        category = "General",
        subcategory = "Updates",
        options = {"Modrinth", "GitHub"}
    )
    public int updateSource = 0;

    public UpdateSource getUpdateSource() {
        if (updateSource == 0) {
            return UpdateSource.MODRINTH;
        }
        else {
            return UpdateSource.GITHUB;
        }
    }

    @VigilanceName(name = "Automatically check for updates", category = "General", subcategory = "Updates")
    @Switch(
        name = "Automatically Check for Updates",
        description = "Checks for updates on game load",
        category = "General",
        subcategory = "Updates"
    )
    public boolean automaticallyCheckForUpdates = true;

    @Button(
        name = "Modrinth Link",
        description = "Click to open the Modrinth site",
        category = "General",
        subcategory = "Updates",
        text = "Open"
    )
    public void openModrinthLink() {
        if (!UDesktop.browse(UpdateSource.MODRINTH.uri)) {
            Notifications.INSTANCE.send("YedelMod", "Couldn't open modrinth link!");
        }
    }

    @Button(
        name = "GitHub Link",
        description = "Click to open the GitHub repository",
        category = "General",
        subcategory = "Updates",
        text = "Open"
    )
    public void openGitHubRepository() {
        if (!UDesktop.browse(UpdateSource.GITHUB.uri)) {
            Notifications.INSTANCE.send("YedelMod", "Couldn't open github link!");
        }
    }

    @Button(
        name = "Check for Updates",
        description = "Check for updates with the selected source",
        category = "General",
        subcategory = "Updates",
        text = "Check",
        size = 2
    )
    public void checkForUpdates() {
        UpdateManager.getInstance().checkForUpdates(getUpdateSource(), UpdateManager.FeedbackMethod.NOTIFICATIONS);
    }

    /* Features */

    // Features

    @VigilanceName(name = "Auto welcome guild members", category = "Features", subcategory = "Features")
    @Switch(
        name = "Auto Welcome Guild Members",
        description = "Automatically welcomes new guild members with a customizable message.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean autoWelcomeGuildMembers = true;

    @VigilanceName(name = "Guild welcome message", category = "Features", subcategory = "Customization")
    @Text(
        name = "Guild Welcome Message",
        description = "Message for new guild members. Use [player] for the new player.",
        category = "Features",
        subcategory = "Features"
    )
    public String guildWelcomeMessage = "Welcome, [player]!";

    @VigilanceName(name = "Custom hit particles", category = "Features", subcategory = "Features")
    @Switch(
        name = "Custom Hit Particles",
        description = "Spawns customizable particles when hitting entities.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean customHitParticles = false;

    @VigilanceName(name = "Custom particle type", category = "Features", subcategory = "Customization")
    @Dropdown(
        name = "Custom Particle Type",
        description = "The custom particle to be spawned when attacking an entity.",
        category = "Features",
        subcategory = "Features",
        options = {
            "Explosion (Normal)",
            "Explosion (Large)",
            "Explosion (Huge)",
            "Fireworks Spark",
            "Water Bubble",
            "Water Splash",
            "Water Wake",
            "Suspended",
            "Suspended Depth",
            "Crit",
            "Sharpness",
            "Smoke (Normal)",
            "Smoke (Large)",
            "Spell",
            "Instant Spell",
            "Mob Spell",
            "Ambient Mob Spell",
            "Witch Spell",
            "Water Drip",
            "Lava Drip",
            "Angry Villager",
            "Happy Villager",
            "Town Aura",
            "Note",
            "Portal",
            "Enchantment Table",
            "Flame",
            "Lava",
            "Footstep",
            "Cloud",
            "Redstone",
            "Snowball",
            "Snow Shovel",
            "Slime",
            "Heart",
            "Barrier",
            "Item Crack",
            "Block Crack",
            "Block Dust",
            "Water Drop",
            "Item Take",
            "Guardian"
        }
    )
    public int customParticleType = EnumParticleTypes.NOTE.getParticleID();

    @VigilanceName(name = "Particle Y offset", category = "Features", subcategory = "Customization")
    @Slider(
        name = "Particle Y Offset",
        description = "Some particles (such as note) may not show well due to being in the player model. Use this for those particles.",
        category = "Features",
        subcategory = "Features",
        min = -1,
        max = 2,
        step = 1
    )
    public int particleYOffset = 0;

    @VigilanceName(name = "Random particle type", category = "Features", subcategory = "Customization")
    @Checkbox(
        name = "Random Particle Type",
        description = "Spawns a random particle type on hit instead of the one chosen above.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean randomParticleType = false;

    @VigilanceName(
        name = "Only spawn custom particles on players",
        category = "Features",
        subcategory = "Customization"
    )
    @Switch(
        name = "Only Spawn Custom Particles on Players",
        category = "Features",
        subcategory = "Features"
    )
    public boolean onlySpawnCustomParticlesOnPlayers = false;

    @Info(
        text = "This only says gg at the end of the game, not when you finish.",
        type = InfoType.INFO,
        category = "Features",
        subcategory = "Features",
        size = 2
    )
    private transient int info$1 = 1;

    @VigilanceName(name = "Dropper AutoGG", category = "Features", subcategory = "Features")
    @Switch(
        name = "Dropper AutoGG",
        description = "AutoGG for dropper, will be removed when it is added to Sk1er's AutoGG.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean dropperAutoGG = Platform.getLoaderPlatform().isModLoaded("autogg");

    @VigilanceName(name = "AutoGG Delay", category = "Features", subcategory = "Customization")
    @Slider(
        name = "AutoGG Delay",
        description = "Delay for AutoGG, measured in seconds.",
        category = "Features",
        subcategory = "Features",
        min = 0,
        max = 5,
        step = 1
    )
    public int autoGGDelay = 0;

    @VigilanceName(name = "Regex chat filter", category = "Features", subcategory = "Features")
    @Switch(
        name = "Regex Chat Filter",
        description = "Use a customizable regular expression to filter chat.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean regexChatFilter = false;

    @VigilanceName(name = "Regex chat filter pattern", category = "Features", subcategory = "Customization")
    @Text(
        name = "Regex Chat Filter Pattern",
        description = "The pattern to use for regex chat filtering.",
        category = "Features",
        subcategory = "Features"
    )
    public String regexChatFilterPattern = "";

    @VigilanceName(name = "Random placeholder", category = "Features", subcategory = "Features")
    @Switch(
        name = "Random Placeholder",
        description = "Type a customizable placeholder to replace it with a random string from a UUID.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean randomPlaceholder = true;

    @VigilanceName(name = "Random placeholder text", category = "Features", subcategory = "Customization")
    @Text(
        name = "Random Placeholder Text",
        description = "When this is typed in chat, it will be replaced with a random string. Be careful not to use short placeholders to not spam excessively.",
        category = "Features",
        subcategory = "Features",
        placeholder = "//r"
    )
    public String randomPlaceholderText = "//r";

    @VigilanceName(name = "SkyWars strength indicators", category = "Features", subcategory = "Features")
    @Switch(
        name = "SkyWars Strength Indicators",
        description = "Shows people's strength above their nametags with customizable colors",
        category = "Features",
        subcategory = "Features"
    )
    public boolean skywarsStrengthIndicators = true;

    @VigilanceName(name = "Strength color", category = "Features", subcategory = "Customization")
    @Dropdown(
        name = "Strength Color",
        description = "Color for strength indicators",
        category = "Features",
        subcategory = "Features",
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

    @VigilanceName(name = "Client-side auto-block", category = "Features", subcategory = "Features")
    @Switch(
        name = "Client-Side Auto-Block",
        description = "Always shows the blocking animation client-side.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean clientSideAutoBlock = false;

    @VigilanceName(name = "Rotate sword in third person", category = "Features", subcategory = "Customization")
    @Switch(
        name = "Rotate Sword in Third Person",
        description = "Applies sword rotations for third-person auto-blocking. Potentially fixes rotation issues with OverflowAnimations.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean rotateSwordInThirdPerson = true;

    @VigilanceName(name = "Limbo creative mode", category = "Features", subcategory = "Features")
    @Switch(
        name = "Limbo Creative Mode",
        description = "Automatically gives creative mode in Hypixel limbo, not bannable because the server does not listen to anything happening. " +
            "Use /yedel lgmc in limbo if it doesn't work the firstTime time.",
        category = "Features",
        subcategory = "Features",
        size = 2
    )
    public boolean limboCreativeMode = true;

    @VigilanceName(name = "Favorite server button", category = "Features", subcategory = "Features")
    @Switch(
        name = "Favorite Server Button",
        description = "Adds a button to the main menu to join a customizable server address.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean favoriteServerButton = false;

    @VigilanceName(name = "Specified server", category = "Features", subcategory = "Customization")
    @Text(
        name = "Specified Server",
        description = "Server joined with button",
        category = "Features",
        subcategory = "Features"
    )
    public String specifiedServer = "mc.hypixel.net";

    @HUD(
        name = "Custom Text HUD",
        category = "HUDs"
    )
    public CustomTextHud customTextHud = new CustomTextHud();

    /* Commands */

    @Header(
        text = "Description of this mod's subcommands, all under /yedel.",
        category = "Commands",
        size = 2
    )
    private transient int header$2 = 1;

    // Index

    @Info(
        text = "Format: - command (any aliases) [arguments]",
        type = InfoType.INFO,
        category = "Commands",
        subcategory = "Index"
    )
    private transient int info$4 = 1;

    @Info(
        text = "Example: - simulatechat (simc) [text] -> /yedel simc Hi!",
        type = InfoType.INFO,
        category = "Commands",
        subcategory = "Index"
    )
    private transient int info$5 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "/yedel (/yedelmod)",
        description = "The main command, hosting all subcommands. When used with no arguments, opens this config screen.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$1 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- cleartext",
        description = "Clears the currently set display text.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$2 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- constants",
        description = "Shows mod constants and build information such as the project version.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$3 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- formatting",
        description = "Shows a formatting guide with color and style codes.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$4 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- limbo (li)",
        description = "Sends an illegal chat character, which disconnects you on most servers and sends you to limbo-like areas on some. No longer works on Hypixel, use /limbo instead.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$5 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- limbocreative (limbogmc, lgmc)",
        description = "Gives you creative mode in Hypixel's limbo, given certain checks are passed.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$6 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- ping [method]",
        description = "Shows your ping to the server in chat, using several methods. Without an argument, uses the default method which can be customized.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$7 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- playtime (pt)",
        description = "Shows your total playtime (while playing on servers) in hours and minutes.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$8 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- setnick [nick]",
        description = "Sets your nick for Bounty Hunting to not select yourself as the target.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$9 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- settext [text]",
        description = "Sets the display text, supporting color codes with ampersands (&).",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$10 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- settitle [title]",
        description = "Sets the title of the game window.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$11 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- simulatechat (simc) [text]",
        description = "Simulates a chat message, also supports color codes with ampersands (&).",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$12 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- update [platform]",
        description = "Checks for mod updates. Without an argument, uses the default one (modrinth). Possible platforms are \"modrinth\" or \"github\".",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$13 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- yedelmessage (message)",
        description = "Shows messages from me about the mod. These can be anything from tips to bug notices.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$14 = 1;

    // Customization

    @VigilanceName(name = "Ping method", category = "Commands", subcategory = "Customization")
    @Dropdown(
        name = "Ping Method",
        description =
            "Ping: Does /ping command. Works on very few servers." +
                "\nCommand (default): Enters a random command and waits for the unknown command response. Works on almost all servers." +
                "\nTab: Sends a tab completion packet and waits for the response. Works on all servers." +
                "\nStats: Sends a statistics packet and waits for the response. Works on all servers." +
                "\nServer list: Gets the ping displayed previously on the server list. Doesn't work on singleplayer or if you used Direct Connect." +
                "\nHypixel: Uses the Hypixel ping packet and waits for the response. Only works on Hypixel.",
        category = "Commands",
        subcategory = "Customization",
        options = {"Ping", "Command", "Tab", "Stats", "Server list", "Hypixel"}
    )
    public int pingMethod = 1;

    /* Keybinds */

    // Keybinds

    @KeyBind(
        name = "Search the auction house for your held item",
        description = "Searches the auction house for your currently held item's coloredName, you may need to switch categories to see your item.",
        category = "Keybinds",
        subcategory = "Keybinds",
        size = 2
    )
    public OneKeyBind ahSearchKeybind = new OneKeyBind(UKeyboard.KEY_K);

    @KeyBind(
        name = "Search the bazaar for your held item",
        description = "Searches the bazaar for your currently held item's coloredName.",
        category = "Keybinds",
        subcategory = "Keybinds",
        size = 2
    )
    public OneKeyBind bzSearchKeybind = new OneKeyBind(UKeyboard.KEY_L);

    @KeyBind(
        name = "Submit insufficient evidence verdict",
        description = "Submits an \"Insufficient Evidence\" verdict in Atlas.",
        category = "Keybinds",
        subcategory = "Keybinds",
        size = 2
    )
    public OneKeyBind insufficientEvidenceKeybind = new OneKeyBind(UKeyboard.KEY_O);

    @KeyBind(
        name = "Submit evidence without doubt verdict",
        description = "Submits an \"Evidence Without Doubt\" verdict in Atlas.",
        category = "Keybinds",
        subcategory = "Keybinds",
        size = 2
    )
    public OneKeyBind evidenceWithoutDoubtKeybind = new OneKeyBind(UKeyboard.KEY_P);

    /* Modern Features */

    @Header(
        text = "Features backported from modern versions of the game.",
        category = "Modern Features",
        size = 2
    )
    private transient int header$3 = 1;

    // General

    @VigilanceName(name = "Book background (1.14+)", category = "Modern Features", subcategory = "General")
    @Switch(
        name = "Book Background (1.14+)",
        description = "Draws the default dark background in book GUIs.",
        category = "Modern Features",
        subcategory = "Features"
    )
    public boolean bookBackground = true;

    @VigilanceName(
        name = "Keep chat history on chat clear (1.15.2+)",
        category = "Modern Features",
        subcategory = "General"
    )
    @Switch(
        name = "Keep Chat History on Chat Clear (1.15.2+)",
        description = "When clearing your chat (F3 + D), keep your message history (from pressing up arrow key).",
        category = "Modern Features",
        subcategory = "Features"
    )
    public boolean keepChatHistoryOnChatClear = true;

    @VigilanceName(name = "Change window title (1.15.2+)", category = "Modern Features", subcategory = "General")
    @Switch(
        name = "Change Window Title (1.15.2+)",
        description = "Changes the window title on world and server join. \nYou can manually do this with /yedel settitle.",
        category = "Modern Features",
        subcategory = "Features"
    )
    public boolean changeWindowTitle = false;

    @VigilanceName(name = "Damage Tilt (1.19.4+)", category = "Modern Features", subcategory = "General")
    @Switch(
        name = "Damage Tilt (1.19.4+)",
        description = "Allows you to customize how much your screen hurts when being damaged.",
        category = "Modern Features",
        subcategory = "Features"
    )
    public boolean damageTilt = false;

    // The vigilance setting was a float from 0-1 with a step of 0.01, so copy that into this field
    @VigilanceName(name = "Damage Tilt Strength", category = "Modern Features", subcategory = "Customization")
    @Number(
        name = "oldDamageTiltStrength",
        category = "Modern Features",
        subcategory = "Customization",
        min = 1,
        max = 1
    )
    private float oldDamageTiltStrength = 1;

    @Slider(
        name = "Damage Tilt Strength",
        description = "The amount of camera shake caused by being hurt.",
        category = "Modern Features",
        subcategory = "Features",
        min = 0,
        max = 100,
        step = 1
    )
    // And move it into this field
    public int damageTiltStrength = (int) (oldDamageTiltStrength * 100);

    // Hand Swings

    @Info(
        text = "Note: This only swings your hand client-side.",
        type = InfoType.INFO,
        category = "Modern Features",
        subcategory = "Hand Swings",
        size = 2
    )
    private transient int info$6 = 1;

    @VigilanceName(name = "Item uses (1.15+)", category = "Modern Features", subcategory = "Hand Swings")
    @Switch(
        name = "Item Uses (1.15+)",
        description = "Swings your hand when using certain items, such as snowballs, eggs and armor.",
        category = "Modern Features",
        subcategory = "Hand Swings"
    )
    public boolean itemUseSwings = false;

    @VigilanceName(name = "Item drops (1.15+)", category = "Modern Features", subcategory = "Hand Swings")
    @Switch(
        name = "Item Drops (1.15+)",
        description = "Swings your hand when dropping an item.",
        category = "Modern Features",
        subcategory = "Hand Swings"
    )
    public boolean itemDropSwings = false;

    /* BedWars */

    // Challenges

    @VigilanceName(name = "BedWars defusal helper", category = "BedWars", subcategory = "Challenges")
    @Switch(
        name = "BedWars Defusal Helper",
        description = "Highlights redstone for the BedWars defusal challenge.",
        category = "BedWars",
        subcategory = "Challenges"
    )
    public boolean bedwarsDefusalHelper = true;

    // Chat

    @VigilanceName(name = "Light green token messages", category = "BedWars", subcategory = "Chat")
    @Checkbox(
        name = "Light Green Token Messages",
        description = "Make token messages light green instead of green (only in bedwars) to make them appear different from emerald messages.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean lightGreenTokenMessages = false;

    @VigilanceName(name = "Hide slumber ticket messages", category = "BedWars", subcategory = "Chat")
    @Checkbox(
        name = "Hide Slumber Ticket Messages",
        description = "Hide slumber ticket messages in-game from things like kills and wins.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideSlumberTicketMessages = false;

    @VigilanceName(name = "Hide item pickup messages", category = "BedWars", subcategory = "Chat")
    @Checkbox(
        name = "Hide Item Pickup Messages",
        description = "Hide \"You picked up: ...\" messages.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideItemPickupMessages = false;

    @VigilanceName(name = "Hide silver coin count", category = "BedWars", subcategory = "Chat")
    @Checkbox(
        name = "Hide Silver Coin Count",
        description = "Hide the silver coin count from item purchase messages.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideSilverCoinCount = false;

    @VigilanceName(name = "Hide comfy pillow messages", category = "BedWars", subcategory = "Chat")
    @Checkbox(
        name = "Hide Comfy Pillow Messages",
        description = "Hides the following messages:" +
            "\n\"You are now carrying x1 Comfy Pillows, bring it back to your shop keeper!\"" +
            "\n\"You cannot return items to another team's Shopkeeper!\"" +
            "\n\"You cannot carry any more Comfy Pillows!\"" +
            "\n\"You died while carrying 1x Comfy Pillows!\"",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideComfyPillowMessages = false;

    @VigilanceName(name = "Hide dreamer's soul fragment messages", category = "BedWars", subcategory = "Chat")
    @Checkbox(
        name = "Hide Dreamer's Soul Fragment Messages",
        description = "Hide \"+1 Dreamer's Soul Fragment!\" messages.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideDreamerSoulFragmentMessages = false;

    @HUD(
        name = "Bedwars XP Display HUD",
        category = "HUDs"
    )
    public BedwarsXPHud bedwarsXPHud = new BedwarsXPHud();

    @HUD(
        name = "Magic Milk Time HUD",
        category = "HUDs"
    )
    public MagicMilkTimeHud magicMilkTimeHud = new MagicMilkTimeHud();

    /* TNT Tag */

    @Header(
        text = "Features relating to TNT Tag, mainly bounty hunting.",
        category = "TNT Tag",
        size = 2
    )
    private transient int header$4 = 1;

    // General

    @VigilanceName(name = "Bounty hunting", category = "TNT Tag", subcategory = "")
    @Switch(
        name = "Bounty Hunting",
        description = "Adds a bounty hunting minigame to TNT Tag.",
        category = "TNT Tag"
    )
    public boolean bountyHunting = true;

    @Button(
        name = "Reset Stats",
        category = "TNT Tag",
        text = "Reset"
    )
    private void resetConfirmation() {
        ClickNotifications.getInstance().send("Bounty Hunting", "Are you sure you want to reset your stats? (press %k)", () -> {
                bountyHuntingPoints = 0;
                bountyHuntingKills = 0;
                TNTTagFeatures.getInstance().getDisplayLines().set(1, "§c0 points (reset)");
                TNTTagFeatures.getInstance().getDisplayLines().set(2, "§c0 kills (reset)");
                Notifications.INSTANCE.send("Bounty Hunting", "Reset stats!", 3000);
            }
        );
    }

    @Button(
        name = "Video",
        description = "This is a complicated feature, watch my video if you need help!",
        category = "TNT Tag",
        text = "Open video"
    )
    private void watchVideo() {
        if (!UDesktop.browse(BOUNTY_HUNTING_VIDEO)) {
            Notifications.INSTANCE.send("YedelMod", "Couldn't open video!");
        }
    }

    @VigilanceName(name = "Current nick", category = "TNT Tag", subcategory = "Customization")
    @Text(
        name = "Current Nick",
        description = "If you're playing nicked, set your currentNick or you might become the target.",
        category = "TNT Tag",
        placeholder = "Replace"
    )
    public String currentNick = "";

    // Features

    @VigilanceName(name = "Highlight target and show distance", category = "TNT Tag", subcategory = "Features")
    @Switch(
        name = "Highlight Target and Show Distance",
        description = "Distance is displayed above their nametag, corresponding to their rank color.",
        category = "TNT Tag",
        subcategory = "Features"
    )
    public boolean highlightTargetAndShowDistance = true;

    @VigilanceName(name = "Play sounds for target selections and kills", category = "TNT Tag", subcategory = "Features")
    @Switch(
        name = "Play Sounds for Target Selections and Kills",
        description = "Use the buttons below to test these sounds.",
        category = "TNT Tag",
        subcategory = "Features"
    )
    public boolean playHuntingSounds = true;

    @Button(
        name = "Target Selection Sound",
        description = "Sound: random.successful_hit at 0.8 pitch.",
        category = "TNT Tag",
        subcategory = "Features",
        text = "Play sound"
    )
    private void playSelection() {
        USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, 0.8F);
    }

    @Button(
        name = "Kill Sound",
        description = "Sound: random.successful_hit at 1.04 pitch.",
        category = "TNT Tag",
        subcategory = "Features",
        text = "Play sound"
    )
    private void playKill() {
        USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, 1.04F);
    }

    @HUD(
        name = "Bounty Hunting HUD",
        category = "HUDs"
    )
    public BountyHuntingHud bountyHuntingHud = new BountyHuntingHud();

    // End of visible config

    // Hidden variables for data

    // Before these were under the "storage" category, but doing so in OneConfig makes a new category even if all options are hidden.
    @VigilanceName(name = "playtimeMinutes", category = "storage", subcategory = "")
    @Number(name = "playtimeMinutes", category = "General", subcategory = "", min = 0, max = Integer.MAX_VALUE)
    public int playtimeMinutes = 0;
    @VigilanceName(name = "first", category = "storage", subcategory = "")
    @Switch(name = "firstTime", category = "General", subcategory = "")
    public boolean firstTime = false;
    @VigilanceName(name = "points", category = "storage", subcategory = "")
    @Number(name = "bountyHuntingPoints", category = "General", subcategory = "", min = 0, max = Integer.MAX_VALUE)
    public int bountyHuntingPoints = 0;
    @VigilanceName(name = "kills", category = "storage", subcategory = "")
    @Number(name = "bountyHuntingKills", category = "General", subcategory = "", min = 0, max = Integer.MAX_VALUE)
    public int bountyHuntingKills = 0;
    @VigilanceName(name = "bhFirst", category = "storage", subcategory = "")
    @Switch(name = "firstTimeBountyHunting", category = "General", subcategory = "")
    public boolean firstTimeBountyHunting = true;

    // End of config
}
