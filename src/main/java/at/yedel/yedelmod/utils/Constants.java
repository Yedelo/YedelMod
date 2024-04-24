package at.yedel.yedelmod.utils;



import gg.essential.api.EssentialAPI;
import gg.essential.api.gui.Notifications;
import net.minecraft.util.ChatComponentText;

import static at.yedel.yedelmod.YedelMod.logo;



public class Constants {
    public static Notifications notifications = EssentialAPI.getNotifications();

    public static class Messages { // To avoid making the same ChatComponentTexts every time the message is displayed
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
        public static ChatComponentText enterValidNick = new ChatComponentText(logo + "§cYou must enter a valid nick!");
        public static ChatComponentText couldntGetMessage = new ChatComponentText(logo + " §cCouldn't get mod message!");
        public static ChatComponentText messageFromYedel = new ChatComponentText(logo + " §eMessage from Yedel:");
    }
}
