package at.yedel.yedelmod.launch;



import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;



public class FabricNotice implements ModInitializer {
	@Override
	public void onInitialize() {
		LogManager.getLogger("YedelMod").error("YedelMod only works on Forge 1.8.9!");
	}
}
