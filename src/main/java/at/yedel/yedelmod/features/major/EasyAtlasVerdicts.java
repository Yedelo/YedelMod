package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.mixins.net.minecraft.client.InvokerMinecraft;
import at.yedel.yedelmod.utils.typeutils.NumberUtils;
import dev.deftu.omnicore.client.OmniChat;
import dev.deftu.omnicore.client.OmniClient;
import dev.deftu.omnicore.client.OmniClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.events.PacketEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.utils.v1.Multithreading;

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

    @Subscribe
    public void onSuspectTeleport(ChatEvent.Receive event) {
        String text = event.getFullyUnformattedMessage();
        if (Objects.equals(text, "Teleporting you to suspect")) {
            inAtlas = true;
        }
        else if (Objects.equals(text, "Atlas verdict submitted! Thank you :)")) inAtlas = false;
    }

    public void submitInsufficientEvidenceVerdict() {
        EntityPlayerSP player = OmniClientPlayer.getInstance();
        if (inAtlas && player != null) {
            OmniChat.displayClientMessage(logo + " §eSubmitting an Atlas verdict for \"Insufficient Evidence\"...");
            player.inventory.currentItem = 7;
            Multithreading.schedule(() -> {
                ((InvokerMinecraft) OmniClient.getInstance()).yedelmod$rightClickMouse();
                    slot = 30;
                    clickerEnabled = true;
                    setupTimeout();
                }, (int) (NumberUtils.randomRange(158, 301)), TimeUnit.MILLISECONDS
            );
        }
    }

    public void submitEvidenceWithoutDoubtVerdict() {
        EntityPlayerSP player = OmniClientPlayer.getInstance();
        if (inAtlas && player != null) {
            OmniChat.displayClientMessage(logo + " §eSubmitting an Atlas verdict for \"Evidence Without Doubt\"...");
            player.inventory.currentItem = 7;
            Multithreading.schedule(() -> {
                ((InvokerMinecraft) OmniClient.getInstance()).yedelmod$rightClickMouse();
                    slot = 32;
                    clickerEnabled = true;
                    setupTimeout();
                }, (int) (NumberUtils.randomRange(158, 301)), TimeUnit.MILLISECONDS
            );
        }
    }

    @Subscribe
    public void onLeaveAtlas(PacketEvent.Receive event) {
        if (event.getPacket() instanceof S01PacketJoinGame) inAtlas = false;
    }

    @SubscribeEvent
    public void onLeaveAtlasPartTwo(WorldEvent.Unload event) {
        inAtlas = false;
    }

    @SubscribeEvent
    public void clickAtlasVerdict(GuiOpenEvent event) {
        if (!clickerEnabled) return;
        if (event.gui instanceof GuiContainer) {
            EntityPlayerSP player = OmniClientPlayer.getInstance();
            if (player == null) return;
            Multithreading.schedule(() -> {
                    OmniClient.getInstance().playerController.windowClick(player.openContainer.windowId, slot, 0, 0, player);
                }, (int) NumberUtils.randomRange(300, 400), TimeUnit.MILLISECONDS
            );
            clickerEnabled = false;
        }
    }

    public void setupTimeout() { // In case anything goes wrong, this makes sure it doesn't randomly click the next inventory
        Multithreading.schedule(() -> clickerEnabled = false, 1500, TimeUnit.MILLISECONDS);
    }
}
