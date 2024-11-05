package at.yedel.yedelmod.features;



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.major.ping.PingSender;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.Requests;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateManager.FeedbackMethod;
import at.yedel.yedelmod.utils.update.UpdateSource;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Greedy;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommandGroup;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.Display;

import static at.yedel.yedelmod.YedelMod.minecraft;



@Command(value = "yedel", description = "The main command for YedelMod, hosting all subcommands. When used with no arguments, opens the config screen.")
public class YedelCommand {
	private YedelCommand() {}

	private static final YedelCommand instance = new YedelCommand();

	public static YedelCommand getInstance() {
		return instance;
	}

	private static final URL modJsonUri;

	static {
		try {
			modJsonUri = new URL("https://yedelo.github.io/yedelmod.json");
		}
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Main(description = "Opens the config screen.")
	private void main() {
		YedelConfig.getInstance().openGui();
	}

	@SubCommand(description = "Clears the currently set display text.")
	private void cleartext() {
		YedelConfig.getInstance().displayedText = "";
		YedelConfig.getInstance().save();
		Chat.display(Messages.clearedDisplayText);
	}

	@SubCommand(description = "Shows a formatting guide with color and style codes.")
	private void formatting() {
		Chat.display(Messages.formattingGuideMessage);
	}

	@SubCommand(description = "Sends an illegal chat character, which disconnects you on most servers and sends you to limbo-like areas on some. No longer works on Hypixel, use /limbo instead.", aliases = "li")
	private void limbo() {
		Chat.say("§");
	}

	@SubCommand(description = "Gives you creative mode in Hypixel's limbo, given certain checks are passed.", aliases = {"limbogmc", "lgmc"})
	private void limbocreative() {
		LimboCreativeCheck.getInstance().checkLimbo();
	}

	@SubCommand(description = "Opens the HUD customization screen.")
	private void movehud() {
		Functions.displayScreen(new MoveHudGui(minecraft.currentScreen));
	}

	@SubCommandGroup(value = "ping")
	private static class Ping {
		@Main(description = "Shows your ping to the server in chat with the method specified in the config.")
		private void main() {
			PingSender.getInstance().defaultMethodPing();
		}

		@SubCommand(description = "Does /ping command. Works on very few servers.", aliases = "p")
		private void ping() {
			PingSender.getInstance().pingPing();
		}

		@SubCommand(description = "Enters a random command and waits for the unknown command response. Works on almost all servers.", aliases = "c")
		private void command() {
			PingSender.getInstance().commandPing();
		}

		@SubCommand(description = "Sends a tab completion packet and waits for the response. Works on all servers.", aliases = "t")
		private void tab() {
			PingSender.getInstance().tabPing();
		}

		@SubCommand(description = "Sends a statistics packet and waits for the response. Works on all servers.", aliases = "s")
		private void stats() {
			PingSender.getInstance().statsPing();
		}

		@SubCommand(description = "Gets the ping displayed previously on the server list. Doesn't work on singleplayer and if you used Direct Connect.", aliases = "l")
		private void list() {
			PingSender.getInstance().serverListPing();
		}

		@SubCommand(description = "Uses the Hypixel ping packet and waits for the response. Only works on hypixel.", aliases = "h")
		private void hypixel() {
			PingSender.getInstance().hypixelPing();
		}
	}

	@SubCommand(description = "Shows your total playtime (while playing on servers) in hours and minutes.", aliases = "pt")
	private void playtime() {
		int minutes = YedelConfig.getInstance().playtimeMinutes;
		Chat.logoDisplay("&ePlaytime: &6" + minutes / 60 + " hours &eand &6" + minutes % 60 + " minutes");
	}

	@SubCommand(description = "Sets your nick for Bounty Hunting to not select yourself as the target.")
	private void setnick(String nick) {
		Chat.display("&6&l[BountyHunting] §eSet nick to " + nick + "§e!");
		YedelConfig.getInstance().nick = nick;
		YedelConfig.getInstance().save();
	}

	@SubCommand(description = "Sets the display text, supporting color codes with ampersands (&).")
	private void settext(@Greedy String text) {
		YedelConfig.getInstance().displayedText = text;
		YedelConfig.getInstance().save();
		Chat.logoDisplay("&eSet displayed text to \"&r" + text + "&e\"!");
	}

	@SubCommand(description = "Sets the title of the game window.")
	private void settitle(@Greedy String text) {
		Chat.logoDisplay("&eSet display title to \"&f" + text + "&e\"!");
		Display.setTitle(text);
	}

	@SubCommand(description = "Simulates a chat message, also supports color codes with ampersands (&).", aliases = "simc")
	private void simulatechat(@Greedy String text) {
		Chat.display(text);
	}

	@SubCommandGroup(value = "update")
	private static class Update {
		@Main(description = "Checks for mod updates with the method specified in the config.")
		private void main() {
			UpdateManager.getInstance().checkForUpdates(YedelConfig.getInstance().getUpdateSource(), FeedbackMethod.CHAT);
		}

		@SubCommand(description = "Checks for mod updates on Modrinth.")
		private void modrinth() {
			UpdateManager.getInstance().checkForUpdates(UpdateSource.MODRINTH, FeedbackMethod.CHAT);
		}

		@SubCommand(description = "Checks for mod updates on GitHub.")
		private void github() {
			UpdateManager.getInstance().checkForUpdates(UpdateSource.GITHUB, FeedbackMethod.CHAT);
		}
	}

	@SubCommand(description = "Shows messages from me about the mod. These can be anything from tips to bug notices.", aliases = "message")
	private void yedelmessage() {
		new Thread(() -> {
			try {
				String yedelMessage = Requests.getJsonObject(modJsonUri).
					getAsJsonObject().
					get("yedelmod-message-formatted").
					getAsString();
				Chat.display(Messages.messageFromYedel);
				Chat.display(yedelMessage);
			}
			catch (IOException e) {
				Chat.display(Messages.couldntGetMessage);
				e.printStackTrace();
			}
		}, "YedelMod Message").start();
	}

	private boolean isArgEmpty(String arg) {
		return StringUtils.isEmpty(arg);
	}
}
