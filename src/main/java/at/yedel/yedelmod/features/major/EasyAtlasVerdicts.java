package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.PacketEvent;
import at.yedel.yedelmod.mixins.net.minecraft.client.InvokerMinecraft;
import at.yedel.yedelmod.utils.typeutils.NumberUtils;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.libs.universal.wrappers.UPlayer;
import cc.polyfrost.oneconfig.utils.Multithreading;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static at.yedel.yedelmod.launch.YedelModConstants.logo;


public class EasyAtlasVerdicts {
    private EasyAtlasVerdicts() {}
    private static final EasyAtlasVerdicts instance = new EasyAtlasVerdicts();

    public static EasyAtlasVerdicts getInstance() {
        return instance;
    }

    private boolean inAtlas;

    private boolean clickerEnabled = false;
    private int slot;

    @SubscribeEvent
    public void onSuspectTeleport(ClientChatReceivedEvent event) {
        String text = event.message.getUnformattedText();
        if (Objects.equals(text, "Teleporting you to suspect")) {
            inAtlas = true;
        }
        else if (Objects.equals(text, "Atlas verdict submitted! Thank you :)")) inAtlas = false;
    }

    @SubscribeEvent
    public void onAtlasKeys(KeyInputEvent event) {
        EntityPlayerSP player = UMinecraft.getPlayer();
        if (player == null) return;
        if (YedelMod.getInstance().getInsufficientKeybind().isPressed()) {
            if (!inAtlas || !YedelConfig.getInstance().easyAtlasVerdicts) return;
            UChat.chat(logo + " §eSubmitting an Atlas verdict for \"Insufficient Evidence\"...");
            player.inventory.currentItem = 7;
            Multithreading.schedule(() -> {
                ((InvokerMinecraft) UMinecraft.getMinecraft()).yedelmod$rightClickMouse();
                slot = 30;
                clickerEnabled = true;
                setupTimeout();
            }, (int) (NumberUtils.randomRange(158, 301)), TimeUnit.MILLISECONDS);
        }
        else if (YedelMod.getInstance().getSufficientKeybind().isPressed()) {
            if (!inAtlas || !YedelConfig.getInstance().easyAtlasVerdicts) return;
            UChat.chat(logo + " §eSubmitting an Atlas verdict for \"Evidence Without Doubt\"...");
            player.inventory.currentItem = 7;
            Multithreading.schedule(() -> {
                ((InvokerMinecraft) UMinecraft.getMinecraft()).yedelmod$rightClickMouse();
                slot = 32;
                clickerEnabled = true;
                setupTimeout();
            }, (int) (NumberUtils.randomRange(158, 301)), TimeUnit.MILLISECONDS);
        }
    }

    @SubscribeEvent
    public void onLeaveAtlas(PacketEvent.ReceiveEvent event) {
        if (event.isJoinGamePacket()) inAtlas = false;
    }

    @SubscribeEvent
    public void onLeaveAtlasPartTwo(WorldEvent.Unload event) {
        inAtlas = false;
    }

    @SubscribeEvent
    public void onOpenAtlas(GuiOpenEvent event) {
        if (!clickerEnabled) return;
        if (event.gui instanceof GuiContainer) {
            EntityPlayerSP player = UPlayer.getPlayer();
            if (player == null) return;
            Multithreading.schedule(() -> {
                UMinecraft.getMinecraft().playerController.windowClick(player.openContainer.windowId, slot, 0, 0, player);
            }, (int) NumberUtils.randomRange(300, 400), TimeUnit.MILLISECONDS);
            clickerEnabled = false;
        }
    }

    public void setupTimeout() { // In case anything goes wrong, this makes sure it doesn't randomly click the next inventory
        Multithreading.schedule(() -> clickerEnabled = false, 1500, TimeUnit.MILLISECONDS);
    }
}
