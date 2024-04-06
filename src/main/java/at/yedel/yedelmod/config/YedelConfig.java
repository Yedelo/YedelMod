package at.yedel.yedelmod.config;



import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.gui.MoveHuntingTextGui;
import at.yedel.yedelmod.gui.MoveTextGui;
import at.yedel.yedelmod.update.UpdateManager;
import at.yedel.yedelmod.update.UpdateSource;
import at.yedel.yedelmod.utils.Constants;
import gg.essential.api.utils.GuiUtil;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.JVMAnnotationPropertyCollector;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import net.minecraftforge.fml.common.Loader;

import static at.yedel.yedelmod.YedelMod.minecraft;



// the order of everything here keeps getting reset its very annoying
public class YedelConfig extends Vigilant {
    private static final URI video;
    public static YedelConfig instance = new YedelConfig();
    @Property(
            type = PropertyType.SELECTOR,
            name = "&9Update source",
            description = "Where to get updates from. Use GitHub for earlier releases and Modrinth for more stable releases.",
            category = "General",
            options = {"Modrinth", "GitHub"}
    )
    public static int updateSource = 0;
    @Property(
            type = PropertyType.SWITCH,
            name = "&9Automatically check for updates",
            description = "Checks for updates on game load",
            category = "General"
    )
    public static boolean autoCheckUpdates = true;

    @Property(
            type = PropertyType.BUTTON,
            name = "&9Modrinth link",
            description = "Click to open the Modrinth site",
            category = "General",
            placeholder = "Open"
    )
    public static void openModrinthLink() throws IOException, URISyntaxException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(UpdateSource.MODRINTH.link));
        }
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "&9GitHub link",
            description = "Click to open the GitHub repository",
            category = "General",
            placeholder = "Open"
    )
    public static void openGitHubRepository() throws IOException, URISyntaxException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(UpdateSource.GITHUB.link));
        }
    }
    @Property(
            type = PropertyType.SWITCH,
            name = "Auto welcome guild members",
            description = "Automatically welcomes new guild members with a custom message, customize below." +
                    "\n&a[VIP] Yedelos &ejoined the guild!" +
                    "\n&2Guild > &b[MVP&8+&b] Yedel &6[Yedel]&f: Welcome, Yedelos!",
            category = "General",
            subcategory = "Features"
    )
    public static boolean guildWelcome = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Dropper AutoGG",
            description = "AutoGG for dropper, will be removed when it is added to Sk1er's AutoGG." +
                    "\n&eNote: This only says gg at the end of the game, not when you finish.",
            category = "General",
            subcategory = "Features"
    )
    public static boolean dropperGG = isAutoGGLoaded();
    @Property(
            type = PropertyType.SWITCH,
            name = "SkyWars strength indicators",
            description = "Shows people's strength above their nametags. Accounts for Apothecary.",
            category = "General",
            subcategory = "Features"
    )
    public static boolean strengthIndicators = true;
    @Property(
            type = PropertyType.SWITCH,
            name = "Easy atlas verdicts",
            description = "Adds keybinds for the two atlas verdicts in your hotbar. \nThis automatically clicks for you, so it is &cuse at your own risk.",
            category = "General",
            subcategory = "Features"
    )
    public static boolean autoAtlas = false;

    // Start of config
    @Property(
            type = PropertyType.SWITCH,
            name = "Kuudra sacrifice display",
            description = "Shows the coins needed to get the Kuudra Follower Helmet from the Kuudra Believer.",
            category = "General",
            subcategory = "Features"
    )
    public static boolean sacrificeDisplay = true;
    @Property(
            type = PropertyType.SWITCH,
            name = "BedWars defusal helper",
            description = "Highlights redstone and blocks incorrect clicks/hotkeys for the BedWars defusal challenge.",
            category = "General",
            subcategory = "Features"
    )
    public static boolean defusalHelper = true;
    @Property(
            type = PropertyType.SWITCH,
            name = "Block non-redstone clicks",
            description = "Blocks clicks on non-redstone items in defusal, &cuse at your own risk.",
            category = "General",
            subcategory = "Features"
    )
    public static boolean defusalBlockClicks = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Limbo creative mode",
            description = "Automatically gives creative mode in limbo, not bannable because the server does not listen to anything happening. " +
                    "Use /lgmc in limbo if it doesn't work the first time",
            category = "General",
            subcategory = "Features"
    )
    public static boolean limboCreative = true;

    /* General */

    // Features
    @Property(
            type = PropertyType.SWITCH,
            name = "Book background",
            description = "Draws the default dark background in book GUIs",
            category = "General",
            subcategory = "Features"
    )
    public static boolean drawBookBackground = true;
    @Property(
            type = PropertyType.SWITCH,
            name = "Favorite server button",
            description = "Adds a button to the main menu to join a server, specified below",
            category = "General",
            subcategory = "Features"
    )
    public static boolean buttonFavoriteServer = true;
    @Property(
            type = PropertyType.TEXT,
            name = "Guild welcome message",
            description = "Message for new guild members. Use [player] for the new player.",
            category = "General",
            subcategory = "Customization"
    )
    public static String guildWelcomeMessage = "Welcome, [player]!";
    @Property(
            type = PropertyType.SLIDER,
            name = "Delay",
            description = "Delay for AutoGG, measured in seconds",
            category = "General",
            subcategory = "Customization",
            max = 5
    )
    public static int dropperGGDelay = 1;
    @Property(
            type = PropertyType.SELECTOR,
            name = "Strength color",
            description = "Color for strength indicators (5.5s - 0.5s)",
            category = "General",
            subcategory = "Customization",
            options = {
                    "§4Dark Red",
                    "§cRed",
                    "§6Gold",
                    "§eYellow",
                    "§2Dark Green",
                    "§aGreen",
                    "§bAqua",
                    "§3Dark Aqua",
                    "§1Dark Blue",
                    "§9Blue",
                    "§dPink",
                    "§5Purple",
                    "§fWhite",
                    "§7Gray",
                    "§8Dark Gray",
                    "§0Black"
            }
    )
    public static int startStrengthColor = 1;
    @Property(
            type = PropertyType.SELECTOR,
            name = "Sub strength color",
            description = "Color for strength indicators (0.5s - end)",
            category = "General",
            subcategory = "Customization",
            options = {
                    "§4Dark Red",
                    "§cRed",
                    "§6Gold",
                    "§eYellow",
                    "§2Dark Green",
                    "§aGreen",
                    "§bAqua",
                    "§3Dark Aqua",
                    "§1Dark Blue",
                    "§9Blue",
                    "§dPink",
                    "§5Purple",
                    "§fWhite",
                    "§7Gray",
                    "§8Dark Gray",
                    "§0Black"
            }
    )
    public static int endStrengthColor = 2;
    @Property(
            type = PropertyType.TEXT,
            name = "Random placeholder",
            description = "When this is typed in chat, it will be replaced with a random string.",
            category = "General",
            subcategory = "Customization",
            placeholder = "//r"
    )
    public static String randomString = "//r";
    @Property(
            type = PropertyType.SELECTOR,
            name = "Ping method",
            description =
                    "&9Ping: &7Does /ping command. Works on very few servers." +
                            "\n&9Command: &7Enters a random command and waits for the unknown command response. Works on almost all servers." +
                            "\n&9Tab: &7Sends a tab completion packet and waits for the response. Works on all servers." +
                            "\n&9Stats: &7Sends a statistics packet and waits for the response. Works on all servers." +
                            "\n&9Server list (default): &7Gets the ping displayed previously on the server list. Doesn't work on singleplayer and if you used Direct Connect. ",
            category = "General",
            subcategory = "Customization",
            options = {"Ping", "Command", "Tab", "Stats", "Server list (default)"}
    )
    public static int pingMethod = 4;
    @Property(
            type = PropertyType.TEXT,
            name = "Specified server",
            description = "Server joined with button",
            category = "General",
            subcategory = "Customization"
    )
    public static String favoriteServer = "yedelmod.hypixel.net";

    // Customization
    @Property(
            type = PropertyType.SWITCH,
            name = "Bounty Hunting",
            description = "Enables bounty hunting",
            category = "TNT Tag"
    )
    public static boolean bountyHunting = true;
    @Property(
            type = PropertyType.SWITCH,
            name = "Highlight target and show distance",
            description = "Distance is displayed above their nametag, corresponding to their rank color.",
            category = "TNT Tag",
            subcategory = "Features"
    )
    public static boolean bhDisplay = true;
    @Property(
            type = PropertyType.SWITCH,
            name = "Play sounds for target selections and kills",
            description = "Use the buttons below to test these sounds.",
            category = "TNT Tag",
            subcategory = "Features"
    )
    public static boolean bhSounds = true;
    @Property(
            type = PropertyType.SWITCH,
            name = "Give items to play again and leave the game",
            description = "Mimics spectator items, useful if you have already lost.",
            category = "TNT Tag",
            subcategory = "Features"
    )
    public static boolean bhClickables = true;
    @Property(
            type = PropertyType.TEXT,
            name = "Current nick",
            description = "If you're playing nicked, set your nick or you might become the target.",
            category = "TNT Tag",
            subcategory = "Customization",
            placeholder = "Replace"
    )
    public static String nick = "";
    @Property(
            type = PropertyType.SLIDER,
            name = "Play again item",
            description = "Where the §b§lPlay Again §ritem should be placed.",
            category = "TNT Tag",
            subcategory = "Customization",
            min = 1,
            max = 9
    )
    public static int playAgainItem = 8;
    @Property(
            type = PropertyType.SLIDER,
            name = "Return to lobby item",
            description = "Where the §c§lReturn To Lobby &ritem should be placed.",
            category = "TNT Tag",
            subcategory = "Customization",
            min = 1,
            max = 9
    )
    public static int returnToLobbyItem = 9;

    /* TNT Tag */
    @Property(
            type = PropertyType.SWITCH,
            name = "Change window title",
            description = "Changes the window title on world join, similar to what newer versions do. You can manually do this with /settitle",
            category = "Modern Features",
            subcategory = "General"
    )
    public static boolean changeTitle = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Projectile throws",
            description = "Swings your hand when using certain items, such as a snowball or water bucket (on the ground)",
            category = "Modern Features",
            subcategory = "Hand Swings"
    )
    public static boolean itemSwings = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Item drops",
            description = "Swings your hand when dropping an item",
            category = "Modern Features",
            subcategory = "Hand Swings"
    )
    public static boolean dropSwings = false;

    // Features
    @Property(
            type = PropertyType.NUMBER,
            category = "storage",
            name = "playtimeMinutes",
            hidden = true
    )
    public static int playtimeMinutes = 0;
    @Property(
            type = PropertyType.SWITCH,
            category = "storage",
            name = "first",
            hidden = true
    )
    public static boolean first = false;
    @Property(
            type = PropertyType.NUMBER,
            category = "storage",
            name = "displayX",
            hidden = true
    )
    public static int displayX = 5;

    // Customization
    @Property(
            type = PropertyType.NUMBER,
            category = "storage",
            name = "displayY",
            hidden = true
    )
    public static int displayY = 5;
    @Property(
            type = PropertyType.TEXT,
            category = "storage",
            name = "displayedText",
            hidden = true
    )
    public static String displayedText = "";
    @Property(
            type = PropertyType.NUMBER,
            category = "storage",
            name = "bhDisplayX",
            hidden = true
    )
    public static int bhDisplayX = 5;
    @Property(
            type = PropertyType.NUMBER,
            category = "storage",
            name = "bhDisplayY",
            hidden = true
    )
    public static int bhDisplayY = 5;
    @Property(
            type = PropertyType.NUMBER,
            category = "storage",
            name = "points",
            hidden = true
    )
    public static int points = 0;
    @Property(
            type = PropertyType.NUMBER,
            category = "storage",
            name = "kills",
            hidden = true
    )
    public static int kills = 0;
    @Property(
            type = PropertyType.CHECKBOX,
            category = "storage",
            name = "bhFirst",
            hidden = true
    )
    public static boolean bhFirst = true;

    /* Modern features */

    // General

    static {
        try {
            video = new URI("https://www.youtube.com/watch?v=-z_AZR35ozI");
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // Hand Swings

    public YedelConfig() {
        super(
                new File("./config/YedelMod.toml"),
                "YedelMod",
                new JVMAnnotationPropertyCollector(),
                new YedelConfigSortingBehavior()
        );
        initialize();
        setCategoryDescription("General",
                "&9&lYedel&7&lMod " + YedelMod.version + "\nDiscord: &9yedel" +
                        "\n" +
                        "\n&nCommands:" +
                        "\n" +
                        "\n&9/simulatechat (/simc): &7Simulates a given chat message" +
                        "\n&9/settext: &7Adds custom display text, move with &9/movetext &7and remove with &9/cleartext " +
                        "\n&9/settitle: &7Sets the title of the game window." +
                        "\n&9/yedelplaytime (/ypt, /yedelpt): &7Shows your playtime in hours and minutes" +
                        "\n&9/yedelli (/yli, /li): &7Sends you to the lobby/limbo for an invalid character (disconnects on most other servers)" +
                        "\n&9/yping (/yp): &7Shows an estimation of your ping using your selected method (customize below)" +
                        "\n" +
                        "\n\n&nKeybinds: " +

                        "\n\n&9Market searches:" +
                        "\n&7Search the auction house for your currently held item: Bound to &9K &7by default" +
                        "\n&7Search the bazaar for your currently held item: Bound to &9L &7by default" +

                        "\n\n&9Easy atlas verdicts (toggleable below):" +
                        "\n&7Insufficient Evidence: Bound to &9O &7by default" +
                        "\n&7Evidence Without Doubt: Bound to &9P &7by default" +
                        "\n"
        );
        addDependencies();
    }

    public static void save() {
        instance.markDirty();
        instance.writeData();
    }

    // Variables stored but hidden in this config

    private static boolean isAutoGGLoaded() {
        return Loader.isModLoaded("autogg");
    }

    public static UpdateSource getUpdateSource() {
        return YedelConfig.updateSource == 0 ? UpdateSource.MODRINTH : UpdateSource.GITHUB;
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "&9Check for updates",
            description = "Check for updates with the selected source",
            category = "General",
            placeholder = "Check"
    )
    public static void checkForUpdates() {
        UpdateManager.instance.checkVersion(getUpdateSource(), "notification");
    }

    public void addDependencies() {
        addDependency("guildWelcomeMessage", "guildWelcome");
        addDependency("dropperGGDelay", "dropperGG");
        addDependency("startStrengthColor", "strengthIndicators");
        addDependency("endStrengthColor", "strengthIndicators");
        addDependency("favoriteServer", "buttonFavoriteServer");

        addDependency("bhDisplay", "bountyHunting");
        addDependency("bhSounds", "bountyHunting");
        addDependency("bhClickables", "bountyHunting");
        addDependency("openHuntingGui", "bountyHunting");
        addDependency("playSelection", "bountyHunting");
        addDependency("playKill", "bountyHunting");
        addDependency("playAgainItem", "bountyHunting");
        addDependency("returnToLobbyItem", "bountyHunting");
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Video",
            description = "This is a complicated feature, watch my video for help!",
            category = "TNT Tag",
            placeholder = "Open video"
    )
    private void watchVideo() throws IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(video);
        }
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "&cReset stats",
            category = "TNT Tag",
            placeholder = "Reset"
    )
    private void resetConfirmation() {
        Constants.notifications.push("Bounty Hunting", "Are you sure you want to reset your stats? (click)", 3, () -> {
            points = 0;
            kills = 0;
            Constants.notifications.push("Bounty Hunting", "Reset stats!", 3);
            return null;
        });
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Customize display text",
            description = "Customize the display text position, can also be done with /movetext.",
            category = "General",
            subcategory = "Customization",
            placeholder = "Open GUI"
    )
    private void openGui() {
        GuiUtil.open(MoveTextGui.instance);
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Customize display",
            description = "Customize the bounty hunting display, can also be done with /movehuntingtext.",
            category = "TNT Tag",
            subcategory = "Features",
            placeholder = "Open GUI"
    )
    private void openHuntingGui() {
        GuiUtil.open(MoveHuntingTextGui.instance);
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Target selection sound",
            description = "Sound: random.successful_hit at 10 volume and 0.8 pitch.",
            category = "TNT Tag",
            subcategory = "Features",
            placeholder = "Play sound"
    )
    private void playSelection() {
        if (minecraft.theWorld == null) return;
        minecraft.thePlayer.playSound("random.successful_hit", 10, 0.8F);
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Kill sound",
            description = "Sound: random.successful_hit at 10 volume and 1.04 pitch.",
            category = "TNT Tag",
            subcategory = "Features",
            placeholder = "Play sound"
    )
    private void playKill() {
        if (minecraft.theWorld == null) return;
        minecraft.thePlayer.playSound("random.successful_hit", 10, 1.04F);
    }

    // End of configuration
}
