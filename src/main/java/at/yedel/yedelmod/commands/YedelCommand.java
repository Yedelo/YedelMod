package at.yedel.yedelmod.commands;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.CustomText;
import at.yedel.yedelmod.features.LimboCreativeCheck;
import at.yedel.yedelmod.features.major.ping.PingSender;
import at.yedel.yedelmod.gui.MoveHuntingTextGui;
import at.yedel.yedelmod.gui.MoveTextGui;
import at.yedel.yedelmod.update.UpdateManager;
import at.yedel.yedelmod.update.UpdateSource;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class YedelCommand extends CommandBase {
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
				CustomText.getInstance().setDisplayedText("");
				YedelConfig.getInstance().displayedText = "";
				YedelConfig.getInstance().save();
				Chat.display(Messages.clearedDisplayText);
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
			case "movehuntingtext":
				Functions.displayScreen(new MoveHuntingTextGui(minecraft.currentScreen));
				break;
			case "movetext":
				Functions.displayScreen(new MoveTextGui(minecraft.currentScreen));
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
				String displayText = TextUtils.joinArgs(args).substring(8);
				displayText = displayText.replaceAll("&", "§");
				CustomText.getInstance().setDisplayedText(displayText);
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
					UpdateManager.getInstance().checkVersion(YedelConfig.getInstance().getUpdateSource(), "chat");
					return;
				}
				if (secondArg.equals("modrinth")) {
					UpdateManager.getInstance().checkVersion(UpdateSource.MODRINTH, "chat");
				}
				else if (secondArg.equals("github")) {
					UpdateManager.getInstance().checkVersion(UpdateSource.GITHUB, "chat");
				}
				else {
					UpdateManager.getInstance().checkVersion(YedelConfig.getInstance().getUpdateSource(), "chat");
				}
				break;
			case "yedelmessage":
			case "message":
				new Thread(() -> {
					try {
						CloseableHttpClient client = HttpClients.createDefault();
						HttpGet request = new HttpGet("https://yedelo.github.io/yedelmod.json");
						request.addHeader("User-Agent", HttpHeaders.USER_AGENT);

						HttpResponse response;
						response = client.execute(request);

						BufferedReader reader;
						reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						StringBuilder result = new StringBuilder();
						String line;
						while ((line = reader.readLine()) != null) {
							result.append(line);
						}
						JsonObject jsonResult = new JsonParser().parse(String.valueOf(result)).getAsJsonObject();
						String formattedMessage = String.valueOf(jsonResult.get("yedelmod-message-formatted")).replaceAll("\"", "");
						Chat.display(Messages.messageFromYedel);
						Chat.display(formattedMessage);
					}
					catch (Exception e) {
						LogManager.getLogger("Mod Message").error("Couldn't get mod message");
						Chat.display(Messages.couldntGetMessage);
					}
				}, "YedelMod").start();
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
		"cleartext", "limbo", "li", "limbocreative", "limbogmc", "lgmc", "movehuntingtext", "movetext", "ping", "playtime", "pt", "setnick", "settext", "settitle", "simulatechat", "simc", "update", "yedelmessage", "message"
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
			).collect(Collectors.toList());
		}
		return null;
	}
}
