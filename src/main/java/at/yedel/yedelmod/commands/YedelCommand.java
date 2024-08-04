package at.yedel.yedelmod.commands;



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.LimboCreativeCheck;
import at.yedel.yedelmod.features.major.ping.PingSender;
import at.yedel.yedelmod.gui.MoveHudGui;
import at.yedel.yedelmod.gui.MoveHuntingTextGui;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.Requests;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateManager.FeedbackMethod;
import at.yedel.yedelmod.utils.update.UpdateSource;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.Display;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class YedelCommand extends CommandBase {
	private static final URL modJsonUri;

	static {
		try {
			modJsonUri = new URL("https://yedelo.github.io/yedelmod.json");
		}
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getCommandName() {
		return "yedel";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "Usage: /yedel (subcommand)";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length == 0) {
			Functions.displayScreen(YedelConfig.getInstance().gui());
			return;
		}
		String firstArg = args[0];
		String secondArg = args.length > 1? args[1] : null;
		boolean noSecondArg = secondArg == null;
		switch (firstArg) {
			case "cleartext":
				YedelConfig.getInstance().displayedText = "";
				YedelConfig.getInstance().save();
				Chat.display(Messages.clearedDisplayText);
				break;
			case "formatting":
				Chat.display(Messages.formattingGuideMessage);
				break;
			case "limbo":
			case "li":
				Chat.say("§");
				break;
			case "limbocreative":
			case "limbogmc":
			case "lgmc":
				LimboCreativeCheck.getInstance().checkLimbo();
				break;
			case "movehud":
				Functions.displayScreen(new MoveHudGui(minecraft.currentScreen));
				break;
			case "movehuntingtext":
				Functions.displayScreen(new MoveHuntingTextGui(minecraft.currentScreen));
				break;
			case "ping":
				PingSender.getInstance().processPingCommand(secondArg);
				break;
			case "playtime":
			case "pt":
				int minutes = YedelConfig.getInstance().playtimeMinutes;
				Chat.logoDisplay("&ePlaytime: &6" + minutes / 60 + " hours &eand &6" + minutes % 60 + " minutes");
				break;
			case "setnick":
				if (noSecondArg) {
					Chat.display(Messages.enterValidNick);
					return;
				}
				Chat.display("&6&l[BountyHunting] §eSet nick to " + secondArg + "§e!");
				YedelConfig.getInstance().nick = secondArg;
				YedelConfig.getInstance().save();
				break;
			case "settext":
				if (noSecondArg) {
					Chat.display(Messages.enterValidText);
					return;
				}
				String displayText = TextUtils.replaceAmpersand(TextUtils.joinArgs(args).substring(8));
				YedelConfig.getInstance().displayedText = displayText;
				YedelConfig.getInstance().save();
				Chat.logoDisplay("&eSet displayed text to \"&r" + displayText + "&e\"!");
				break;
			case "settitle":
				if (noSecondArg) {
					Chat.display(Messages.enterValidTitle);
					return;
				}
				String title = TextUtils.joinArgs(args).substring(9);
				Chat.logoDisplay("&eSet display title to \"&f" + title + "&e\"!");
				Display.setTitle(title);
				break;
			case "simulatechat":
			case "simc":
				String[] newArray = new String[args.length - 1];
				System.arraycopy(args, 1, newArray, 0, newArray.length);
				String message = TextUtils.joinArgs(newArray);
				Chat.display(message);
				break;
			case "update":
				if (noSecondArg) {
					UpdateManager.getInstance().checkForUpdates(YedelConfig.getInstance().getUpdateSource(), FeedbackMethod.CHAT);
					return;
				}
				if (secondArg.equals("modrinth")) {
					UpdateManager.getInstance().checkForUpdates(UpdateSource.MODRINTH, FeedbackMethod.CHAT);
				}
				else if (secondArg.equals("github")) {
					UpdateManager.getInstance().checkForUpdates(UpdateSource.GITHUB, FeedbackMethod.CHAT);
				}
				else {
					UpdateManager.getInstance().checkForUpdates(YedelConfig.getInstance().getUpdateSource(), FeedbackMethod.CHAT);
				}
				break;
			case "yedelmessage":
			case "message":
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
				break;
			default:
				Chat.display(Messages.unknownSubcommandMessage);
		}
	}

	@Override
	public List<String> getCommandAliases() {
		return Collections.singletonList("yedelmod");
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	private final String[] baseTabCompletions = new String[] {
		"cleartext", "formatting", "limbo", "li", "limbocreative", "limbogmc", "lgmc", "movehud", "movehuntingtext", "ping", "playtime", "pt", "setnick", "settext", "settitle", "simulatechat", "simc", "update", "yedelmessage", "message"
	};
	private final String[] pingTabCompletions = new String[] {
		"ping", "command", "tab", "stats", "list", "hypixel"
	};
	private final String[] updateTabCompletions = new String[] {
		"modrinth", "github"
	};

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		String firstArg = args.length > 0? args[0] : null;
		String secondArg = args.length > 1? args[1] : null;
		if (firstArg == null || firstArg.isEmpty()) {
			return Arrays.asList(baseTabCompletions);
		}
		else if (firstArg.equals("ping")) {
			if (secondArg != null) {
				return Arrays.stream(pingTabCompletions).filter(tabCompletion -> tabCompletion.startsWith(secondArg)).collect(Collectors.toList());
			}
			else {
				return Arrays.asList(pingTabCompletions);
			}
		}

		else if (firstArg.equals("update")) {
			if (secondArg != null) {
				return Arrays.stream(updateTabCompletions).filter(tabCompletion -> tabCompletion.startsWith(secondArg)).collect(Collectors.toList());
			}
			else {
				return Arrays.asList(updateTabCompletions);
			}
		}
		else if (secondArg == null) {
			return Arrays.stream(baseTabCompletions).filter(tabCompletion ->
				TextUtils.removeFormatting(tabCompletion).startsWith(firstArg) // This is my own array. Why does it have formatting???
				// i think because the tab completion text is gray (stupid)
			).collect(Collectors.toList());
		}
		return null;
	}
}
