package at.yedel.yedelmod.config;



import at.yedel.yedelmod.utils.Constants;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateSource;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.config.elements.OptionPage;
import cc.polyfrost.oneconfig.config.migration.VigilanceMigrator;
import cc.polyfrost.oneconfig.libs.universal.USound;
import cc.polyfrost.oneconfig.utils.Notifications;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.Loader;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Objects;



public class YedelConfig extends Config {
    private YedelConfig() {
        super(new Mod("YedelMod", ModType.UTIL_QOL, "assets/yedelmod/yedelmod.png", new VigilanceMigrator("./config/YedelMod.toml")), "yedelmod.json");
        initialize();
        addDependency("guildWelcomeMessage", "guildWelcome");
        addDependency("dropperGGDelay", "dropperGG");
        addDependency("onlySpawnCustomParticlesOnPlayers", "customHitParticles");
        addDependency("hitParticleType", "customHitParticles");
        addDependency("randomParticleType", "customHitParticles");
        addDependency("hitParticleYOffset", "customHitParticles");
        addDependency("regexChatFilterPattern", "regexChatFilterToggled");
        addDependency("randomString", "randomPlaceholderToggled");
        addDependency("startStrengthColor", "strengthIndicators");
        addDependency("endStrengthColor", "strengthIndicators");
        addDependency("favoriteServer", "buttonFavoriteServer");

        addDependency("damageTiltStrength", "damageTiltToggled");

        addDependency("bhDisplay", "bountyHunting");
        addDependency("bhSounds", "bountyHunting");
        addDependency("playSelection", "bountyHunting");
        addDependency("playKill", "bountyHunting");
    }

    private static final YedelConfig instance = new YedelConfig();

    public static YedelConfig getInstance() {
        return instance;
    }

    private final transient URI video = URI.create("https://www.youtube.com/watch?v=-z_AZR35ozI");

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

    @Header(
        text = "Some properties may mention subcommands with -(the subcommand). They refer to using /yedel (the subcommand)."
    )
    private int header$1 = 1;

    // Updates
    @Dropdown(
        name = "Update source",
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
        name = "Automatically check for updates",
        description = "Checks for updates on game load",
        category = "General",
        subcategory = "Updates"
    )
    public boolean autoCheckUpdates = true;

    @Button(
        name = "Modrinth link",
        description = "Click to open the Modrinth site",
        category = "General",
        subcategory = "Updates",
        text = "Open"
    )
    public void openModrinthLink() {
        try {
            Desktop.getDesktop().browse(UpdateSource.MODRINTH.uri);
        } catch (IOException e) {
            Notifications.INSTANCE.send("YedelMod", "Couldn't open modrinth link!");
            e.printStackTrace();
        }
    }

    @Button(
        name = "GitHub link",
        description = "Click to open the GitHub repository",
        category = "General",
        subcategory = "Updates",
        text = "Open"
    )
    public void openGitHubRepository() {
        try {
            Desktop.getDesktop().browse(UpdateSource.GITHUB.uri);
        } catch (IOException e) {
            Notifications.INSTANCE.send("YedelMod", "Couldn't open github link!");
            e.printStackTrace();
        }
    }

    @Button(
        name = "Check for updates",
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
        name = "Auto welcome guild members",
        description = "Automatically welcomes new guild members with a customizable message." +
            "\n[VIP] Yedelos joined the guild!" +
            "\nGuild > [MVP+] Yedel [Yedel]: Welcome, Yedelos!",
        category = "Features",
        subcategory = "Features"
    )
    public boolean guildWelcome = true;
    @Switch(
        name = "Custom hit particles",
        description = "Spawns customizable particles when hitting entities.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean customHitParticles = false;

    @Switch(
        name = "Display text",
        description = "Show text which can be customized with -settext and cleared with -cleartext, supporting color codes with ampersands (&).",
        category = "Features",
        subcategory = "Features"
    )
    public boolean displayTextToggled = true;

    @Switch(
        name = "Dropper AutoGG",
        description = "AutoGG for dropper, will be removed when it is added to Sk1er's AutoGG." +
            "\nNote: This only says gg at the end of the game, not when you finish.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean dropperGG = Loader.isModLoaded("autogg");
    @Switch(
        name = "Regex chat filter",
        description = "Use a customizable regular expression to filter chat.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean regexChatFilterToggled = false;

    @Switch(
        name = "Random placeholder",
        description = "Type a customizable placeholder to replace it with a random string from a UUID.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean randomPlaceholderToggled = true;

    @Switch(
        name = "SkyWars strength indicators",
        description = "Shows people's strength above their nametags with customizable colors. Accounts for Apothecary.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean strengthIndicators = true;
    @Switch(
        name = "Client-side auto-block",
        description = "Always shows the blocking animation client-side.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean clientSideAutoBlock = false;
    @Switch(
        name = "Rotate sword in third person",
        description = "Applies sword rotations for third-person auto-blocking. Potentially fixes rotation issues with OverflowAnimations.",
        category = "Features",
        subcategory = "Customization"
    )
    public boolean rotateThirdPersonBlock = true;
    @Switch(
        name = "Limbo creative mode",
        description = "Automatically gives creative mode in Hypixel limbo, not bannable because the server does not listen to anything happening. " +
            "Use -lgmc in limbo if it doesn't work the first time.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean limboCreative = true;
    @Switch(
        name = "Favorite server button",
        description = "Adds a button to the main menu to join a customizable server address.",
        category = "Features",
        subcategory = "Features"
    )
    public boolean buttonFavoriteServer = false;

    // Tweaks

    @Info(
        text = "Smaller features that change the game, similar to features in Patcher.",
        type = InfoType.INFO
    )
    private int info$1 = 1;

    @Switch(
        name = "Unformat chat logs",
        description = "Remove formatting that was not removed from chat logs.\nThis will be removed in a future update, use Finement to replace it.",
        category = "Features",
        subcategory = "Tweaks"
    )
    public boolean unformatChatLogs = true;

    @Switch(
        name = "Hide missing signature errors",
        description = "Hide \"Signature is missing from textures payload\" errors from being logged.\nThis will be removed in a future update, use Finement to replace it.",
        category = "Features",
        subcategory = "Tweaks"
    )
    public boolean hideMissingSignatureErrors = true;

    // Customization

    @Text(
        name = "Guild welcome message",
        description = "Message for new guild members. Use [player] for the new player.",
        category = "Features",
        subcategory = "Customization"
    )
    public String guildWelcomeMessage = "Welcome, [player]!";
    @Switch(
        name = "Only spawn custom particles on players",
        category = "Features",
        subcategory = "Customization"
    )
    public boolean onlySpawnCustomParticlesOnPlayers = false;
    @Slider(
        name = "Particle Y offset",
        description = "Some particles (such as note) may not show well due to being in the player model. Use this for those particles.",
        category = "Features",
        subcategory = "Customization",
        min = -1,
        max = 2,
        step = 1
    )
    public int hitParticleYOffset = 0;
    @Dropdown(
        name = "Custom particle type",
        description = "The custom particle to be spawned when attacking an entity.",
        category = "Features",
        subcategory = "Customization",
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
    public int hitParticleType = EnumParticleTypes.NOTE.getParticleID();
    @Checkbox(
        name = "Random particle type",
        description = "Spawns a random particle type on hit instead of the one chosen above.",
        category = "Features",
        subcategory = "Customization"
    )
    public boolean randomParticleType = false;

    @Slider(
        name = "AutoGG Delay",
        description = "Delay for AutoGG, measured in seconds.",
        category = "Features",
        subcategory = "Customization",
        min = 0,
        max = 5,
        step = 1
    )
    public int dropperGGDelay = 0;
    @Dropdown(
        name = "Strength color",
        description = "Color for strength indicators (5.5s - 0.5s)",
        category = "Features",
        subcategory = "Customization",
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
    public int startStrengthColor = 1;
    @Dropdown(
        name = "Sub strength color",
        description = "Color for strength indicators (0.5s - end)",
        category = "Features",
        subcategory = "Customization",
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
    public int endStrengthColor = 2;
    @Text(
        name = "Regex chat filter pattern",
        description = "The pattern to use for regex chat filtering.",
        category = "Features",
        subcategory = "Customization"
    )
    public String regexChatFilterPattern = "";

    @Text(
        name = "Random placeholder text",
        description = "When this is typed in chat, it will be replaced with a random string. Be careful not to use short placeholders to not spam excessively.",
        category = "Features",
        subcategory = "Customization",
        placeholder = "//r"
    )
    public String randomString = "//r";

    @Text(
        name = "Specified server",
        description = "Server joined with button",
        category = "Features",
        subcategory = "Customization"
    )
    public String favoriteServer = "mc.hypixel.net";

    /* Commands */
    // Index

    @Header(
        text = "Description of this mod's subcommands, all under /yedel."
    )
    private int header$2 = 1;

    @Info(
        text = "Format: - command (any aliases) [arguments]",
        type = InfoType.INFO
    )
    private int info$2 = 1;
    @Info(
        text = "Example: - simulatechat (simc) [text] -> /yedel simc Hi!",
        type = InfoType.INFO
    )
    private int info$3 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "/yedel (/yedelmod)",
        description = "The main command, hosting all subcommands. When used with no arguments, opens this config screen.",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$1 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- cleartext",
        description = "Clears the currently set display text.",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$2 = 1;
    @CustomOption(id = "empty")
    @Empty(
        name = "- formatting",
        description = "Shows a formatting guide with color and style codes.",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$3 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- limbo (li)",
        description = "Sends an illegal chat character, which disconnects you on most servers and sends you to limbo-like areas on some. No longer works on Hypixel, use /limbo instead.",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$4 = 1;
    @CustomOption(id = "empty")
    @Empty(
        name = "- limbocreative (limbogmc, lgmc)",
        description = "Gives you creative mode in Hypixel's limbo, given certain checks are passed.",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$5 = 1;

    @CustomOption(id = "empty")
    @Empty(
        name = "- ping [method]",
        description = "Shows your ping to the server in chat, using several methods. Without an argument, uses the default method which can be customized.",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$6 = 1;
    @CustomOption(id = "empty")
    @Empty(
        name = "- playtime (pt)",
        description = "Shows your total playtime (while playing on servers) in hours and minutes.",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$7 = 1;
    @CustomOption(id = "empty")
    @Empty(
        name = "- setnick [nick]",
        description = "Sets your nick for Bounty Hunting to not select yourself as the target.",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$8 = 1;
    @CustomOption(id = "empty")
    @Empty(
        name = "- settext [text]",
        description = "Sets the display text, supporting color codes with ampersands (&).",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$9 = 1;
    @CustomOption(id = "empty")
    @Empty(
        name = "- settitle [title]",
        description = "Sets the title of the game window.",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$10 = 1;
    @CustomOption(id = "empty")
    @Empty(
        name = "- simulatechat (simc) [text]",
        description = "Simulates a chat message, also supports color codes with ampersands (&).",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$11 = 1;
    @CustomOption(id = "empty")
    @Empty(
        name = "- update [platform]",
        description = "Checks for mod updates. Without an argument, uses the default one (modrinth). Possible platforms are \"modrinth\" or \"github\".",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$12 = 1;
    @CustomOption(id = "empty")
    @Empty(
        name = "- yedelmessage (message)",
        description = "Shows messages from me about the mod. These can be anything from tips to bug notices.",
        category = "Commands",
        subcategory = "Index"
    )
    private int empty$13 = 1;

    // Customization
    @Dropdown(
        name = "Ping method",
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
    @Switch(
        name = "Search the auction house for your held item",
        description =
            "Searches the auction house for your currently held item's name, you may need to switch categories to see your item." +
                "\nBound to K by default.",
        category = "Keybinds",
        subcategory = "Keybinds"
    )
    public boolean ahSearch = true;
    @Switch(
        name = "Search the bazaar for your held item",
        description =
            "Searches the bazaar for your currently held item's name." +
                "\nBound to L by default.",
        category = "Keybinds",
        subcategory = "Keybinds"
    )
    public boolean bzSearch = true;
    @Switch(
        name = "Easy atlas verdicts",
        description =
            "Adds keybinds for the two atlas verdicts in your hotbar. \nThis automatically clicks for you, so it is use at your own risk." +
                "\nInsufficient Evidence: Bound to O by default." +
                "\nEvidence Without Doubt: Bound to P by default.",
        category = "Keybinds",
        subcategory = "Keybinds"
    )
    public boolean easyAtlasVerdicts = false;

    /* Modern Features */

    @Header(
        text = "Features backported from newer versions of the game."
    )
    private int header$3 = 1;

    // General

    @Switch(
        name = "Book background (1.14+)",
        description = "Draws the default dark background in book GUIs.",
        category = "Modern Features",
        subcategory = "General"
    )
    public boolean drawBookBackground = true;

    @Switch(
        name = "Keep chat history on chat clear (1.15.2+)",
        description = "When clearing your chat (F3 + D), keep your message history (from pressing up arrow key).",
        category = "Modern Features",
        subcategory = "General"
    )
    public boolean keepChatHistory = true;

    @Switch(
        name = "Change window title (1.15.2+)",
        description = "Changes the window title on world and server join. \nYou can manually do this with -settitle.",
        category = "Modern Features",
        subcategory = "General"
    )
    public boolean changeTitle = false;
    @Switch(
        name = "Damage Tilt (1.19.4+)",
        description = "Allows you to customize how much your screen hurts when being damaged.",
        category = "Modern Features",
        subcategory = "General"
    )
    public boolean damageTiltToggled = false;

    // Hand Swings

    @Info(
        text = "Note: This only swings your hand client-side.",
        type = InfoType.INFO
    )
    private int info$4 = 1;

    @Switch(
        name = "Item uses (1.15+)",
        description = "Swings your hand when using certain items, such as snowballs, eggs and armor.",
        category = "Modern Features",
        subcategory = "Hand Swings"
    )
    public boolean itemUseSwings = false;

    @Switch(
        name = "Item drops (1.15+)",
        description = "Swings your hand when dropping an item.",
        category = "Modern Features",
        subcategory = "Hand Swings"
    )
    public boolean dropSwings = false;

    // Customization
    @Slider(
        name = "Damage Tilt Strength",
        description = "The amount of camera shake caused by being hurt.",
        category = "Modern Features",
        subcategory = "Customization",
        min = 0,
        max = 100,
        step = 1
    )
    public int damageTiltStrength = 100;

    /* BedWars */

    // HUDs
    @Switch(
        name = "XP display",
        description = "Shows your experience out of 5,000. Inaccurate for lower levels (0-4).",
        category = "BedWars",
        subcategory = "HUDs"
    )
    public boolean xpDisplay = true;
    @Switch(
        name = "Magic milk display",
        description = "Shows how long your Magic Milk will last for.",
        category = "BedWars",
        subcategory = "HUDs"
    )
    public boolean magicMilkDisplay = true;

    // Challenges
    @Switch(
        name = "BedWars defusal helper",
        description = "Highlights redstone for the BedWars defusal challenge.",
        category = "BedWars",
        subcategory = "Challenges"
    )
    public boolean defusalHelper = true;

    // Chat
    @Checkbox(
        name = "Light green token messages",
        description = "Make token messages light green instead of green (only in bedwars) to make them appear different from emerald messages.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean lightGreenTokenMessages = false;
    @Checkbox(
        name = "Hide slumber ticket messages",
        description = "Hide slumber ticket messages in-game from things like kills and wins.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideSlumberTicketMessages = false;
    @Checkbox(
        name = "Hide item pickup messages",
        description = "Hide \"You picked up: ...\" messages.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideItemPickupMessages = false;
    @Checkbox(
        name = "Hide silver coin count",
        description = "Hide the silver coin count from item purchase messages.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideSilverCoinCount = false;
    @Checkbox(
        name = "Hide comfy pillow messages",
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
        name = "Hide dreamer's soul fragment messages",
        description = "Hide \"+1 Dreamer's Soul Fragment!\" messages.",
        category = "BedWars",
        subcategory = "Chat"
    )
    public boolean hideDreamerSoulFragmentMessages = false;

    /* TNT Tag */

    @Header(
        text = "Features relating to TNT Tag, mainly bounty hunting."
    )
    private int header$4 = 1;

    // General

    @Switch(
        name = "Bounty Hunting",
        description = "Adds a bounty hunting minigame to TNT Tag.",
        category = "TNT Tag"
    )
    public boolean bountyHunting = true;

    @Button(
        name = "Reset stats",
        category = "TNT Tag",
        text = "Reset"
    )
    private void resetConfirmation() {
        Notifications.INSTANCE.send("Bounty Hunting", "Are you sure you want to reset your stats? (click)", 3, () -> {
            points = 0;
            kills = 0;
            Notifications.INSTANCE.send("Bounty Hunting", "Reset stats!", 3);
        });
    }

    @Button(
        name = "Video",
        description = "This is a complicated feature, watch my video if you need help!",
        category = "TNT Tag",
        text = "Open video"
    )
    private void watchVideo() {
        try {
            Desktop.getDesktop().browse(video);
        } catch (IOException e) {
            Notifications.INSTANCE.send("YedelMod", "Couldn't open video!");
            e.printStackTrace();
        }
    }

    // Features

    @Switch(
        name = "Highlight target and show distance",
        description = "Distance is displayed above their nametag, corresponding to their rank color.",
        category = "TNT Tag",
        subcategory = "Features"
    )
    public boolean bhDisplay = true;
    @Switch(
        name = "Play sounds for target selections and kills",
        description = "Use the buttons below to test these sounds.",
        category = "TNT Tag",
        subcategory = "Features"
    )
    public boolean bhSounds = true;

    @Button(
        name = "Target selection sound",
        description = "Sound: random.successful_hit at 0.8 pitch.",
        category = "TNT Tag",
        subcategory = "Features",
        text = "Play sound"
    )
    private void playSelection() {
        USound.INSTANCE.playSoundStatic(Constants.plingSoundLocation, 1, 0.8F);
    }

    @Button(
        name = "Kill sound",
        description = "Sound: random.successful_hit at 1.04 pitch.",
        category = "TNT Tag",
        subcategory = "Features",
        text = "Play sound"
    )
    private void playKill() {
        USound.INSTANCE.playSoundStatic(Constants.plingSoundLocation, 1, 1.04F);
    }
    // Customization

    @Text(
        name = "Current nick",
        description = "If you're playing nicked, set your nick or you might become the target.",
        category = "TNT Tag",
        subcategory = "Customization",
        placeholder = "Replace"
    )
    public String nick = "";

    // End of visible config

    // Hidden variables for data

    public int playtimeMinutes = 0;
    public boolean first = false;
    public String displayedText = "";
    public int points = 0;
    public int kills = 0;
    public boolean bhFirst = true;

    // End of config
}
