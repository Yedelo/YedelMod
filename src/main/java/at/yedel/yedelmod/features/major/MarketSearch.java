package at.yedel.yedelmod.features.major;



import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.wrappers.UPlayer;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import net.hypixel.data.type.GameType;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.item.ItemStack;

import java.util.Objects;

import static at.yedel.yedelmod.launch.YedelModConstants.yedelogo;


public class MarketSearch {
    private static final MarketSearch INSTANCE = new MarketSearch();

    public static MarketSearch getInstance() {
        return INSTANCE;
    }

    private MarketSearch() {
        HypixelModAPI.getInstance().registerHandler(ClientboundLocationPacket.class, this::handleLocationPacket);
    }

    private boolean inSkyblock;

    private void handleLocationPacket(ClientboundLocationPacket packet) {
        inSkyblock = packet.getServerType().isPresent() && packet.getServerType().get() == GameType.SKYBLOCK;
    }

    private boolean ahSearching = false;
    private boolean bzSearching = false;

    public void ahSearch() {
        if (inSkyblock) {
            ItemStack heldItem = UPlayer.getPlayer().getHeldItem();
            if (heldItem != null) {
                String itemName = heldItem.getDisplayName();
                if (Objects.equals(itemName, "§aSkyBlock Menu §7(Click)")) return;
                String unformattedItemName = UTextComponent.Companion.stripFormatting(itemName);
                ahSearching = true;
                UChat.chat(yedelogo + " &eSearching the auction house for " + itemName + "&e...");
                UChat.say("/ahs " + unformattedItemName);
            }
        }
    }

    public void bzSearch() {
        if (inSkyblock) {
            ItemStack heldItem = UPlayer.getPlayer().getHeldItem();
            if (heldItem != null) {
                String itemName = heldItem.getDisplayName();
                if (Objects.equals(itemName, "§aSkyBlock Menu §7(Click)")) return;
                String unformattedItemName = UTextComponent.Companion.stripFormatting(itemName);
                bzSearching = true;
                UChat.chat(yedelogo + " &eSearching the bazaar for " + itemName + "&e...");
                UChat.say("/bz " + unformattedItemName);
            }
        }
    }

    @Subscribe
    public void onPOORPEOPLEmessages(ChatReceiveEvent event) {
        String msg = event.message.getUnformattedText();
        if (msg.startsWith("You need the Cookie Buff to use this")) {
            if (ahSearching || bzSearching) {
                event.isCancelled = true;
                UChat.chat(yedelogo + " §r§cYou don't have the Cookie Buff!");
            }
        }
        else if (Objects.equals(msg, "Obtain a Booster Cookie from the community shop in the hub!")) {
            if (ahSearching || bzSearching) {
                event.isCancelled = true;
                ahSearching = false;
                bzSearching = false;
            }
        }
    }
}
