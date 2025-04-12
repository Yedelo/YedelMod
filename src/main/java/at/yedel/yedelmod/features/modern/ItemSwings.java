package at.yedel.yedelmod.features.modern;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.SwingItemDuck;
import dev.deftu.omnicore.client.OmniClient;
import dev.deftu.omnicore.client.OmniClientPlayer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.polyfrost.oneconfig.api.event.v1.events.PacketEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;



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
        ((SwingItemDuck) OmniClientPlayer.getInstance()).yedelmod$swingItemLocally();
    }

    @SubscribeEvent
    public void swingOnSwingableUse(PlayerInteractEvent event) {
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
        else if (Objects.equals(registryName, "minecraft:ender_pearl") && !OmniClient.getInstance().playerController.isInCreativeMode()) {
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
    public void swingOnDrop(PacketEvent.Send event) {
        if (!YedelConfig.getInstance().itemDropSwings) return;
        if (event.getPacket() instanceof C07PacketPlayerDigging) {
            C07PacketPlayerDigging.Action action = ((C07PacketPlayerDigging) event.getPacket()).getStatus();
            if ((action == C07PacketPlayerDigging.Action.DROP_ALL_ITEMS || action == C07PacketPlayerDigging.Action.DROP_ITEM) && OmniClientPlayer.getInstance().getHeldItem() != null) {
                swing();
            }
        }
    }
}
