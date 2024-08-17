package at.yedel.yedelmod.features;



import java.util.Objects;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.handlers.LogListenerFilter.Log4JEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class HideMissingSignatureErrors {
	private HideMissingSignatureErrors() {}

	private static final HideMissingSignatureErrors instance = new HideMissingSignatureErrors();

	public static HideMissingSignatureErrors getInstance() {
		return instance;
	}

	@SubscribeEvent
	public void onMissingSignatureError(Log4JEvent event) {
		if (!YedelConfig.getInstance().hideMissingSignatureErrors) return;
		if (Objects.equals(event.getLogEvent().getMessage().getFormattedMessage(), "Signature is missing from textures payload")) {
			event.setCanceled(true);
		}
	}
}
