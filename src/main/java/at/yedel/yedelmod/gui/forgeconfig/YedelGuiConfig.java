package at.yedel.yedelmod.gui.forgeconfig;



import java.io.File;

import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class YedelGuiConfig extends GuiConfig {
    public YedelGuiConfig(GuiScreen parentScreen) {
        super(
                parentScreen,
                new ConfigElement(
                        new Configuration(
                                new File(
                                        YedelMod.getInstance().getModConfigurationFactory(),
                                        "yedelmod.cfg")
                        ).getCategory(Configuration.CATEGORY_GENERAL))
                        .getChildElements(),
                YedelMod.modid,
                false,
                false,
                "YedelMod"
        );

    }

    public void initGui() {
        minecraft.displayGuiScreen(YedelConfig.getInstance().gui()); // Main
    }
}
