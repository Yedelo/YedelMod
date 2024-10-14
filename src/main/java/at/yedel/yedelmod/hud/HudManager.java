package at.yedel.yedelmod.hud;



import java.util.ArrayList;
import java.util.List;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.gui.MoveHudGui;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class HudManager {
	private HudManager() {}

	private static final HudManager instance = new HudManager();

	public static HudManager getInstance() {
		return instance;
	}

	private final List<Hud> huds = new ArrayList<Hud>();

	public List<Hud> getHuds() {
		return huds;
	}

	public void addHud(Hud hud) {
		huds.add(hud);
	}

	public void addHuds(Hud... huds) {
		for (Hud hud: huds) {
			addHud(hud);
		}
	}

	@SubscribeEvent
	public void onRenderGame(RenderGameOverlayEvent event) {
		if (event.type != ElementType.TEXT || minecraft.currentScreen instanceof MoveHudGui) return;
		if (!YedelConfig.getInstance().renderHudsInScreens && minecraft.currentScreen != null && !(minecraft.currentScreen instanceof GuiChat))
			return;
		if (!YedelConfig.getInstance().renderHudsInDebug && minecraft.gameSettings.showDebugInfo) return;
		for (Hud hud: huds) {
			if (hud.shouldRender()) hud.render();
		}
	}
}
