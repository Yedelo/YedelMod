package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.mixins.AbstractContainerScreenInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.events.ScreenOpenEvent;
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
    private boolean clickerEnabled = false;
    private int slotIndex;

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
        submitVerdict("Insufficient Evidence", 30);
    }

    public void submitEvidenceWithoutDoubtVerdict() {
        submitVerdict("Evidence Without Doubt", 32);
    }

    private void submitVerdict(String name, int inventorySlot) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().easyAtlasVerdicts) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (inAtlas && player != null) {
                Platform.compatibility().displayChatMessage(yedelogo + " §eSubmitting an Atlas verdict for \"" + name + "\"...");
                player.getInventory().setSelectedSlot(7);
                Multithreading.schedule(() -> {
                    Minecraft.getInstance().gameMode.useItem(Minecraft.getInstance().player, InteractionHand.MAIN_HAND);
                    slotIndex = inventorySlot;
                    clickerEnabled = true;
                    setupTimeout();
                }, 250, TimeUnit.MILLISECONDS);
            }
        }
    }

    @Subscribe
    public void clickAtlasVerdict(ScreenOpenEvent event) {
        if (clickerEnabled) {
            if (event.getScreen() instanceof AbstractContainerScreen containerScreen) {
                if (!Objects.equals(containerScreen.getTitle().getString(), "Atlas Verdict - Hacking")) {
                    return;
                }
                AbstractContainerMenu containerMenu = containerScreen.getMenu();
                if (containerMenu.slots.size() <= slotIndex) {
                    return;
                }
                Slot slot = containerMenu.slots.get(slotIndex);
                //@TODO ineffective
                ((AbstractContainerScreenInvoker) containerScreen).yedelmod$slotClicked(slot, slot.index, 0, ContainerInput.PICKUP);
                clickerEnabled = false;
            }
        }
    }

    private void c(String string) {
        Platform.compatibility().displayChatMessage(string);
    }

    public void setupTimeout() {
        Multithreading.schedule(() -> clickerEnabled = false, 1000, TimeUnit.MILLISECONDS);
    }
}
