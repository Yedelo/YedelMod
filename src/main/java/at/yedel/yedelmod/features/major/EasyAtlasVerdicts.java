package at.yedel.yedelmod.features.major;



import java.util.Objects;
import java.util.concurrent.TimeUnit;

import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.InventoryClicker;
import at.yedel.yedelmod.utils.typeutils.NumberUtils;
import gg.essential.api.utils.Multithreading;
import gg.essential.universal.UChat;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import static at.yedel.yedelmod.YedelMod.logo;
import static at.yedel.yedelmod.YedelMod.minecraft;



public class EasyAtlasVerdicts {
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
            UChat.chat(logo + " &eSubmitting an Atlas verdict for \"Insufficient Evidence\"...");
            player.inventory.currentItem = 7;
            Multithreading.schedule(() -> {
                KeyBinding.onTick(minecraft.gameSettings.keyBindUseItem.getKeyCode()); // click
                InventoryClicker.instance.slot = 30;
                MinecraftForge.EVENT_BUS.register(InventoryClicker.instance);
                InventoryClicker.instance.setupTimeout();
            }, (long) (NumberUtils.randomRange(158, 301)), TimeUnit.MILLISECONDS);
        }
        else if (YedelMod.sufficient.isPressed()) {
            if (!inAtlas || !YedelConfig.autoAtlas) return;
            UChat.chat(logo + " &eSubmitting an Atlas verdict for \"Evidence Without Doubt\"...");
            player.inventory.currentItem = 7;
            Multithreading.schedule(() -> {
                KeyBinding.onTick(minecraft.gameSettings.keyBindUseItem.getKeyCode());
                InventoryClicker.instance.slot = 32;
                MinecraftForge.EVENT_BUS.register(InventoryClicker.instance);
                InventoryClicker.instance.setupTimeout();
            }, (long) (NumberUtils.randomRange(158, 301)), TimeUnit.MILLISECONDS);
        }
    }

    @SubscribeEvent
    public void onLeaveAtlas(WorldEvent.Load event) {
        inAtlas = false;
    }

    @SubscribeEvent
    public void onLeaveAtlasPartTwo(WorldEvent.Unload event) {
        inAtlas = false;
    }
}
