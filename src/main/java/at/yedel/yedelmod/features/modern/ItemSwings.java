package at.yedel.yedelmod.features.modern;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.PacketEvent;
import at.yedel.yedelmod.utils.SwingItemDuck;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class ItemSwings {
    private static final ItemSwings instance = new ItemSwings();

    public static ItemSwings getInstance() {
        return instance;
    }

    private final List<String> swingItems = new ArrayList<String>();

    private ItemSwings() {
        swingItems.addAll(Arrays.asList(
            "minecraft:egg",
            "minecraft:ender_eye",
            "minecraft:experience_bottle",
            "minecraft:snowball"
        ));
    }

    private void swing() {
        ((SwingItemDuck) minecraft.thePlayer).yedelmod$swingItemLocally();
    }

    @SubscribeEvent
    public void onUseSwingable(PlayerInteractEvent event) {
        if (!YedelConfig.getInstance().itemUseSwings) return;
        ItemStack itemStack = event.entityPlayer.getHeldItem();
        if (itemStack == null) return;
        Item item = itemStack.getItem();
        String registryName = item.getRegistryName();
        if (swingItems.contains(registryName)) {
            swing();
        }
        else if (Objects.equals(registryName, "minecraft:potion") && ItemPotion.isSplash(itemStack.getMetadata())) {
            swing();
        }
        else if (Objects.equals(registryName, "minecraft:ender_pearl") && !minecraft.playerController.isInCreativeMode()) {
            swing();
        }
        else if (item instanceof ItemArmor) {
            int slot = EntityLiving.getArmorPosition(itemStack) - 1;
            if (event.entityPlayer.getCurrentArmor(slot) == null) {
                swing();
            }
        }
    }

    @SubscribeEvent
    public void onDropPacket(PacketEvent.SendEvent event) {
        if (!YedelConfig.getInstance().dropSwings) return;
        if (event.getPacket() instanceof C07PacketPlayerDigging) {
            C07PacketPlayerDigging.Action action = ((C07PacketPlayerDigging) event.getPacket()).getStatus();
            if ((action == C07PacketPlayerDigging.Action.DROP_ALL_ITEMS || action == C07PacketPlayerDigging.Action.DROP_ITEM) && minecraft.thePlayer.getHeldItem() != null) {
                swing();
            }
        }
    }
}
