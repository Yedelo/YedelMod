package at.yedel.yedelmod.config;



import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import at.yedel.yedelmod.features.MoveHudGui;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateManager.FeedbackMethod;
import at.yedel.yedelmod.utils.update.UpdateSource;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Exclude;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.utils.Notifications;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.Loader;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class YedelConfig extends Config {
	private YedelConfig() {
		super(
			new Mod(
				"YedelMod",
				ModType.UTIL_QOL,
				"assets/yedelmod/yedelmod.png",
				255,
				255
			),
			"./YedelMod.json",
			true,
			false
		);
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

	@Exclude
	private static final URI videoURI;

	static {
		try {
			videoURI = new URI("https://www.youtube.com/watch?v=-z_AZR35ozI");
		}
		catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	// Start of config
	// Start of visible config

	/* Category - General */

	// Subcategory - Updates
	@Dropdown(
		name = "Update source",
		description = "Where to get updates from. Use GitHub for earlier releases and Modrinth for more stable releases.",
		options = {"Modrinth", "GitHub"},
		category = "General",
		subcategory = "Updates"
	)
	private int updateSource = 0;

	public UpdateSource getUpdateSource() {
		return updateSource == 0? UpdateSource.MODRINTH : UpdateSource.GITHUB;
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
		text = "Open",
		category = "General",
		subcategory = "Updates"
	)
	private final Runnable openModrinthLink = () -> {
		try {
			Desktop.getDesktop().browse(UpdateSource.MODRINTH.uri);
		}
		catch (IOException e) {
			Notifications.INSTANCE.send("YedelMod", "Couldn't open modrinth link!");
			e.printStackTrace();
		}
	};
	@Button(
		name = "GitHub link",
		description = "Click to open the GitHub repository",
		text = "Open",
		category = "General",
		subcategory = "Updates"
	)
	private final Runnable openGithubRepository = () -> {
		try {
			Desktop.getDesktop().browse(UpdateSource.GITHUB.uri);
		}
		catch (IOException e) {
			Notifications.INSTANCE.send("YedelMod", "Couldn't open github link!");
			e.printStackTrace();
		}
	};
	@Button(
		name = "Check for updates",
		description = "Check for updates with the selected source",
		text = "Check",
		category = "General",
		subcategory = "Updates"
	)
	private final Runnable checkForUpdates = () -> {
		UpdateManager.getInstance().checkForUpdates(getUpdateSource(), FeedbackMethod.NOTIFICATIONS);
	};

	// Subcategory - HUDs
	@Button(
		name = "Customize HUDs",
		description = "Customize the position of different HUDs.",
		text = "Open GUI",
		category = "General",
		subcategory = "HUDs"
	)
	private final Runnable openMoveHudGui = () -> {
		minecraft.displayGuiScreen(new MoveHudGui(minecraft.currentScreen));
	};
	@Checkbox(
		name = "Render HUDs in screens",
		description = "Render HUDs (custom text, bounty hunting text, etc) while in another GUI.",
		category = "General",
		subcategory = "HUDs"
	)
	public boolean renderHudsInScreens = true;
	@Checkbox(
		name = "Render HUDs in F3 menu",
		description = "Render HUDs (custom text, bounty hunting text, etc) while the debug screen is open.",
		category = "General",
		subcategory = "HUDs"
	)
	public boolean renderHudsInDebug = false;

	/* Category - Features */

	// Subcategory - Features
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

	// Subcategory - Tweaks
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

	// Subcategory - Customization
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
		min = -1,
		max = 2,
		category = "Features",
		subcategory = "Customization"
	)
	public int hitParticleYOffset = 0;
	@Dropdown(
		name = "Custom particle type",
		description = "The custom particle to be spawned when attacking an entity.",
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
		},
		category = "Features",
		subcategory = "Customization"
	)
	public int hitParticleType = EnumParticleTypes.NOTE.getParticleID();
	@Switch(
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
		min = 0,
		max = 5,
		step = 1,
		subcategory = "Customization"
	)
	public int dropperGGDelay = 0;
	@Dropdown(
		name = "Strength color",
		description = "Color for strength indicators (5.5s - 0.5s)",
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
		},
		category = "Features",
		subcategory = "Customization"
	)
	public int startStrengthColor = 1;
	@Dropdown(
		name = "Sub strength color",
		description = "Color for strength indicators (0.5s - end)",
		category = "Features",
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
		},
		subcategory = "Customization"
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
		subcategory = "Customization"
	)
	public String randomString = "//r";
	@Text(
		name = "Specified server",
		description = "Server joined with button",
		category = "Features",
		subcategory = "Customization"
	)
	public String favoriteServer = "mc.hypixel.net";

	/* Category - Commands */

	// Subcategory - Index
	@Info(
		text = "/yedel (/yedelmod): The main command, hosting all subcommands. When used with no arguments, opens this config screen.",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$yedel = null;
	@Info(
		text = "- cleartext: Clears the currently set display text.",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$cleartext = null;
	@Info(
		text = "- formatting: Shows a formatting guide with color and style codes.",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$formatting = null;
	@Info(
		text = "- limbo (li): Sends an illegal chat character, which disconnects you on most servers and sends you to limbo-like areas on some. No longer works on Hypixel, use /limbo instead.",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$limbo = null;
	@Info(
		text = "- limbocreative (limbogmc, lgmc): Gives you creative mode in Hypixel's limbo, given certain checks are passed.",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$limbocreative = null;
	@Info(
		text = "- movehud: Opens the HUD customization screen.",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$movehud = null;
	@Info(
		text = "- ping [method]: Shows your ping to the server in chat, using several methods. Without an argument, uses the default method which can be customized.",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$ping = null;
	@Info(
		text = "- playtime (pt): Shows your total playtime (while playing on servers) in hours and minutes.",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$playtime = null;
	@Info(
		text = "- setnick [nick]: Sets your nick for Bounty Hunting to not select yourself as the target.",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$setnick = null;
	@Info(
		text = "- settext [text]: Sets the display text, supporting color codes with ampersands (&).",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$settext = null;
	@Info(
		text = "- settitle [title]: Sets the title of the game window.",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$settitle = null;
	@Info(
		text = "- simulatechat (simc) [text]: Simulates a chat message, also supports color codes with ampersands (&).",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$simulatechat = null;
	@Info(
		text = "- update [platform]: Checks for mod updates. Without an argument, uses the default one (modrinth). Possible platforms are \"modrinth\" or \"github\".",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$update = null;
	@Info(
		text = "- yedelmessage (message): Shows messages from me about the mod. These can be anything from tips to bug notices.",
		type = InfoType.INFO,
		size = 2,
		category = "Commands",
		subcategory = "Index"
	)
	private final Object ignored$command$yedelmessage = null;

	// Subcategory - Customization
	@Dropdown(
		name = "Ping method",
		description =
			"Ping: Does /ping command. Works on very few servers." +
				"\nCommand (default): Enters a random command and waits for the unknown command response. Works on almost all servers." +
				"\nTab: Sends a tab completion packet and waits for the response. Works on all servers." +
				"\nStats: Sends a statistics packet and waits for the response. Works on all servers." +
				"\nServer list: Gets the ping displayed previously on the server list. Doesn't work on singleplayer and if you used Direct Connect." +
				"\nHypixel: Uses the Hypixel ping packet and waits for the response. Only works on hypixel.",
		options = {"Ping", "Command", "Tab", "Stats", "Server list", "Hypixel"},
		category = "Commands",
		subcategory = "Customization"
	)
	public int pingMethod = 1;

	/* Category - Keybinds */

	// Subcategory - Keybinds
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

	/* Category - Modern Features */

	// Subcategory - General
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

	// Subcategory - Hand Swings
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

	// Subcategory - Customization
	@Slider(
		name = "Damage Tilt Strength",
		description = "The amount of camera shake caused by being hurt.",
		min = -1,
		max = 1,
		category = "Modern Features",
		subcategory = "Customization"
	)
	public float damageTiltStrength = 1;

	/* Category - BedWars */

	// Subcategory - HUDs
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

	// Subcategory - Challenges
	@Switch(
		name = "BedWars defusal helper",
		description = "Highlights redstone for the BedWars defusal challenge.",
		category = "BedWars",
		subcategory = "Challenges"
	)
	public boolean defusalHelper = true;

	// Subcategory - Chat
	@Switch(
		name = "Light green token messages",
		description = "Make token messages light green instead of green (only in bedwars) to make them appear different from emerald messages.",
		category = "BedWars",
		subcategory = "Chat"
	)
	public boolean lightGreenTokenMessages = false;
	@Switch(
		name = "Hide slumber ticket messages",
		description = "Hide slumber ticket messages in-game from things like kills and wins.",
		category = "BedWars",
		subcategory = "Chat"
	)
	public boolean hideSlumberTicketMessages = false;
	@Switch(
		name = "Hide item pickup messages",
		description = "Hide \"You picked up: ...\" messages.",
		category = "BedWars",
		subcategory = "Chat"
	)
	public boolean hideItemPickupMessages = false;
	@Switch(
		name = "Hide silver coin count",
		description = "Hide the silver coin count from item purchase messages.",
		category = "BedWars",
		subcategory = "Chat"
	)
	public boolean hideSilverCoinCount = false;
	@Switch(
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
	@Switch(
		name = "Hide dreamer's soul fragment messages",
		description = "Hide \"+1 Dreamer's Soul Fragment!\" messages.",
		category = "BedWars",
		subcategory = "Chat"
	)
	public boolean hideDreamerSoulFragmentMessages = false;

	/* Category - TNT Tag */

	// Subcategory -
	@Switch(
		name = "Bounty Hunting",
		description = "Adds a bounty hunting minigame to TNT Tag.",
		category = "TNT Tag"
	)
	public boolean bountyHunting = true;
	@Button(
		name = "Reset stats",
		text = "Reset",
		category = "TNT Tag"
	)
	private final Runnable resetConfirmation = () -> {
		Notifications.INSTANCE.send("Bounty Hunting", "Are you sure you want to reset your stats? (click)", 3, () -> {
			points = 0;
			kills = 0;
			Notifications.INSTANCE.send("Bounty Hunting", "Reset stats!", 3);
			return null;
		});
	};
	@Button(
		name = "Video",
		description = "This is a complicated feature, watch my video if you need help!",
		text = "Open video",
		category = "TNT Tag"
	)
	private final Runnable watchVideo = () -> {
		try {
			Desktop.getDesktop().browse(videoURI);
		}
		catch (IOException e) {
			Notifications.INSTANCE.send("YedelMod", "Couldn't open video!");
			e.printStackTrace();
		}
	};

	// Subcategory - Features
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
		text = "Play sound",
		category = "TNT Tag",
		subcategory = "Features"
	)
	private final Runnable playSelection = () -> {
		Functions.playSound("random.successful_hit", 0.8F);
	};
	@Button(
		name = "Kill sound",
		description = "Sound: random.successful_hit at 1.04 pitch.",
		text = "Play sound",
		category = "TNT Tag",
		subcategory = "Features"
	)
	private final Runnable playKill = () -> {
		Functions.playSound("random.successful_hit", 1.04F);
	};

	// Subcategory - Customization
	@Text(
		name = "Current nick",
		description = "If you're playing nicked, set your nick or you might become the target.",
		category = "TNT Tag",
		subcategory = "Customization"
	)
	public String nick = "";

	// End of visible config

	// Data

	public int playtimeMinutes = 0;
	public boolean first = false;
	public String displayedText = "";
	public int displayX = 5;
	public int displayY = 5;
	public int xpDisplayX = 5;
	public int xpDisplayY = 15;
	public int magicMilkDisplayX = 5;
	public int magicMilkDisplayY = 25;
	public int bhDisplayX = 5;
	public int bhDisplayY = 35;
	public int points = 0;
	public int kills = 0;
	public boolean bhFirst = true;

	// End of config
}
