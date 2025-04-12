package at.yedel.yedelmod.config;



import at.yedel.yedelmod.features.major.EasyAtlasVerdicts;
import at.yedel.yedelmod.features.major.MarketSearch;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.launch.YedelModConstants;
import at.yedel.yedelmod.utils.Constants;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateSource;
import dev.deftu.omnicore.client.OmniClientSound;
import dev.deftu.omnicore.client.OmniDesktop;
import dev.deftu.omnicore.common.OmniLoader;
import net.minecraft.util.EnumParticleTypes;
import org.lwjgl.input.Keyboard;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.*;
import org.polyfrost.oneconfig.api.ui.v1.Notifications;
import org.polyfrost.polyui.component.extensions.EventsKt;
import org.polyfrost.polyui.input.KeyBinder;
import org.polyfrost.polyui.input.KeybindHelper;

import java.net.URI;



public class YedelConfig extends Config {
    private YedelConfig() {
        super(YedelModConstants.MOD_ID + ".json", "assets/yedelmod/yedelmod.png", "YedelMod", Category.HYPIXEL);

        addDependencies();
        hideInternals();
    }

    private void addDependencies() {
        addDependency("guildWelcomeMessage", "autoWelcomeGuildMembers");
        addDependency("customParticleType", "customHitParticles");
        addDependency("particleYOffset", "customHitParticles");
        addDependency("randomParticleType", "customHitParticles");
        addDependency("onlySpawnCustomParticlesOnPlayers", "customHitParticles");
        addDependency("autoGGDelay", "dropperAutoGG");
        addDependency("regexChatFilterPattern", "regexChatFilter");
        addDependency("randomPlaceholderText", "randomPlaceholder");
        addDependency("strengthColor", "skywarsStrengthIndicators");
        addDependency("subStrengthColor", "skywarsStrengthIndicators");
        addDependency("rotateSwordInThirdPerson", "clientSideAutoBlock");
        addDependency("specifiedServer", "favoriteServerButton");

        addDependency("damageTiltStrength", "damageTilt");

        addDependency("highlightTargetAndShowDistance", "bountyHunting");
        addDependency("playHuntingSounds", "bountyHunting");
        addDependency("playSelection", "bountyHunting");
        addDependency("playKill", "bountyHunting");
        addDependency("bountyHuntingHud", "bountyHunting");
    }

    private void hideInternals() {
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

    private static final YedelConfig instance = new YedelConfig();

    public static YedelConfig getInstance() {
        return instance;
    }

    private final transient URI video = URI.create("https://www.youtube.com/watch?v=-z_AZR35ozI");

    // Start of config
    // Start of visible config

    /* General */

    // Updates

    @Dropdown(
        title = "Update Source",
        description = "Where to get updates from. Use GitHub for earlier releases and Modrinth for more stable releases.",
        category = "General",
        subcategory = "Updates",
        options = {"Modrinth", "GitHub"}
    )
    public int updateSource = 0;

    public UpdateSource getUpdateSource() {
        if (updateSource == 0) return UpdateSource.MODRINTH;
        else return UpdateSource.GITHUB;
    }

    @Switch(
        title = "Automatically Check for Updates",
        description = "Checks for updates on game load",
        category = "General",
        subcategory = "Updates"
    )
    public boolean automaticallyCheckForUpdates = true;

    @Button(
        title = "Modrinth Link",
        description = "Click to open the Modrinth site",
        category = "General",
        subcategory = "Updates",
        text = "Open"
    )
    public void openModrinthLink() {
        if (!OmniDesktop.browse(UpdateSource.MODRINTH.uri)) {
            Notifications.enqueue(Notifications.Type.Error, "YedelMod", "Couldn't open modrinth link!");
        }
    }

    @Button(
        title = "GitHub Link",
        description = "Click to open the GitHub repository",
        category = "General",
        subcategory = "Updates",
        text = "Open"
    )
    public void openGitHubRepository() {
        if (!OmniDesktop.browse(UpdateSource.GITHUB.uri)) {
            Notifications.enqueue(Notifications.Type.Error, "YedelMod", "Couldn't open github link!");
        }
    }

    @Button(
        title = "Check for Updates",
        description = "Check for updates with the selected source",
        category = "General",
        subcategory = "Updates",
        text = "Check"
    )
    public void checkForUpdates() {
        UpdateManager.getInstance().checkForUpdates(getUpdateSource(), UpdateManager.FeedbackMethod.NOTIFICATIONS);
    }

    /* Features */

    // Features

    @Switch(
        title = "Auto Welcome Guild Members",
        description = "Automatically welcomes new guild members with a customizable message.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean autoWelcomeGuildMembers = true;

    @Text(
        title = "Guild Welcome Message",
        description = "Message for new guild members. Use [player] for the new player.",
        category = "Features",
        subcategory = "Features"
    )
    public String guildWelcomeMessage = "Welcome, [player]!";

    @Switch(
        title = "Custom Hit Particles",
        description = "Spawns customizable particles when hitting entities.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean customHitParticles = false;

    @Dropdown(
        title = "Custom Particle Type",
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

    @Slider(
        title = "Particle Y Offset",
        description = "Some particles (such as note) may not show well due to being in the player model. Use this for those particles.",
        category = "Features",
        subcategory = "Features",
        min = -1,
        max = 2,
        step = 1
    )
    public int particleYOffset = 0;

    @Checkbox(
        title = "Random Particle Type",
        description = "Spawns a random particle type on hit instead of the one chosen above.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean randomParticleType = false;

    @Switch(
        title = "Only Spawn Custom Particles on Players",
        category = "Features",
        subcategory = "Features"
    )
    public boolean onlySpawnCustomParticlesOnPlayers = false;

    @Info(
        title = "This only says gg at the end of the game, not when you finish.", description = "",
        category = "Features", subcategory = "Features"
    )
    private transient int info$1 = 1;

    @Switch(
        title = "Dropper AutoGG",
        description = "AutoGG for dropper, will be removed when it is added to Sk1er's AutoGG.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean dropperAutoGG = OmniLoader.isModLoaded("autogg");

    @Slider(
        title = "AutoGG Delay",
        description = "Delay for AutoGG, measured in seconds.",
        category = "Features",
        subcategory = "Features",
        min = 0,
        max = 5,
        step = 1
    )
    public int autoGGDelay = 0;

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
    public boolean randomPlaceholder = true;

    @Text(
        title = "Random Placeholder Text",
        description = "When this is typed in chat, it will be replaced with a random string. Be careful not to use short placeholders to not spam excessively.",
        category = "Features",
        subcategory = "Features",
        placeholder = "//r"
    )
    public String randomPlaceholderText = "//r";

    @Switch(
        title = "SkyWars Strength Indicators",
        description = "Shows people's strength above their nametags with customizable colors. Accounts for Apothecary.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean skywarsStrengthIndicators = true;

    @Dropdown(
        title = "Strength Color",
        description = "Color for strength indicators (5.5s - 0.5s)",
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

    @Dropdown(
        title = "Sub Strength Color",
        description = "Color for strength indicators (0.5s - end)",
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
    public int subStrengthColor = 2;

    @Switch(
        title = "Client-Side Auto-Block",
        description = "Always shows the blocking animation client-side.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean clientSideAutoBlock = false;

    @Switch(
        title = "Rotate Sword in Third Person",
        description = "Applies sword rotations for third-person auto-blocking. Potentially fixes rotation issues with OverflowAnimations.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean rotateSwordInThirdPerson = true;

    @Switch(
        title = "Limbo Creative Mode",
        description = "Automatically gives creative mode in Hypixel limbo, not bannable because the server does not listen to anything happening. " +
            "Use /yedel lgmc in limbo if it doesn't work the firstTime time.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean limboCreativeMode = true;

    @Switch(
        title = "Favorite Server Button",
        description = "Adds a button to the main menu to join a customizable server address.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean favoriteServerButton = false;

    @Text(
        title = "Specified Server",
        description = "Server joined with button",
        category = "Features",
        subcategory = "Features"
    )
    public String specifiedServer = "mc.hypixel.net";

    /* Commands */

    // Index

    @Info(
        title = "Format: - command (any aliases) [arguments]", description = "",
        category = "Commands", subcategory = "Index"
    )
    private transient int info$4 = 1;

    @Info(
        title = "Example: - simulatechat (simc) [text] -> /yedel simc Hi!", description = "",
        category = "Commands", subcategory = "Index"
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
        title = "- formatting",
        description = "Shows a formatting guide with color and style codes.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$3 = 1;

    @Info(
        title = "- limbo (li)",
        description = "Sends an illegal chat character, which disconnects you on most servers and sends you to limbo-like areas on some. No longer works on Hypixel, use /limbo instead.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$4 = 1;

    @Info(
        title = "- limbocreative (limbogmc, lgmc)",
        description = "Gives you creative mode in Hypixel's limbo, given certain checks are passed.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$5 = 1;

    @Info(
        title = "- ping [method]",
        description = "Shows your ping to the server in chat, using several methods. Without an argument, uses the default method which can be customized.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$6 = 1;

    @Info(
        title = "- playtime (pt)",
        description = "Shows your total playtime (while playing on servers) in hours and minutes.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$7 = 1;

    @Info(
        title = "- setnick [nick]",
        description = "Sets your nick for Bounty Hunting to not select yourself as the target.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$8 = 1;

    @Info(
        title = "- settext [text]",
        description = "Sets the display text, supporting color codes with ampersands (&).",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$9 = 1;

    @Info(
        title = "- settitle [title]",
        description = "Sets the title of the game window.",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$10 = 1;

    @Info(
        title = "- simulatechat (simc) [text]",
        description = "Simulates a chat message, also supports color codes with ampersands (&).",
        category = "Commands",
        subcategory = "Index"
    )
    private transient int empty$11 = 1;

    @Info(
        title = "- update [platform]",
        description = "Checks for mod updates. Without an argument, uses the default one (modrinth). Possible platforms are \"modrinth\" or \"github\".",
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
    private transient int empty$13 = 1;

    // Customization

    @Dropdown(
        title = "Ping Method",
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

    @Keybind(
        title = "Search the auction house for your held item",
        description = "Searches the auction house for your currently held item's name, you may need to switch categories to see your item.",
        category = "Keybinds",
        subcategory = "Keybinds"
    )
    public KeyBinder.Bind ahSearchKeybind = KeybindHelper.builder().keys(Keyboard.KEY_K).does((bool) -> {
        MarketSearch.getInstance().ahSearch();
        return null;
    }).build();

    @Keybind(
        title = "Search the bazaar for your held item",
        description = "Searches the bazaar for your currently held item's name.",
        category = "Keybinds",
        subcategory = "Keybinds"
    )
    public KeyBinder.Bind bzSearchKeybind = KeybindHelper.builder().keys(Keyboard.KEY_L).does((bool) -> {
        MarketSearch.getInstance().bzSearch();
        return null;
    }).build();

    @Keybind(
        title = "Submit insufficient evidence verdict",
        description = "Submits an \"Insufficient Evidence\" verdict in Atlas.",
        category = "Keybinds",
        subcategory = "Keybinds"
    )
    public KeyBinder.Bind insufficientEvidenceKeybind = KeybindHelper.builder().keys(Keyboard.KEY_O).does(bool -> {
        EasyAtlasVerdicts.getInstance().submitInsufficientEvidenceVerdict();
        return null;
    }).build();

    @Keybind(
        title = "Submit evidence without doubt verdict",
        description = "Submits an \"Evidence Without Doubt\" verdict in Atlas.",
        category = "Keybinds",
        subcategory = "Keybinds"
    )
    public KeyBinder.Bind evidenceWithoutDoubtKeybind = KeybindHelper.builder().keys(Keyboard.KEY_P).does(bool -> {
        EasyAtlasVerdicts.getInstance().submitEvidenceWithoutDoubtVerdict();
        return null;
    }).build();

    /* Modern Features */

    // General

    @Switch(
        title = "Book Background (1.14+)",
        description = "Draws the default dark background in book GUIs.",
        category = "Modern Features",
        subcategory = "Features"
    )
    public boolean bookBackground = true;

    @Switch(
        title = "Keep Chat History on Chat Clear (1.15.2+)",
        description = "When clearing your chat (F3 + D), keep your message history (from pressing up arrow key).",
        category = "Modern Features",
        subcategory = "Features"
    )
    public boolean keepChatHistoryOnChatClear = true;

    @Switch(
        title = "Change Window Title (1.15.2+)",
        description = "Changes the window title on world and server join. \nYou can manually do this with /yedel settitle.",
        category = "Modern Features",
        subcategory = "Features"
    )
    public boolean changeWindowTitle = false;

    @Switch(
        title = "Damage Tilt (1.19.4+)",
        description = "Allows you to customize how much your screen hurts when being damaged.",
        category = "Modern Features",
        subcategory = "Features"
    )
    public boolean damageTilt = false;

    @Slider(
        title = "Damage Tilt Strength",
        description = "The amount of camera shake caused by being hurt.",
        category = "Modern Features",
        subcategory = "Features",
        min = 0,
        max = 100,
        step = 1
    )
    // And move it into this field
    public int damageTiltStrength = 100;

    // Hand Swings

    @Info(
        title = "Note: This only swings your hand client-side.", description = "",
        category = "Modern Features", subcategory = "Hand Swings"
    )
    private transient int info$6 = 1;

    @Switch(
        title = "Item Uses (1.15+)",
        description = "Swings your hand when using certain items, such as snowballs, eggs and armor.",
        category = "Modern Features",
        subcategory = "Hand Swings"
    )
    public boolean itemUseSwings = false;

    @Switch(
        title = "Item Drops (1.15+)",
        description = "Swings your hand when dropping an item.",
        category = "Modern Features",
        subcategory = "Hand Swings"
    )
    public boolean itemDropSwings = false;

    /* BedWars */

    // Challenges

    @Switch(
        title = "BedWars Defusal Helper",
        description = "Highlights redstone for the BedWars defusal challenge.",
        category = "BedWars",
        subcategory = "Challenges"
    )
    public boolean bedwarsDefusalHelper = true;

    // Chat

    @Checkbox(
        title = "Light Green Token Messages",
        description = "Make token messages light green instead of green (only in bedwars) to make them appear different from emerald messages.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean lightGreenTokenMessages = false;

    @Checkbox(
        title = "Hide Slumber Ticket Messages",
        description = "Hide slumber ticket messages in-game from things like kills and wins.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideSlumberTicketMessages = false;

    @Checkbox(
        title = "Hide Item Pickup Messages",
        description = "Hide \"You picked up: ...\" messages.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideItemPickupMessages = false;

    @Checkbox(
        title = "Hide Silver Coin Count",
        description = "Hide the silver coin count from item purchase messages.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideSilverCoinCount = false;

    @Checkbox(
        title = "Hide Comfy Pillow Messages",
        description = "Hides the following messages:" +
            "\n\"You are now carrying x1 Comfy Pillows, bring it back to your shop keeper!\"" +
            "\n\"You cannot return items to another team's Shopkeeper!\"" +
            "\n\"You cannot carry any more Comfy Pillows!\"" +
            "\n\"You died while carrying 1x Comfy Pillows!\"",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideComfyPillowMessages = false;

    @Checkbox(
        title = "Hide Dreamer's Soul Fragment Messages",
        description = "Hide \"+1 Dreamer's Soul Fragment!\" messages.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideDreamerSoulFragmentMessages = false;

    /* TNT Tag */

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
        EventsKt.onClick(Notifications.enqueue(Notifications.Type.Warning, "YedelMod", "Are you sure you want to reset your stats? (click)"), (block, event) -> {
                bountyHuntingPoints = 0;
                bountyHuntingKills = 0;
                TNTTagFeatures.getInstance().getDisplayLines().set(1, "§c0 points (reset)");
                TNTTagFeatures.getInstance().getDisplayLines().set(2, "§c0 kills (reset)");
                Notifications.enqueue(Notifications.Type.Success, "YedelMod", "Reset Bounty Hunting stats!");
                return null;
            }
        );
    }

    @Button(
        title = "Video",
        description = "This is a complicated feature, watch my video if you need help!",
        category = "TNT Tag",
        text = "Open video"
    )
    private void watchVideo() {
        if (!OmniDesktop.browse(video)) {
            Notifications.enqueue(Notifications.Type.Error, "Couldn't open video!");
        }
    }

    @Text(
        title = "Current Nick",
        description = "If you're playing nicked, set your currentNick or you might become the target.",
        category = "TNT Tag",
        placeholder = "Replace"
    )
    public String currentNick = "";

    // Features

    @Switch(
        title = "Highlight Target and Show Distance",
        description = "Distance is displayed above their nametag, corresponding to their rank color.",
        category = "TNT Tag",
        subcategory = "Features"
    )
    public boolean highlightTargetAndShowDistance = true;

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
        OmniClientSound.play(Constants.plingSound, 1, 0.8F);
    }

    @Button(
        title = "Kill Sound",
        description = "Sound: random.successful_hit at 1.04 pitch.",
        category = "TNT Tag",
        subcategory = "Features",
        text = "Play sound"
    )
    private void playKill() {
        OmniClientSound.play(Constants.plingSound, 1, 1.04F);
    }

    // End of visible config

    // Hidden variables for data

    @Include
    public int playtimeMinutes = 0;

    @Include
    public boolean firstTime = false;

    @Include
    public int bountyHuntingPoints = 0;

    @Include
    public int bountyHuntingKills = 0;

    @Include
    public boolean firstTimeBountyHunting = true;

    // End of config
}
