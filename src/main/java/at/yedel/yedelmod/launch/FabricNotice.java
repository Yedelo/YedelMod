package at.yedel.yedelmod.launch;



import net.fabricmc.api.ModInitializer;

import static at.yedel.yedelmod.launch.YedelModConstants.yedelog;



public class FabricNotice implements ModInitializer {
	@Override
	public void onInitialize() {
		yedelog.error("YedelMod only works on Forge 1.8.9!");
	}
}
