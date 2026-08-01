package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.NumberUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
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
    private int slot;

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
                    slot = inventorySlot;
                    clickerEnabled = true;
                    setupTimeout();
                }, (int) NumberUtils.randomRange(158, 301), TimeUnit.MILLISECONDS);
            }
        }
    }

    @Subscribe
    public void onLeaveAtlasPartTwo(WorldEvent.Unload event) {
        inAtlas = false;
    }

    @Subscribe
    public void clickAtlasVerdict(ScreenOpenEvent event) {
        //@TODO click
        //        if (clickerEnabled) {
        //            if (event.getScreen() instanceof ChestMenu container) {
        //                container.clicked();
        //                Multithreading.schedule(() -> {
        //                        UMinecraft.getMinecraft().playerController.windowClick(player.openContainer.windowId, slot, 0, 0, player);
        //                        Minecraft.getMinecraft()
        //                    }, (int) NumberUtils.randomRange(300, 400), TimeUnit.MILLISECONDS
        //                );
        //                clickerEnabled = false;
        //            }
        //        }
    }

    public void setupTimeout() { // In case anything goes wrong, this makes sure it doesn't randomly click the next inventory
        Multithreading.schedule(() -> clickerEnabled = false, 1500, TimeUnit.MILLISECONDS);
    }
}
