package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;




import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.events.PacketEvent;
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
//? if v0 {
/*import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.libs.universal.UScreen;
import cc.polyfrost.oneconfig.libs.universal.wrappers.UPlayer;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
*///?} else {
import org.polyfrost.oneconfig.api.platform.v1.Platform;
import org.polyfrost.oneconfig.api.platform.v1.ScreenPlatform;
//?}
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import net.minecraft.client.Minecraft;
//? if legacy {
/*import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import at.yedel.yedelmod.mixins.legacy.InvokerMinecraft;
*///?} else {

import at.yedel.yedelmod.mixins.modern.AbstractContainerScreenInvoker;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
//?}
//? if forge {
/*import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

*///?}

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static at.yedel.yedelmod.launch.YedelModConstants.yedelogo;



public class EasyAtlasVerdicts {
    private static final EasyAtlasVerdicts INSTANCE = new EasyAtlasVerdicts();

    public static EasyAtlasVerdicts getInstance() {
        return INSTANCE;
    }

    private boolean inAtlas;
    private String verdict;

    private EasyAtlasVerdicts() {}

    @Subscribe
    public void onSuspectTeleport(ChatEvent.Receive event) {
        String text = event.getFullyUnformattedMessage();
        if (Objects.equals(text, "Teleporting you to suspect")) {
            inAtlas = true;
        }
        else if (Objects.equals(text, "Atlas verdict submitted! Thank you :)")) {
            inAtlas = false;
        }
    }

    //? if forge {
    //@SubscribeEvent
    //?} else
    @Subscribe
    public void onLeaveAtlasPartTwo(WorldEvent.Unload event) {
        inAtlas = false;
    }

    public void submitInsufficientEvidenceVerdict() {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().easyAtlasVerdicts) {
            submitVerdict("Insufficient Evidence");
        }
    }

    public void submitEvidentWithoutDoubtVerdict() {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().easyAtlasVerdicts) {
            submitVerdict("Evident Without Doubt");
        }
    }
    private void submitVerdict(String name) {
        //~ if modern 'EntityPlayerSP player = Minecraft.getMinecraft().thePlayer' -> 'LocalPlayer player = Minecraft.getInstance().player'
        LocalPlayer player = Minecraft.getInstance().player;
            if (inAtlas && player != null) {
                Platform.compatibility().displayChatMessage(yedelogo + " §eSubmitting an Atlas verdict for \"" + name + "\"...");
                //~ if modern 'player.inventory.currentItem = 7' -> 'player.getInventory().setSelectedSlot(7)'
                player.getInventory().setSelectedSlot(7);
                Multithreading.schedule(() -> {
                    //~ if v1 'Minecraft.getMinecraft().currentScreen' -> 'Platform.screen().current()'
                    if (Platform.screen().current() == null) {
                        verdict = name;
                        //? if legacy
                        //((InvokerMinecraft) UMinecraft.getMinecraft()).yedelmod$rightClickMouse();
                        //? else
                        Minecraft.getInstance().gameMode.useItem(Minecraft.getInstance().player, InteractionHand.MAIN_HAND);
                    }
                }, 250, TimeUnit.MILLISECONDS);
            }
    }

    @Subscribe
    public void reallyClickAtlasVerdict(PacketEvent.Receive event) {
        //~ if modern 'S2FPacketSetSlot' -> 'ClientboundContainerSetSlotPacket'
        if (inAtlas && event.getPacket() instanceof ClientboundContainerSetSlotPacket) {
            //? if legacy {
            /*S2FPacketSetSlot packet = (S2FPacketSetSlot) event.getPacket();
            ItemStack item = packet.func_149174_e();
            if (item == null) return;
            String itemName = UTextComponent.Companion.stripFormatting(item.getDisplayName());
            *///?} else {
            ClientboundContainerSetSlotPacket packet = (ClientboundContainerSetSlotPacket) event.getPacket();
            ItemStack item = packet.getItem();
            if (item == null) return;
            Component itemNameComponent = item.getCustomName();
            if (itemNameComponent == null) return;
            String itemName = itemNameComponent.getString();
            //?}
            if (Objects.equals(itemName, verdict)) {
                Multithreading.schedule(() -> {
                    Minecraft.getInstance().schedule(() -> {
                        //? if legacy {
                        /*if (Minecraft.getMinecraft().currentScreen instanceof GuiContainer) {
                            int windowId = ((GuiContainer) UScreen.getCurrentScreen()).inventorySlots.windowId;
                            UMinecraft.getMinecraft().playerController.windowClick(windowId, packet.func_149173_d(), 0, 0, UPlayer.getPlayer());
                            verdict = "";
                        }
                        *///?} else {
                        if (Platform.screen().current() instanceof AbstractContainerScreen screen) {
                            // this is mad stupid
                            ((AbstractContainerScreenInvoker) screen).yedelmod$slotClicked(screen.getMenu().getSlot(packet.getSlot()), packet.getSlot(), 0, ContainerInput.PICKUP);
                            verdict = "";
                        }
                        //?}
                    });
                }, 250, TimeUnit.MILLISECONDS);
            }
        }
    }
}
