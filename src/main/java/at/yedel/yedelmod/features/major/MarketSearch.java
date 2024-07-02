package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.messages;
import at.yedel.yedelmod.utils.ScoreboardName;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;

;



public class MarketSearch {
    private static final MarketSearch instance = new MarketSearch();

    public static MarketSearch getInstance() {
        return instance;
    }

    private boolean ahSearching = false;
    private boolean bzSearching = false;
    private boolean bzSearchingClose = false;

    @SubscribeEvent
    public void onMarketSearchKeys(InputEvent.KeyInputEvent event) {
        if (YedelMod.getInstance().getAhSearch().isPressed()) {
            if (ScoreboardName.getInstance().getInSkyblock()) {
                ItemStack heldItem = minecraft.thePlayer.getHeldItem();
                if (heldItem != null) {
                    String itemName = heldItem.getDisplayName();
                    if (itemName.equals("§aSkyBlock Menu §7(Click)")) return;
                    ahSearching = true;
                    Chat.logoDisplay("&eSearching the auction house for " + itemName + "&e...");
                    Chat.command("ahs " + TextUtils.removeAmpersand(itemName));
                }
            }
        }
        else if (YedelMod.getInstance().getBzSearch().isPressed()) {
            if (ScoreboardName.getInstance().getInSkyblock()) {
                ItemStack heldItem = minecraft.thePlayer.getHeldItem();
                if (heldItem != null) {
                    String itemName = TextUtils.removeFormatting(heldItem.getDisplayName());
                    if (itemName.equals("§aSkyBlock Menu §7(Click)")) return;
                    bzSearching = true;
                    Chat.logoDisplay("&eSearching the bazaar for " + itemName + "&e...");
                    Chat.command("bz " + itemName);
                    bzSearchingClose = true;
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
                Chat.display(messages.noCookieBuff);
            }
        }
        else if (msg.equals("Obtain a Booster Cookie from the community shop in the hub!")) {
            if (ahSearching || bzSearching) {
                event.setCanceled(true);
                ahSearching = false;
                bzSearching = false;
            }
        }
    }

    @SubscribeEvent
    public void onBazaarGUIOpen(GuiOpenEvent event) {
        if (!bzSearchingClose) return;
        Gui gui = event.gui;
        if (gui instanceof GuiInventory) {
            for (Slot slot: ((GuiInventory) gui).inventorySlots.inventorySlots) {
                if (slot.slotNumber != 18) return;
                String itemName = slot.getStack().getDisplayName();
                if (itemName.contains("No Product Found")) {
                    bzSearchingClose = false;
                    minecraft.thePlayer.closeScreen();
                    Chat.display(messages.noItemFound);
                }
            }
        }
    }
}
