package at.yedel.yedelmod.features.major;



import java.util.Objects;

import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.JoinGamePacketEvent;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
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

import static at.yedel.yedelmod.YedelMod.minecraft;



public class EasyAtlasVerdicts {
    public static EasyAtlasVerdicts instance = new EasyAtlasVerdicts();
    private boolean inAtlas;

    @SubscribeEvent
    public void onSuspectTeleport(ClientChatReceivedEvent event) {
        String text = event.message.getUnformattedText();
        if (Objects.equals(text, "Teleporting you to suspect")) {
            inAtlas = true;
        }
        else if (Objects.equals(text, "Atlas verdict submitted! Thank you :)")) inAtlas = false;
    }

    @SubscribeEvent
    public void onAtlasKeys(InputEvent.KeyInputEvent event) {
        EntityPlayerSP player = minecraft.thePlayer;
        if (YedelMod.insufficient.isPressed()) {
            if (!inAtlas || !YedelConfig.autoAtlas) return;
            Chat.display(Messages.insufficientEvidence);
            player.inventory.currentItem = 7;
            ThreadManager.scheduleOnce(() -> {
                KeyBinding.onTick(minecraft.gameSettings.keyBindUseItem.getKeyCode()); // click
                InventoryClicker.instance.slot = 30;
                MinecraftForge.EVENT_BUS.register(InventoryClicker.instance);
                InventoryClicker.instance.setupTimeout();
            }, (int) (NumberUtils.randomRange(158, 301)));
        }
        else if (YedelMod.sufficient.isPressed()) {
            if (!inAtlas || !YedelConfig.autoAtlas) return;
            Chat.display(Messages.evidenceWithoutDoubt);
            player.inventory.currentItem = 7;
            ThreadManager.scheduleOnce(() -> {
                KeyBinding.onTick(minecraft.gameSettings.keyBindUseItem.getKeyCode());
                InventoryClicker.instance.slot = 32;
                MinecraftForge.EVENT_BUS.register(InventoryClicker.instance);
                InventoryClicker.instance.setupTimeout();
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
