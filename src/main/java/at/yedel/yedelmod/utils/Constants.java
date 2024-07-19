package at.yedel.yedelmod.utils;



import java.util.regex.Pattern;

import gg.essential.api.EssentialAPI;
import gg.essential.api.gui.Notifications;
import net.minecraft.util.ChatComponentText;



public class Constants {
    public static Notifications notifications = EssentialAPI.getNotifications();
    public static final String logo = "§8§l- §9§lYedel§7§lMod §8§l-";

    public static class messages { // To avoid making the same ChatComponentTexts every time the message is displayed
        public static ChatComponentText unknownSubcommandMessage = new ChatComponentText(logo + " §eUnknown subcommand, refer to the command index (/yedel).");
        public static ChatComponentText YedelUtilsMessage = new ChatComponentText(logo + " §cYedelUtils detected, it will likely completely break this mod. Do §7/ct delete YedelUtils §cto remove it.");
        public static ChatComponentText welcomeMessage = new ChatComponentText(logo + " §7Welcome to §9§lYedel§7§lMod! Use §9/yedel §7for more information.");
        public static ChatComponentText latestVersion = new ChatComponentText(logo + " §cYou are already on the latest version!");
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
    }

    public static final Pattern[] killMessages = {
        Pattern.compile("(?<killed>.+) was killed by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was thrown into the void by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was thrown off a cliff by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was shot by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) got rekt by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) took the L to (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) got dabbed on by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) got bamboozled by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was trampled by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was back kicked into the void by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was headbutted off a cliff by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was impaled from a distance by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was struck down by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was turned to dust by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was turned to ash by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was melted by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was filled full of lead by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) met their end by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) lost a drinking contest with (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) lost the draw to (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was given the cold shoulder by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was out of the league of (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+)'s heart was broken by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was struck with Cupid's arrow by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) be sent to Davy Jones' locker by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) be cannonballed to death by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) be voodooed by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was turned into space dust by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was sent into orbit by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was retrograded by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was hit by an asteroid from (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was deleted by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was ALT\\+F4'd by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was crashed by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was rm -rf by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) died in close combat to (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) fought to the edge with (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) stumbled off a ledge with help by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) fell to the great marksmanship of (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was glazed in BBQ sauce by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) slipped in BBQ sauce of the edge spilled by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was not spicy enough for (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was thrown chili powder at by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was exterminated by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was squashed by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was tranquilized by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was mushed by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was peeled by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) slipped on (?<killer>.+)'s banana peel off a cliff\\."),
        Pattern.compile("(?<killed>.+) got banana pistol'd by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was chewed up by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was squeaked off the edge by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was distracted by a rat draggging pizza from (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was squeaked from a distance by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was oinked up by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) slipped into void for (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was distracted by a piglet from (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) got attacked by a carrot from (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was buzzed to death by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was bzzz'd off the edge by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was stung by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was startled from a distance by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was socked by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was KO'd by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) took an uppercut from (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was sent into a daze by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was crusaded by the knight (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was jousted by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was capapulted by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was shot to the knee by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was bit by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) got WOOF'D by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was growled off an edge by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was thrown a frisbee by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was backstabbed by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was pushed into the abyss by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was thrown into a ravine by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was brutally shot by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was locked outside during a snow storm by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was shoved down an icy slope by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was made into a snowman by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was hit with a snowball from (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was painted pretty by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was flipped off the edge by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was deviled by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was made sunny side up by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was whacked with a party balloon by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was popped into the void by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was launched like a firework by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was shot with a roman candle by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was wrapped up by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was tied into a bow by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) tripped over a present placed by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was glued up by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was crushed into moon dust by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was sent the wrong way by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was blasted to the moon by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was smothered in holiday cheer by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was banished into the ether by (?<killer>.+)'s holiday spirit\\."),
        Pattern.compile("(?<killed>.+) was pushed by (?<killer>.+)'s holiday spirit\\."),
        Pattern.compile("(?<killed>.+) was sniped by a missle of festivity by (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) became victim .+? of (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was bow kill .+? of (?<killer>.+)\\."),
        Pattern.compile("(?<killed>.+) was void victim .+? of (?<killer>.+)\\.")
    };
}
