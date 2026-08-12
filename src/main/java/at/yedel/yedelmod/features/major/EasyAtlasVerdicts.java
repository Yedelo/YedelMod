package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.mixins.AbstractContainerScreenInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.events.PacketEvent;
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.platform.v1.Platform;
import org.polyfrost.oneconfig.utils.v1.Multithreading;

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
    //~ if >=26.2 '.screen' -> '.gui.screen()' {
    private void submitVerdict(String name) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (inAtlas && player != null) {
                Platform.compatibility().displayChatMessage(yedelogo + " §eSubmitting an Atlas verdict for \"" + name + "\"...");
                player.getInventory().setSelectedSlot(7);
                Multithreading.schedule(() -> {
                    if (Minecraft.getInstance().screen == null) {
                        verdict = name;
                        Minecraft.getInstance().gameMode.useItem(Minecraft.getInstance().player, InteractionHand.MAIN_HAND);
                    }
                }, 250, TimeUnit.MILLISECONDS);
            }
    }

    @Subscribe
    public void reallyClickAtlasVerdict(PacketEvent.Receive event) {
        if (inAtlas && event.getPacket() instanceof ClientboundContainerSetSlotPacket packet) {
            ItemStack item = packet.getItem();
            if (item == null) return;
            Component itemNameComponent = item.getCustomName();
            if (itemNameComponent == null) return;
            String itemName = itemNameComponent.getString();
            if (Objects.equals(itemName, verdict)) {
                Multithreading.schedule(() -> {
                    Minecraft.getInstance().schedule(() -> {
                        if (Minecraft.getInstance().screen instanceof AbstractContainerScreen screen) {
                            // this is mad stupid
                            ((AbstractContainerScreenInvoker) screen).yedelmod$slotClicked(screen.getMenu().getSlot(packet.getSlot()), packet.getSlot(), 0, ContainerInput.PICKUP);
                            verdict = "";
                        }
                    });
                }, 250, TimeUnit.MILLISECONDS);
            }
        }
    }
    //~}
}
