package at.yedel.yedelmod.features.modern;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.SwingItemDuck;
import cc.polyfrost.oneconfig.events.event.SendPacketEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.libs.universal.wrappers.UPlayer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//#if MC == 1.12.2
//$$import net.minecraft.util.EnumHand;
//#endif

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

    //#if MC == 1.12.2
    //$$EnumHand swingHand;
    //#endif

    //#if MC == 1.8.9
    private void swing() {
        ((SwingItemDuck) UPlayer.getPlayer()).yedelmod$swingHandLocally();
    }
    //#else
    //$$private void swing() {
    //$$      ((SwingItemDuck) UPlayer.getPlayer()).yedelmod$swingHandLocally(swingHand);
    //$$}
    //#endif

    @SubscribeEvent
    public void swingOnSwingableUse(PlayerInteractEvent event) {
        if (!YedelConfig.getInstance().itemUseSwings) return;
        ItemStack itemStack;
        //#if MC == 1.8.9
        itemStack = event.entityPlayer.getHeldItem();
        //#else
        //$$swingHand = event.getHand();
        //$$itemStack = event.entityPlayer.getHeldItem(swingHand);
        //#endif

        if (itemStack == null) return;
        Item item = itemStack.getItem();
        String registryName = item.getRegistryName();
        if (swingItems.contains(registryName)) {
            swing();
        }
        else if (Objects.equals(registryName, "minecraft:potion") && ItemPotion.isSplash(itemStack.getMetadata())) {
            swing();
        }
        else if (Objects.equals(registryName, "minecraft:ender_pearl") && !UMinecraft.getMinecraft().playerController.isInCreativeMode()) {
            swing();
        }
        else if (item instanceof ItemArmor) {
            int slot = EntityLiving.getArmorPosition(itemStack) - 1;
            if (event.entityPlayer.getCurrentArmor(slot) == null) {
                swing();
            }
        }
    }

    @Subscribe
    public void swingOnDrop(SendPacketEvent event) {
        if (!YedelConfig.getInstance().itemDropSwings) return;
        if (event.packet instanceof C07PacketPlayerDigging) {
            C07PacketPlayerDigging.Action action = ((C07PacketPlayerDigging) event.packet).getStatus();
            ItemStack heldItem;
            if ((action == C07PacketPlayerDigging.Action.DROP_ALL_ITEMS || action == C07PacketPlayerDigging.Action.DROP_ITEM) && heldItem != null) {
                swing();
            }
        }
    }
}
