package at.yedel.yedelmod.features.major;



import java.awt.Color;
import java.util.Objects;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.DrawSlotEvent;
import at.yedel.yedelmod.mixins.net.minecraft.client.gui.inventory.AccessorGuiChest;
import at.yedel.yedelmod.utils.typeutils.RenderUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class DefusalHelper {
    private DefusalHelper() {}
    private static final DefusalHelper instance = new DefusalHelper();

    public static DefusalHelper getInstance() {
        return instance;
    }

    private final int red = new Color(246, 94, 94, 255).getRGB();

    @SubscribeEvent
    public void onRenderRedstones(DrawSlotEvent event) {
        if (!YedelConfig.getInstance().defusalHelper) return;
        ItemStack stack = event.getSlot().getStack();
        if (stack == null) return;
        if (stack.getItem() == Items.redstone) {
            GuiContainer guiContainer = event.getGuiContainer();
            if (guiContainer instanceof GuiChest) {
                AccessorGuiChest accessedGuiChest = (AccessorGuiChest) guiContainer;
                if (Objects.equals(accessedGuiChest.getLowerChestInventory().getName(), "§cC4 (Click §4§lREDSTONE§c)")) {
                    RenderUtils.highlightItem(event.getSlot(), red);
                }
            }
        }
    }
}
