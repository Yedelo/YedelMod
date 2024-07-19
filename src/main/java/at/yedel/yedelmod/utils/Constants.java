package at.yedel.yedelmod.utils;



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

    public static final String[] killMessages = {
            "(?<killed>.+) was killed by (?<killer>.+)\\.",
            "(?<killed>.+) was thrown into the void by (?<killer>.+)\\.",
            "(?<killed>.+) was thrown off a cliff by (?<killer>.+)\\.",
            "(?<killed>.+) was shot by (?<killer>.+)\\.",
            "(?<killed>.+) got rekt by (?<killer>.+)\\.",
            "(?<killed>.+) took the L to (?<killer>.+)\\.",
            "(?<killed>.+) got dabbed on by (?<killer>.+)\\.",
            "(?<killed>.+) got bamboozled by (?<killer>.+)\\.",
            "(?<killed>.+) was trampled by (?<killer>.+)\\.",
            "(?<killed>.+) was back kicked into the void by (?<killer>.+)\\.",
            "(?<killed>.+) was headbutted off a cliff by (?<killer>.+)\\.",
            "(?<killed>.+) was impaled from a distance by (?<killer>.+)\\.",
            "(?<killed>.+) was struck down by (?<killer>.+)\\.",
            "(?<killed>.+) was turned to dust by (?<killer>.+)\\.",
            "(?<killed>.+) was turned to ash by (?<killer>.+)\\.",
            "(?<killed>.+) was melted by (?<killer>.+)\\.",
            "(?<killed>.+) was filled full of lead by (?<killer>.+)\\.",
            "(?<killed>.+) met their end by (?<killer>.+)\\.",
            "(?<killed>.+) lost a drinking contest with (?<killer>.+)\\.",
            "(?<killed>.+) lost the draw to (?<killer>.+)\\.",
            "(?<killed>.+) was given the cold shoulder by (?<killer>.+)\\.",
            "(?<killed>.+) was out of the league of (?<killer>.+)\\.",
            "(?<killed>.+)'s heart was broken by (?<killer>.+)\\.",
            "(?<killed>.+) was struck with Cupid's arrow by (?<killer>.+)\\.",
            "(?<killed>.+) be sent to Davy Jones' locker by (?<killer>.+)\\.",
            "(?<killed>.+) be cannonballed to death by (?<killer>.+)\\.",
            "(?<killed>.+) be voodooed by (?<killer>.+)\\.",
            "(?<killed>.+) was turned into space dust by (?<killer>.+)\\.",
            "(?<killed>.+) was sent into orbit by (?<killer>.+)\\.",
            "(?<killed>.+) was retrograded by (?<killer>.+)\\.",
            "(?<killed>.+) was hit by an asteroid from (?<killer>.+)\\.",
            "(?<killed>.+) was deleted by (?<killer>.+)\\.",
            "(?<killed>.+) was ALT\\+F4'd by (?<killer>.+)\\.",
            "(?<killed>.+) was crashed by (?<killer>.+)\\.",
            "(?<killed>.+) was rm -rf by (?<killer>.+)\\.",
            "(?<killed>.+) died in close combat to (?<killer>.+)\\.",
            "(?<killed>.+) fought to the edge with (?<killer>.+)\\.",
            "(?<killed>.+) stumbled off a ledge with help by (?<killer>.+)\\.",
            "(?<killed>.+) fell to the great marksmanship of (?<killer>.+)\\.",
            "(?<killed>.+) was glazed in BBQ sauce by (?<killer>.+)\\.",
            "(?<killed>.+) slipped in BBQ sauce of the edge spilled by (?<killer>.+)\\.",
            "(?<killed>.+) was not spicy enough for (?<killer>.+)\\.",
            "(?<killed>.+) was thrown chili powder at by (?<killer>.+)\\.",
            "(?<killed>.+) was exterminated by (?<killer>.+)\\.",
            "(?<killed>.+) was squashed by (?<killer>.+)\\.",
            "(?<killed>.+) was tranquilized by (?<killer>.+)\\.",
            "(?<killed>.+) was mushed by (?<killer>.+)\\.",
            "(?<killed>.+) was peeled by (?<killer>.+)\\.",
            "(?<killed>.+) slipped on (?<killer>.+)'s banana peel off a cliff\\.",
            "(?<killed>.+) got banana pistol'd by (?<killer>.+)\\.",
            "(?<killed>.+) was chewed up by (?<killer>.+)\\.",
            "(?<killed>.+) was squeaked off the edge by (?<killer>.+)\\.",
            "(?<killed>.+) was distracted by a rat draggging pizza from (?<killer>.+)\\.",
            "(?<killed>.+) was squeaked from a distance by (?<killer>.+)\\.",
            "(?<killed>.+) was oinked up by (?<killer>.+)\\.",
            "(?<killed>.+) slipped into void for (?<killer>.+)\\.",
            "(?<killed>.+) was distracted by a piglet from (?<killer>.+)\\.",
            "(?<killed>.+) got attacked by a carrot from (?<killer>.+)\\.",
            "(?<killed>.+) was buzzed to death by (?<killer>.+)\\.",
            "(?<killed>.+) was bzzz'd off the edge by (?<killer>.+)\\.",
            "(?<killed>.+) was stung by (?<killer>.+)\\.",
            "(?<killed>.+) was startled from a distance by (?<killer>.+)\\.",
            "(?<killed>.+) was socked by (?<killer>.+)\\.",
            "(?<killed>.+) was KO'd by (?<killer>.+)\\.",
            "(?<killed>.+) took an uppercut from (?<killer>.+)\\.",
            "(?<killed>.+) was sent into a daze by (?<killer>.+)\\.",
            "(?<killed>.+) was crusaded by the knight (?<killer>.+)\\.",
            "(?<killed>.+) was jousted by (?<killer>.+)\\.",
            "(?<killed>.+) was capapulted by (?<killer>.+)\\.",
            "(?<killed>.+) was shot to the knee by (?<killer>.+)\\.",
            "(?<killed>.+) was bit by (?<killer>.+)\\.",
            "(?<killed>.+) got WOOF'D by (?<killer>.+)\\.",
            "(?<killed>.+) was growled off an edge by (?<killer>.+)\\.",
            "(?<killed>.+) was thrown a frisbee by (?<killer>.+)\\.",
            "(?<killed>.+) was backstabbed by (?<killer>.+)\\.",
            "(?<killed>.+) was pushed into the abyss by (?<killer>.+)\\.",
            "(?<killed>.+) was thrown into a ravine by (?<killer>.+)\\.",
            "(?<killed>.+) was brutally shot by (?<killer>.+)\\.",
            "(?<killed>.+) was locked outside during a snow storm by (?<killer>.+)\\.",
            "(?<killed>.+) was shoved down an icy slope by (?<killer>.+)\\.",
            "(?<killed>.+) was made into a snowman by (?<killer>.+)\\.",
            "(?<killed>.+) was hit with a snowball from (?<killer>.+)\\.",
            "(?<killed>.+) was painted pretty by (?<killer>.+)\\.",
            "(?<killed>.+) was flipped off the edge by (?<killer>.+)\\.",
            "(?<killed>.+) was deviled by (?<killer>.+)\\.",
            "(?<killed>.+) was made sunny side up by (?<killer>.+)\\.",
            "(?<killed>.+) was whacked with a party balloon by (?<killer>.+)\\.",
            "(?<killed>.+) was popped into the void by (?<killer>.+)\\.",
            "(?<killed>.+) was launched like a firework by (?<killer>.+)\\.",
            "(?<killed>.+) was shot with a roman candle by (?<killer>.+)\\.",
            "(?<killed>.+) was wrapped up by (?<killer>.+)\\.",
            "(?<killed>.+) was tied into a bow by (?<killer>.+)\\.",
            "(?<killed>.+) tripped over a present placed by (?<killer>.+)\\.",
            "(?<killed>.+) was glued up by (?<killer>.+)\\.",
            "(?<killed>.+) was crushed into moon dust by (?<killer>.+)\\.",
            "(?<killed>.+) was sent the wrong way by (?<killer>.+)\\.",
            "(?<killed>.+) was blasted to the moon by (?<killer>.+)\\.",
            "(?<killed>.+) was smothered in holiday cheer by (?<killer>.+)\\.",
            "(?<killed>.+) was banished into the ether by (?<killer>.+)'s holiday spirit\\.",
            "(?<killed>.+) was pushed by (?<killer>.+)'s holiday spirit\\.",
            "(?<killed>.+) was sniped by a missle of festivity by (?<killer>.+)\\.",
            "(?<killed>.+) became victim .+? of (?<killer>.+)\\.",
            "(?<killed>.+) was bow kill .+? of (?<killer>.+)\\.",
            "(?<killed>.+) was void victim .+? of (?<killer>.+)\\."
    };
}
