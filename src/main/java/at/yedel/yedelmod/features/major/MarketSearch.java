package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.handlers.HypixelManager;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.wrappers.UPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.Objects;

import static at.yedel.yedelmod.launch.YedelModConstants.logo;


public class MarketSearch {
    private MarketSearch() {}
    private static final MarketSearch instance = new MarketSearch();

    public static MarketSearch getInstance() {
        return instance;
    }

    private boolean ahSearching = false;
    private boolean bzSearching = false;

    @SubscribeEvent
    public void onMarketSearchKeys(InputEvent.KeyInputEvent event) {
        if (YedelMod.getInstance().getAhSearchKeybind().isPressed() && YedelConfig.getInstance().ahSearch) {
            if (HypixelManager.getInstance().isInSkyblock()) {
                ItemStack heldItem = UPlayer.getPlayer().getHeldItem();
                if (heldItem != null) {
                    String itemName = heldItem.getDisplayName();
                    if (Objects.equals(itemName, "§aSkyBlock Menu §7(Click)")) return;
                    String unformattedItemName = TextUtils.removeFormatting(itemName);
                    ahSearching = true;
                    UChat.chat(logo + " &eSearching the auction house for " + itemName + "&e...");
                    UChat.chat("/ahs " + unformattedItemName);
                }
            }
        }
        else if (YedelMod.getInstance().getBzSearchKeybind().isPressed() && YedelConfig.getInstance().bzSearch) {
            if (HypixelManager.getInstance().isInSkyblock()) {
                ItemStack heldItem = UPlayer.getPlayer().getHeldItem();
                if (heldItem != null) {
                    String itemName = heldItem.getDisplayName();
                    if (Objects.equals(itemName, "§aSkyBlock Menu §7(Click)")) return;
                    String unformattedItemName = TextUtils.removeFormatting(itemName);
                    bzSearching = true;
                    UChat.chat(logo + " &eSearching the bazaar for " + itemName + "&e...");
                    UChat.say("/bz " + unformattedItemName);
                }
            }
        }
    }

    @SubscribeEvent
    public void onDeniedMessages(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (msg.startsWith("You need the Cookie Buff to use this")) {
            if (ahSearching || bzSearching) {
                event.setCanceled(true);
                UChat.chat(logo + " §r§cYou don't have the Cookie Buff!");
            }
        }
        else if (Objects.equals(msg, "Obtain a Booster Cookie from the community shop in the hub!")) {
            if (ahSearching || bzSearching) {
                event.setCanceled(true);
                ahSearching = false;
                bzSearching = false;
            }
        }
    }
}
