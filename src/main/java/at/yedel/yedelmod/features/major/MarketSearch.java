package at.yedel.yedelmod.features.major;



import dev.deftu.omnicore.client.OmniChat;
import dev.deftu.omnicore.client.OmniClientPlayer;
import dev.deftu.textile.minecraft.MCTextFormat;
import net.minecraft.item.ItemStack;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

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

    public void ahSearch() {
        if (HypixelManager.getInstance().isInSkyblock()) {
            ItemStack heldItem = OmniClientPlayer.getInstance().getHeldItem();
            if (heldItem != null) {
                String itemName = heldItem.getDisplayName();
                if (Objects.equals(itemName, "§aSkyBlock Menu §7(Click)")) return;
                String unformattedItemName = MCTextFormat.strip(itemName);
                ahSearching = true;
                OmniChat.displayClientMessage(logo + " §eSearching the auction house for " + itemName + "§e...");
                OmniChat.sendPlayerMessage("/ahs " + unformattedItemName);
            }
        }
    }

    public void bzSearch() {
        if (HypixelManager.getInstance().isInSkyblock()) {
            ItemStack heldItem = OmniClientPlayer.getInstance().getHeldItem();
            if (heldItem != null) {
                String itemName = heldItem.getDisplayName();
                if (Objects.equals(itemName, "§aSkyBlock Menu §7(Click)")) return;
                String unformattedItemName = MCTextFormat.strip(itemName);
                bzSearching = true;
                OmniChat.displayClientMessage(logo + " §eSearching the bazaar for " + itemName + "§e...");
                OmniChat.sendPlayerMessage("/bz " + unformattedItemName);
            }
        }
    }

    @Subscribe
    public void onPOORPEOPLEmessages(ChatEvent.Receive event) {
        String msg = event.getFullyUnformattedMessage();
        if (msg.startsWith("You need the Cookie Buff to use this")) {
            if (ahSearching || bzSearching) {
                event.cancelled = true;
                OmniChat.displayClientMessage(logo + " §r§cYou don't have the Cookie Buff!");
            }
        }
        else if (Objects.equals(msg, "Obtain a Booster Cookie from the community shop in the hub!")) {
            if (ahSearching || bzSearching) {
                event.cancelled = true;
                ahSearching = false;
                bzSearching = false;
            }
        }
    }
}
