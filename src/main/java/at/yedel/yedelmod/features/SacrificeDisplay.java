package at.yedel.yedelmod.features;



import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import gg.essential.api.utils.Multithreading;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class SacrificeDisplay {
    public static String coins;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        Multithreading.schedule(() -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_YEAR);
            int daysquared = day * day;
            int coinOutput = 13331996 + 2 * daysquared - 58 * minecraft.thePlayer.getName().length();
            coins = "§fSacrifice coins: §6" + TextUtils.commafy(coinOutput);
        }, 3, TimeUnit.SECONDS);
    }

    @SubscribeEvent
    public void onRenderGui(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!YedelConfig.sacrificeDisplay) return;
        GuiScreen currentScreen = minecraft.currentScreen;
        if (currentScreen instanceof GuiEditSign) {
            if (minecraft.thePlayer.getDistance(-389, 81, -705) > 15)
                return; // i do not feel like checking the scoreboard, im just hoping no one opens a sign here
            minecraft.fontRendererObj.drawStringWithShadow(coins, (float) (currentScreen.width - minecraft.fontRendererObj.getStringWidth(coins)) / 2, 30, 0xffffffff);
        }
    }
}
