package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.mixins.InvokerMinecraft;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.libs.universal.UScreen;
import cc.polyfrost.oneconfig.libs.universal.wrappers.UPlayer;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraftforge.event.world.WorldEvent;

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
    public void onSuspectTeleport(ChatReceiveEvent event) {
        String text = event.message.getUnformattedText();
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

    private void submitVerdict(String name) {
        EntityPlayerSP player = UPlayer.getPlayer();
        if (inAtlas && player != null) {
            UChat.chat(yedelogo + " §eSubmitting an Atlas verdict for \"" + name + "\"...");
            player.inventory.currentItem = 7;
            Multithreading.schedule(() -> {
                if (UScreen.getCurrentScreen() == null) {
                    verdict = name;
                    ((InvokerMinecraft) UMinecraft.getMinecraft()).yedelmod$rightClickMouse();
                }
            }, 250, TimeUnit.MILLISECONDS);
        }
    }

    @Subscribe
    public void reallyClickAtlasVerdict(ReceivePacketEvent event) {
        if (inAtlas && event.packet instanceof S2FPacketSetSlot) {
            S2FPacketSetSlot packet = (S2FPacketSetSlot) event.packet;
            ItemStack item = packet.func_149174_e();
            if (item == null) return;
            String itemName = UTextComponent.Companion.stripFormatting(item.getDisplayName());
            if (Objects.equals(itemName, verdict)) {
                Multithreading.schedule(() -> {
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        if (UScreen.getCurrentScreen() instanceof GuiContainer) {
                            int windowId = ((GuiContainer) UScreen.getCurrentScreen()).inventorySlots.windowId;
                            UMinecraft.getMinecraft().playerController.windowClick(windowId, packet.func_149173_d(), 0, 0, UPlayer.getPlayer());
                            verdict = "";
                        }
                    });
                }, 250, TimeUnit.MILLISECONDS);
            }
        }
    }
}
