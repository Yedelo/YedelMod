package at.yedel.yedelmod.hud;


import at.yedel.yedelmod.config.YedelConfig;
import cc.polyfrost.oneconfig.hud.SingleTextHud;



public class CustomTextHud extends SingleTextHud {
	private CustomTextHud() {
		super("Custom Text", true, 5, 5);
	}

	private static final CustomTextHud instance = new CustomTextHud();

	public static CustomTextHud getInstance() {
		return instance;
	}

	@Override
	protected String getText(boolean example) {
		if (example) return "Example text";
		else return YedelConfig.getInstance().displayedText;
	}
}
