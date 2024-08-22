package at.yedel.yedelmod.config;



import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.gui.MoveHudGui;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateManager.FeedbackMethod;
import at.yedel.yedelmod.utils.update.UpdateSource;
import gg.essential.api.EssentialAPI;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.JVMAnnotationPropertyCollector;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.Loader;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class YedelConfig extends Vigilant {
	private final URI video = new URI("https://www.youtube.com/watch?v=-z_AZR35ozI");

	// Start of config
	// Start of visible config

	/* General */

	// Updates

	@Property(
		type = PropertyType.SELECTOR,
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

	@Property(
		type = PropertyType.SWITCH,
		name = "Automatically check for updates",
		description = "Checks for updates on game load",
		category = "General",
		subcategory = "Updates"
	)
	public boolean autoCheckUpdates = true;

	@Property(
		type = PropertyType.BUTTON,
		name = "Modrinth link",
		description = "Click to open the Modrinth site",
		category = "General",
		subcategory = "Updates",
		placeholder = "Open"
	)
	public void openModrinthLink() {
		try {
			Desktop.getDesktop().browse(UpdateSource.MODRINTH.uri);
		}
		catch (IOException e) {
			EssentialAPI.getNotifications().push("YedelMod", "Couldn't open modrinth link!");
			e.printStackTrace();
		}
	}

	@Property(
		type = PropertyType.BUTTON,
		name = "GitHub link",
		description = "Click to open the GitHub repository",
		category = "General",
		subcategory = "Updates",
		placeholder = "Open"
	)
	public void openGitHubRepository() {
		try {
			Desktop.getDesktop().browse(UpdateSource.GITHUB.uri);
		}
		catch (IOException e) {
			EssentialAPI.getNotifications().push("YedelMod", "Couldn't open github link!");
			e.printStackTrace();
		}
	}

	@Property(
		type = PropertyType.BUTTON,
		name = "Check for updates",
		description = "Check for updates with the selected source",
		category = "General",
		subcategory = "Updates",
		placeholder = "Check"
	)
	public void checkForUpdates() {
		UpdateManager.getInstance().checkForUpdates(getUpdateSource(), FeedbackMethod.NOTIFICATIONS);
	}

	// HUDs

	@Property(
		type = PropertyType.BUTTON,
		name = "Customize HUDs",
		description = "Customize the position of different HUDs.",
		category = "General",
		subcategory = "HUDs",
		placeholder = "Open GUI"
	)
	public void openMoveHudGui() {
		minecraft.displayGuiScreen(new MoveHudGui(minecraft.currentScreen));
	}

	@Property(
		type = PropertyType.CHECKBOX,
		name = "Render HUDs in screens",
		description = "Render HUDs (custom text, bounty hunting text, etc) while in another GUI.",
		category = "General",
		subcategory = "HUDs"
	)
	public boolean renderHudsInScreens = true;
	@Property(
		type = PropertyType.CHECKBOX,
		name = "Render HUDs in F3 menu",
		description = "Render HUDs (custom text, bounty hunting text, etc) while the debug screen is open.",
		category = "General",
		subcategory = "HUDs"
	)
	public boolean renderHudsInDebug = false;

	/* Features */

	// Features

	@Property(
		type = PropertyType.SWITCH,
		name = "Auto welcome guild members",
		description = "Automatically welcomes new guild members with a customizable message." +
			"\n§a[VIP] Yedelos §ejoined the guild!" +
			"\n§2Guild > §b[MVP§8+§b] Yedel §6[Yedel]§f: Welcome, Yedelos!",
		category = "Features",
		subcategory = "Features"
	)
	public boolean guildWelcome = true;
	@Property(
		type = PropertyType.SWITCH,
		name = "Custom hit particles",
		description = "Spawns customizable particles when hitting entities.",
		category = "Features",
		subcategory = "Features"
	)
	public boolean customHitParticles = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Display text",
		description = "Show text which can be customized with -settext and cleared with -cleartext, supporting color codes with ampersands (&).",
		category = "Features",
		subcategory = "Features"
	)
	public boolean displayTextToggled = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Dropper AutoGG",
		description = "AutoGG for dropper, will be removed when it is added to Sk1er's AutoGG." +
			"\n§eNote: This only says gg at the end of the game, not when you finish.",
		category = "Features",
		subcategory = "Features"
	)
	public boolean dropperGG = isAutoGGLoaded();
	@Property(
		type = PropertyType.SWITCH,
		name = "Regex chat filter",
		description = "Use a customizable regular expression to filter chat.",
		category = "Features",
		subcategory = "Features"
	)
	public boolean regexChatFilterToggled = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Random placeholder",
		description = "Type a customizable placeholder to replace it with a random string from a UUID.",
		category = "Features",
		subcategory = "Features"
	)
	public boolean randomPlaceholderToggled = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "SkyWars strength indicators",
		description = "Shows people's strength above their nametags with customizable colors. Accounts for Apothecary.",
		category = "Features",
		subcategory = "Features"
	)
	public boolean strengthIndicators = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Limbo creative mode",
		description = "Automatically gives creative mode in Hypixel limbo, not bannable because the server does not listen to anything happening. " +
			"Use -lgmc in limbo if it doesn't work the first time.",
		category = "Features",
		subcategory = "Features"
	)
	public boolean limboCreative = true;
	@Property(
		type = PropertyType.SWITCH,
		name = "Favorite server button",
		description = "Adds a button to the main menu to join a customizable server address.",
		category = "Features",
		subcategory = "Features"
	)
	public boolean buttonFavoriteServer = false;

	// Tweaks
	@Property(
		type = PropertyType.SWITCH,
		name = "Unformat chat logs",
		description = "Remove formatting that was not removed from chat logs.",
		category = "Features",
		subcategory = "Tweaks"
	)
	public boolean unformatChatLogs = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide missing signature errors",
		description = "Hide \"Signature is missing from textures payload\" errors from being logged.",
		category = "Features",
		subcategory = "Tweaks"
	)
	public boolean hideMissingSignatureErrors = true;

	// Customization

	@Property(
		type = PropertyType.TEXT,
		name = "Guild welcome message",
		description = "Message for new guild members. Use [player] for the new player.",
		category = "Features",
		subcategory = "Customization"
	)
	public String guildWelcomeMessage = "Welcome, [player]!";
	@Property(
		type = PropertyType.SWITCH,
		name = "Only spawn custom particles on players",
		category = "Features",
		subcategory = "Customization"
	)
	public boolean onlySpawnCustomParticlesOnPlayers = false;
	@Property(
		type = PropertyType.SLIDER,
		name = "Particle Y offset",
		description = "Some particles (such as note) may not show well due to being in the player model. Use this for those particles.",
		category = "Features",
		subcategory = "Customization",
		min = -1,
		max = 2
	)
	public int hitParticleYOffset = 0;
	@Property(
		type = PropertyType.SELECTOR,
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
	@Property(
		type = PropertyType.CHECKBOX,
		name = "Random particle type",
		description = "Spawns a random particle type on hit instead of the one chosen above.",
		category = "Features",
		subcategory = "Customization"
	)
	public boolean randomParticleType = false;

	@Property(
		type = PropertyType.SLIDER,
		name = "AutoGG Delay",
		description = "Delay for AutoGG, measured in seconds.",
		category = "Features",
		subcategory = "Customization",
		max = 5
	)
	public int dropperGGDelay = 0;
	@Property(
		type = PropertyType.SELECTOR,
		name = "Strength color",
		description = "Color for strength indicators (5.5s - 0.5s)",
		category = "Features",
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
	public int startStrengthColor = 1;
	@Property(
		type = PropertyType.SELECTOR,
		name = "Sub strength color",
		description = "Color for strength indicators (0.5s - end)",
		category = "Features",
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
	public int endStrengthColor = 2;
	@Property(
		type = PropertyType.TEXT,
		name = "Regex chat filter pattern",
		description = "The pattern to use for regex chat filtering.",
		category = "Features",
		subcategory = "Customization"
	)
	public String regexChatFilterPattern = "";

	@Property(
		type = PropertyType.TEXT,
		name = "Random placeholder text",
		description = "When this is typed in chat, it will be replaced with a random string. §cBe careful not to use short placeholders to not spam excessively.",
		category = "Features",
		subcategory = "Customization",
		placeholder = "//r"
	)
	public String randomString = "//r";

	@Property(
		type = PropertyType.TEXT,
		name = "Specified server",
		description = "Server joined with button",
		category = "Features",
		subcategory = "Customization"
	)
	public String favoriteServer = "mc.hypixel.net";

	/* Commands */
	// Index
	@Property(
		type = PropertyType.CUSTOM,
		name = "/yedel (/yedelmod)",
		description = "The main command, hosting all subcommands. When used with no arguments, opens this config screen.",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$yedel = 1;
	@Property(
		type = PropertyType.CUSTOM,
		name = "- cleartext",
		description = "Clears the currently set display text.",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$cleartext = 1;
	@Property(
		type = PropertyType.CUSTOM,
		name = "- formatting",
		description = "Shows a formatting guide with color and style codes.",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$formatting = 1;

	@Property(
		type = PropertyType.CUSTOM,
		name = "- limbo (li)",
		description = "Sends an illegal chat character, which disconnects you on most servers and sends you to limbo-like areas on some. No longer works on Hypixel, use /limbo instead.",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$limbo = 1;
	@Property(
		type = PropertyType.CUSTOM,
		name = "- limbocreative (limbogmc, lgmc)",
		description = "Gives you creative mode in Hypixel's limbo, given certain checks are passed.",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$limbocreative = 1;
	@Property(
		type = PropertyType.CUSTOM,
		name = "- movehud",
		description = "Opens the HUD customization screen.",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$movehud = 1;

	@Property(
		type = PropertyType.CUSTOM,
		name = "- ping [method]",
		description = "Shows your ping to the server in chat, using several methods. Without an argument, uses the default method which can be customized.",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$ping = 1;
	@Property(
		type = PropertyType.CUSTOM,
		name = "- playtime (pt)",
		description = "Shows your total playtime (while playing on servers) in hours and minutes.",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$playtime = 1;
	@Property(
		type = PropertyType.CUSTOM,
		name = "- setnick [nick]",
		description = "Sets your nick for Bounty Hunting to not select yourself as the target.",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$setnick = 1;
	@Property(
		type = PropertyType.CUSTOM,
		name = "- settext [text]",
		description = "Sets the display text, supporting color codes with ampersands (&).",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$settext = 1;
	@Property(
		type = PropertyType.CUSTOM,
		name = "- settitle [title]",
		description = "Sets the title of the game window.",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$settitle = 1;
	@Property(
		type = PropertyType.CUSTOM,
		name = "- simulatechat (simc) [text]",
		description = "Simulates a chat message, also supports color codes with ampersands (&).",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$simulatechat = 1;
	@Property(
		type = PropertyType.CUSTOM,
		name = "- update [platform]",
		description = "Checks for mod updates. Without an argument, uses the default one (modrinth). Possible platforms are \"modrinth\" or \"github\".",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$update = 1;
	@Property(
		type = PropertyType.CUSTOM,
		name = "- yedelmessage (message)",
		description = "Shows messages from me about the mod. These can be anything from tips to bug notices.",
		category = "Commands",
		subcategory = "Index",
		customPropertyInfo = EmptyProperty.class
	)
	public int command$yedelmessage = 1;

	// Customization
	@Property(
		type = PropertyType.SELECTOR,
		name = "Ping method",
		description =
			"§9Ping: §7Does /ping command. Works on very few servers." +
				"\n§9Command: §7Enters a random command and waits for the unknown command response. Works on almost all servers." +
				"\n§9Tab: §7Sends a tab completion packet and waits for the response. Works on all servers." +
				"\n§9Stats: §7Sends a statistics packet and waits for the response. Works on all servers." +
				"\n§9Server list (default): §7Gets the ping displayed previously on the server list. Doesn't work on singleplayer and if you used Direct Connect." +
				"\n§9Hypixel: §7Uses the Hypixel ping packet and waits for the response. Only works on hypixel.",
		category = "Commands",
		subcategory = "Customization",
		options = {"Ping", "Command", "Tab", "Stats", "Server list (default)", "Hypixel"}
	)
	public int pingMethod = 1;

	/* Keybinds */

	// Keybinds
	@Property(
		type = PropertyType.SWITCH,
		name = "Search the auction house for your held item",
		description =
			"Searches the auction house for your currently held item's name, you may need to switch categories to see your item." +
				"\nBound to §9K §7by default.",
		category = "Keybinds",
		subcategory = "Keybinds"
	)
	public boolean ahSearch = true;
	@Property(
		type = PropertyType.SWITCH,
		name = "Search the bazaar for your held item",
		description =
			"Searches the bazaar for your currently held item's name." +
				"\nBound to §9L §7by default.",
		category = "Keybinds",
		subcategory = "Keybinds"
	)
	public boolean bzSearch = true;
	@Property(
		type = PropertyType.SWITCH,
		name = "Easy atlas verdicts",
		description =
			"Adds keybinds for the two atlas verdicts in your hotbar. \nThis automatically clicks for you, so it is §cuse at your own risk." +
				"\n§7Insufficient Evidence: Bound to §9O §7by default." +
				"\nEvidence Without Doubt: Bound to §9P §7by default.",
		category = "Keybinds",
		subcategory = "Keybinds"
	)
	public boolean easyAtlasVerdicts = false;

	/* Modern Features */

	// General

	@Property(
		type = PropertyType.SWITCH,
		name = "Book background (1.14+)",
		description = "Draws the default dark background in book GUIs.",
		category = "Modern Features",
		subcategory = "General"
	)
	public boolean drawBookBackground = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Keep chat history on chat clear (1.15.2+)",
		description = "When clearing your chat (F3 + D), keep your message history (from pressing up arrow key).",
		category = "Modern Features",
		subcategory = "General"
	)
	public boolean keepChatHistory = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Change window title (1.15.2+)",
		description = "Changes the window title on world and server join. \nYou can manually do this with -settitle.",
		category = "Modern Features",
		subcategory = "General"
	)
	public boolean changeTitle = false;
	@Property(
		type = PropertyType.SWITCH,
		name = "Damage Tilt (1.19.4+)",
		description = "Allows you to customize how much your screen hurts when being damaged.",
		category = "Modern Features",
		subcategory = "General"
	)
	public boolean damageTiltToggled = false;

	// Hand Swings

	@Property(
		type = PropertyType.SWITCH,
		name = "Item uses (1.15+)",
		description = "Swings your hand when using certain items, such as snowballs, eggs and armor.",
		category = "Modern Features",
		subcategory = "Hand Swings"
	)
	public boolean itemUseSwings = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Item drops (1.15+)",
		description = "Swings your hand when dropping an item.",
		category = "Modern Features",
		subcategory = "Hand Swings"
	)
	public boolean dropSwings = false;

	// Customization
	@Property(
		type = PropertyType.PERCENT_SLIDER,
		name = "Damage Tilt Strength",
		description = "The amount of camera shake caused by being hurt.",
		category = "Modern Features",
		subcategory = "Customization"
	)
	public float damageTiltStrength = 1f;

	/* BedWars */

	// HUDs
	@Property(
		type = PropertyType.SWITCH,
		name = "XP display",
		description = "Shows your experience out of 5,000. Inaccurate for lower levels (0-4).",
		category = "BedWars",
		subcategory = "HUDs"
	)
	public boolean xpDisplay = true;
	@Property(
		type = PropertyType.SWITCH,
		name = "Magic milk display",
		description = "Shows how long your Magic Milk will last for.",
		category = "BedWars",
		subcategory = "HUDs"
	)
	public boolean magicMilkDisplay = true;

	// Challenges
	@Property(
		type = PropertyType.SWITCH,
		name = "BedWars defusal helper",
		description = "Highlights redstone for the BedWars defusal challenge.",
		category = "BedWars",
		subcategory = "Challenges"
	)
	public boolean defusalHelper = true;

	// Chat
	@Property(
		type = PropertyType.CHECKBOX,
		name = "Light green token messages",
		description = "Make token messages §alight green §7instead of §2green §7(only in bedwars) to make them appear different from emerald messages.",
		category = "BedWars",
		subcategory = "Chat"
	)
	public boolean lightGreenTokenMessages = false;
	@Property(
		type = PropertyType.CHECKBOX,
		name = "Hide slumber ticket messages",
		description = "Hide slumber ticket messages in-game from things like kills and wins.",
		category = "BedWars",
		subcategory = "Chat"
	)
	public boolean hideSlumberTicketMessages = false;
	@Property(
		type = PropertyType.CHECKBOX,
		name = "Hide item pickup messages",
		description = "Hide \"§oYou picked up: ...\" messages.",
		category = "BedWars",
		subcategory = "Chat"
	)
	public boolean hideItemPickupMessages = false;
	@Property(
		type = PropertyType.CHECKBOX,
		name = "Hide silver coin count",
		description = "Hide the silver coin count from item purchase messages.",
		category = "BedWars",
		subcategory = "Chat"
	)
	public boolean hideSilverCoinCount = false;
	@Property(
		type = PropertyType.CHECKBOX,
		name = "Hide comfy pillow messages",
		description = "Hides the following messages:" +
			"\n\"§aYou are now carrying §ex1 Comfy Pillows§a, bring it back to your shop keeper!\"" +
			"\n\"§cYou cannot return items to another team's Shopkeeper!\"" +
			"\n\"§cYou cannot carry any more Comfy Pillows!\"" +
			"\n\"§cYou died while carrying §e1x Comfy Pillows§c!\"",
		category = "BedWars",
		subcategory = "Chat"
	)
	public boolean hideComfyPillowMessages = false;
	@Property(
		type = PropertyType.CHECKBOX,
		name = "Hide dreamer's soul fragment messages",
		description = "Hide \"§e+1 §cDreamer's Soul Fragment§e!§7\" messages.",
		category = "BedWars",
		subcategory = "Chat"
	)
	public boolean hideDreamerSoulFragmentMessages = false;

	/* TNT Tag */

	//

	@Property(
		type = PropertyType.SWITCH,
		name = "Bounty Hunting",
		description = "Adds a bounty hunting minigame to TNT Tag.",
		category = "TNT Tag"
	)
	public boolean bountyHunting = true;

	@Property(
		type = PropertyType.BUTTON,
		name = "Reset stats",
		category = "TNT Tag",
		placeholder = "Reset"
	)
	private void resetConfirmation() {
		EssentialAPI.getNotifications().push("Bounty Hunting", "Are you sure you want to reset your stats? (click)", 3, () -> {
			points = 0;
			kills = 0;
			EssentialAPI.getNotifications().push("Bounty Hunting", "Reset stats!", 3);
			return null;
		});
	}

	@Property(
		type = PropertyType.BUTTON,
		name = "Video",
		description = "This is a complicated feature, watch my video if you need help!",
		category = "TNT Tag",
		placeholder = "Open video"
	)
	private void watchVideo() {
		try {
			Desktop.getDesktop().browse(video);
		}
		catch (IOException e) {
			EssentialAPI.getNotifications().push("YedelMod", "Couldn't open video!");
			e.printStackTrace();
		}
	}

	// Features

	@Property(
		type = PropertyType.SWITCH,
		name = "Highlight target and show distance",
		description = "Distance is displayed above their nametag, corresponding to their rank color.",
		category = "TNT Tag",
		subcategory = "Features"
	)
	public boolean bhDisplay = true;
	@Property(
		type = PropertyType.SWITCH,
		name = "Play sounds for target selections and kills",
		description = "Use the buttons below to test these sounds.",
		category = "TNT Tag",
		subcategory = "Features"
	)
	public boolean bhSounds = true;

	@Property(
		type = PropertyType.BUTTON,
		name = "Target selection sound",
		description = "Sound: random.successful_hit at 0.8 pitch.",
		category = "TNT Tag",
		subcategory = "Features",
		placeholder = "Play sound"
	)
	private void playSelection() {
		Functions.playSound("random.successful_hit", 0.8F);
	}

	@Property(
		type = PropertyType.BUTTON,
		name = "Kill sound",
		description = "Sound: random.successful_hit at 1.04 pitch.",
		category = "TNT Tag",
		subcategory = "Features",
		placeholder = "Play sound"
	)
	private void playKill() {
		Functions.playSound("random.successful_hit", 1.04F);
	}
	// Customization

	@Property(
		type = PropertyType.TEXT,
		name = "Current nick",
		description = "If you're playing nicked, set your nick or you might become the target.",
		category = "TNT Tag",
		subcategory = "Customization",
		placeholder = "Replace"
	)
	public String nick = "";

	// End of visible config

	// Hidden variables for data
	@Property(type = PropertyType.NUMBER, category = "storage", name = "playtimeMinutes", hidden = true)
	public int playtimeMinutes = 0;
	@Property(type = PropertyType.SWITCH, category = "storage", name = "first", hidden = true)
	public boolean first = false;
	@Property(type = PropertyType.TEXT, category = "storage", name = "displayedText", hidden = true)
	public String displayedText = "";
	@Property(type = PropertyType.NUMBER, category = "storage", name = "displayX", hidden = true)
	public int displayX = 5;
	@Property(type = PropertyType.NUMBER, category = "storage", name = "displayY", hidden = true)
	public int displayY = 5;
	@Property(type = PropertyType.NUMBER, category = "storage", name = "xpDisplayX", hidden = true)
	public int xpDisplayX = 5;
	@Property(type = PropertyType.NUMBER, category = "storage", name = "xpDisplayY", hidden = true)
	public int xpDisplayY = 15;
	@Property(type = PropertyType.NUMBER, category = "storage", name = "magicMilkDisplayX", hidden = true)
	public int magicMilkDisplayX = 5;
	@Property(type = PropertyType.NUMBER, category = "storage", name = "magicMilkDisplayY", hidden = true)
	public int magicMilkDisplayY = 25;
	@Property(type = PropertyType.NUMBER, category = "storage", name = "bhDisplayX", hidden = true)
	public int bhDisplayX = 5;
	@Property(type = PropertyType.NUMBER, category = "storage", name = "bhDisplayY", hidden = true)
	public int bhDisplayY = 35;
	@Property(type = PropertyType.NUMBER, category = "storage", name = "points", hidden = true)
	public int points = 0;
	@Property(type = PropertyType.NUMBER, category = "storage", name = "kills", hidden = true)
	public int kills = 0;
	@Property(type = PropertyType.CHECKBOX, category = "storage", name = "bhFirst", hidden = true)
	public boolean bhFirst = true;

	// End of config

	public YedelConfig() throws URISyntaxException {
		super(
			new File("./config/YedelMod.toml"),
			"YedelMod",
			new JVMAnnotationPropertyCollector(),
			new YedelConfigSortingBehavior()
		);
		initialize();
		setCategoryDescription("General",
			"§9§lYedel§7§lMod " + YedelMod.version + "\nDiscord: §9yedel" +
				"\n§7Some properties may mention subcommands with -(the subcommand). They refer to using /yedel (the subcommand)."
		);
		setSubcategoryDescription("Features", "Tweaks",
			"Smaller features that change the game, similar to features in Patcher."
		);
		setCategoryDescription("Commands",
			"Description of this mod's subcommands, all under /yedel. " +
				"\nFormat: §9- command (any aliases) [arguments]" +
				"\nExample: §9- simulatechat (simc) [text] §7-> §9/yedel simc Hi!"
		);
		setCategoryDescription("Modern Features", "Features backported from newer versions of the game.");
		setSubcategoryDescription("Modern Features", "Hand Swings", "Features that add extra hand swings on certain actions. Note: this only swings your hand client-side.");
		setCategoryDescription("TNT Tag", "Features relating to TNT Tag, mainly bounty hunting.");
		addDependencies();
	}

	public void save() {
		instance.markDirty();
		instance.writeData();
	}

	private boolean isAutoGGLoaded() {
		return Loader.isModLoaded("autogg");
	}

	public void addDependencies() {
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

	private static final YedelConfig instance;

	static {
		try {
			instance = new YedelConfig();
		}
		catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static YedelConfig getInstance() {
		return instance;
	}
}
