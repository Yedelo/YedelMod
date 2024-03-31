package at.yedel.yedelmod.features.modern;



import java.util.Arrays;
import java.util.List;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.ducks.entity.LocalSwinger;
import at.yedel.yedelmod.events.PacketEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class ItemSwings {
    private final List swingItems;

    public ItemSwings() {
        swingItems = Arrays.asList(
                "minecraft:egg",
                "minecraft:ender_eye",
                "minecraft:ender_pearl",
                "minecraft:experience_bottle",
                "minecraft:lava_bucket",
                "minecraft:snowball",
                "minecraft:water_bucket",
                "minecraft:potion"
        );
    }

    public void swing() {
        ((LocalSwinger) minecraft.thePlayer).yedelmod$swingItemLocally();
    }

    @SubscribeEvent
    public void onUseSwingable(PlayerInteractEvent event) {
        if (!YedelConfig.itemSwings) return;
        ItemStack stack = minecraft.thePlayer.getHeldItem();
        if (stack == null) return;
        Item item = stack.getItem();
        String registryName = item.getRegistryName();
        if (swingItems.contains(registryName)) {
            if (registryName.equals("minecraft:potion")) {
                if (!ItemPotion.isSplash(stack.getMetadata())) return;
            }
            else if (registryName.contains("bucket")) {
                MovingObjectPosition.MovingObjectType type = minecraft.objectMouseOver.typeOfHit;
                if (type == MovingObjectPosition.MovingObjectType.ENTITY || type == MovingObjectPosition.MovingObjectType.MISS)
                    return;
            }
            else if (registryName.equals("minecraft:ender_pearl")) {
                if (minecraft.playerController.isInCreativeMode()) return;
            }
            swing();
        }
    }

    @SubscribeEvent
    public void onDropItem(ItemTossEvent event) { // rework
        if (YedelConfig.dropSwings) swing();
    }

    @SubscribeEvent
    public void onDropPacket(PacketEvent.SendEvent event) {
        if (event.packet instanceof C07PacketPlayerDigging) {
            C07PacketPlayerDigging.Action action = ((C07PacketPlayerDigging) event.packet).getStatus();
            if (action == C07PacketPlayerDigging.Action.DROP_ALL_ITEMS || action == C07PacketPlayerDigging.Action.DROP_ITEM) {
                if (YedelConfig.dropSwings) swing();
            }
        }

    }
}
