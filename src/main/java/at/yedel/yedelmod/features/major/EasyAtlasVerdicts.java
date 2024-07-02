package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.JoinGamePacketEvent;
import at.yedel.yedelmod.mixins.net.minecraft.client.InvokerMinecraft;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.messages;
import at.yedel.yedelmod.utils.InventoryClicker;
import at.yedel.yedelmod.utils.ThreadManager;
import at.yedel.yedelmod.utils.typeutils.NumberUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class EasyAtlasVerdicts {
    private static final EasyAtlasVerdicts instance = new EasyAtlasVerdicts();

    public static EasyAtlasVerdicts getInstance() {
        return instance;
    }

    private boolean inAtlas;

    @SubscribeEvent
    public void onSuspectTeleport(ClientChatReceivedEvent event) {
        String text = event.message.getUnformattedText();
        if (text.equals("Teleporting you to suspect")) {
            inAtlas = true;
        }
        else if (text.equals("Atlas verdict submitted! Thank you :)")) inAtlas = false;
    }

    @SubscribeEvent
    public void onAtlasKeys(KeyInputEvent event) {
        EntityPlayerSP player = minecraft.thePlayer;
        if (YedelMod.getInstance().getInsufficient().isPressed()) {
            if (!inAtlas || !YedelConfig.getInstance().autoAtlas) return;
            Chat.display(messages.insufficientEvidence);
            player.inventory.currentItem = 7;
            ThreadManager.scheduleOnce(() -> {
                ((InvokerMinecraft) minecraft).yedelmod$rightClickMouse();
                InventoryClicker.getInstance().setSlot(30);
                MinecraftForge.EVENT_BUS.register(InventoryClicker.getInstance());
                InventoryClicker.getInstance().setupTimeout();
            }, (int) (NumberUtils.randomRange(158, 301)));
        }
        else if (YedelMod.getInstance().getSufficient().isPressed()) {
            if (!inAtlas || !YedelConfig.getInstance().autoAtlas) return;
            Chat.display(messages.evidenceWithoutDoubt);
            player.inventory.currentItem = 7;
            ThreadManager.scheduleOnce(() -> {
                ((InvokerMinecraft) minecraft).yedelmod$rightClickMouse();
                InventoryClicker.getInstance().setSlot(32);
                MinecraftForge.EVENT_BUS.register(InventoryClicker.getInstance());
                InventoryClicker.getInstance().setupTimeout();
            }, (int) (NumberUtils.randomRange(158, 301)));
        }
    }

    @SubscribeEvent
    public void onLeaveAtlas(JoinGamePacketEvent event) {
        inAtlas = false;
    }

    @SubscribeEvent
    public void onLeaveAtlasPartTwo(WorldEvent.Unload event) {
        inAtlas = false;
    }
}
