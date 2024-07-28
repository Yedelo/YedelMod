package at.yedel.yedelmod.utils;



import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import gg.essential.api.EssentialAPI;
import gg.essential.api.gui.Notifications;
import net.minecraft.util.ChatComponentText;



public class Constants {
	public static final Notifications notifications = EssentialAPI.getNotifications();
    public static final String logo = "§8§l- §9§lYedel§7§lMod §8§l-";
	public static final URL modrinthApiUrl;
	public static final URL githubApiUrl;

	static {
		try {
			modrinthApiUrl = new URL("https://api.modrinth.com/v2/project/yedelmod/version");
			githubApiUrl = new URL("https://api.github.com/repos/yedelo/yedelmod/releases/latest");
		}
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static class Messages { // To avoid making the same ChatComponentTexts every time the message is displayed
        public static ChatComponentText unknownSubcommandMessage = new ChatComponentText(logo + " §eUnknown subcommand, refer to the command index (/yedel).");
        public static ChatComponentText YedelUtilsMessage = new ChatComponentText(logo + " §cYedelUtils detected, it will likely completely break this mod. Do §7/ct delete YedelUtils §cto remove it.");
        public static ChatComponentText welcomeMessage = new ChatComponentText(logo + " §7Welcome to §9§lYedel§7§lMod! Use §9/yedel §7for more information.");
		public static ChatComponentText upToDateOnModrinth = new ChatComponentText(logo + "§cYou are up to date with the mod on §aModrinth!");
		public static ChatComponentText upToDateOnGithub = new ChatComponentText(logo + " §cYou are up to date with the mod on §9GitHub!");
        public static ChatComponentText firstTime = new ChatComponentText("§6§l[BountyHunting] §eIf this is your first time using this mod and you're nicked, or you've changed your nick, you will have to set your nick with §n/setnick§r§3.");
        public static ChatComponentText pleaseChangeNick = new ChatComponentText("§6§l[BountyHunting] §ePlease set your nick with /setnick or in the config.");
        public static ChatComponentText gamemodeCreative = new ChatComponentText(logo + " §eSet gamemode to creative!");
        public static ChatComponentText insufficientEvidence = new ChatComponentText(logo + " §eSubmitting an Atlas verdict for \"Insufficient Evidence\"...");
        public static ChatComponentText evidenceWithoutDoubt = new ChatComponentText(logo + " §eSubmitting an Atlas verdict for \"Evidence Without Doubt\"...");
        public static ChatComponentText listPingInSingleplayer = new ChatComponentText(logo + " §cThis method does not work in singleplayer!");
        public static ChatComponentText noCookieBuff = new ChatComponentText(logo + " §r§cYou don't have the Cookie Buff!");
        public static ChatComponentText noItemFound = new ChatComponentText(logo + " §cNo item in bazaar with this name!");
        public static ChatComponentText clearedDisplayText = new ChatComponentText(logo + " §eCleared display text!");
        public static ChatComponentText alreadyCreative = new ChatComponentText(logo + " §cYou are already in creative mode!");
        public static ChatComponentText limboCheckFailed = new ChatComponentText(logo + " §cLimbo check failed, try again in a bit or rejoin!");
        public static ChatComponentText pingIs0 = new ChatComponentText(logo + " §cPing is 0! This might have occured if you used Direct Connect or the favorite server button.");
        public static ChatComponentText enterValidText = new ChatComponentText(logo + " §cYou must enter valid text!");
        public static ChatComponentText enterValidTitle = new ChatComponentText(logo + " §cYou must enter a valid title!");
        public static ChatComponentText enterValidNick = new ChatComponentText(logo + " §cYou must enter a valid nick!");
        public static ChatComponentText couldntGetMessage = new ChatComponentText(logo + " §cCouldn't get mod message!");
        public static ChatComponentText messageFromYedel = new ChatComponentText(logo + " §eMessage from Yedel:");
        public static ChatComponentText notOnHypixel = new ChatComponentText(logo + " §cYou must be on Hypixel to use this!");
        public static ChatComponentText hypixelRateLimited = new ChatComponentText(logo + " §cYou were rate limited while using this method!");
    }

    public static final Pattern[] skywarsKillPatterns = {
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was killed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was thrown into the void by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was thrown off a cliff by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was shot by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) got rekt by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) took the L to (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) got dabbed on by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) got bamboozled by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was trampled by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was back kicked into the void by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was headbutted off a cliff by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was impaled from a distance by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was struck down by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was turned to dust by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was turned to ash by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was melted by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was filled full of lead by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) met their end by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) lost a drinking contest with (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) lost the draw to (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was given the cold shoulder by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was out of the league of (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*)'s heart was broken by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was struck with Cupid's arrow by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) be sent to Davy Jones' locker by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) be cannonballed to death by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) be voodooed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was turned into space dust by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was sent into orbit by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was retrograded by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was hit by an asteroid from (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was deleted by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was ALT\\+F4'd by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was crashed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was rm -rf by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) died in close combat to (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) fought to the edge with (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) stumbled off a ledge with help by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) fell to the great marksmanship of (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was glazed in BBQ sauce by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) slipped in BBQ sauce of the edge spilled by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was not spicy enough for (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was thrown chili powder at by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was exterminated by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was squashed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was tranquilized by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was mushed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was peeled by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) slipped on (?<killer>[a-zA-Z0-9_]*)'s banana peel off a cliff\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) got banana pistol'd by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was chewed up by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was squeaked off the edge by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was distracted by a rat draggging pizza from (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was squeaked from a distance by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was oinked up by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) slipped into void for (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was distracted by a piglet from (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) got attacked by a carrot from (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was buzzed to death by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was bzzz'd off the edge by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was stung by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was startled from a distance by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was socked by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was KO'd by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) took an uppercut from (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was sent into a daze by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was crusaded by the knight (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was jousted by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was capapulted by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was shot to the knee by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was bit by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) got WOOF'D by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was growled off an edge by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was thrown a frisbee by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was backstabbed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was pushed into the abyss by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was thrown into a ravine by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was brutally shot by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was locked outside during a snow storm by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was shoved down an icy slope by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was made into a snowman by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was hit with a snowball from (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was painted pretty by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was flipped off the edge by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was deviled by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was made sunny side up by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was whacked with a party balloon by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was popped into the void by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was launched like a firework by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was shot with a roman candle by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was wrapped up by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was tied into a bow by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) tripped over a present placed by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was glued up by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was crushed into moon dust by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was sent the wrong way by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was blasted to the moon by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was smothered in holiday cheer by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was banished into the ether by (?<killer>[a-zA-Z0-9_]*)'s holiday spirit\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was pushed by (?<killer>[a-zA-Z0-9_]*)'s holiday spirit\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was sniped by a missle of festivity by (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) became victim .+? of (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was bow kill .+? of (?<killer>[a-zA-Z0-9_]*)\\."),
        Pattern.compile("(?<killed>[a-zA-Z0-9_]*) was void victim .+? of (?<killer>[a-zA-Z0-9_]*)\\.")
    };
}
