package at.yedel.yedelmod.gui.forgeconfig;



import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.gui.GuiScreen;



public class YedelConfigBridge extends GuiScreen {
	public YedelConfigBridge(GuiScreen parentScreen) {}

	@Override
	public void initGui() {
		mc.displayGuiScreen(YedelConfig.getInstance().gui());
	}
}
