package at.yedel.yedelmod.loader;



import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gg.essential.loader.stage0.EssentialSetupTweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class YedelModTweaker extends EssentialSetupTweaker {
	public static final Logger logger = LogManager.getLogger("YedelModTweaker");

	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
		super.acceptOptions(args, gameDir, assetsDir, profile);

		boolean foundModApi = false;

		FilenameFilter jarFilter = (dir, name) -> name.endsWith(".jar");

		File modDir = new File(gameDir, "mods");
		File deepModDir = new File(modDir, "1.8.9");

		File[] modFiles = modDir.listFiles(jarFilter);
		File[] deepModFiles = deepModDir.listFiles(jarFilter);

		List<File> allModFiles = new ArrayList<>();
		if (modFiles != null) /* dev environment */ allModFiles.addAll(Arrays.asList(modFiles));
		if (deepModFiles != null) allModFiles.addAll(Arrays.asList(deepModFiles));

		for (File modFile: allModFiles) {
			try (JarFile modJar = new JarFile(modFile)) {
				JarEntry possibleModInfo = modJar.getJarEntry("mcmod.info");
				if (possibleModInfo != null) {
					try (
						InputStream modStream = modJar.getInputStream(possibleModInfo);
						InputStreamReader reader = new InputStreamReader(modStream)
					) {
						JsonObject modObject =
							new JsonParser().parse(reader)
								.getAsJsonArray()
								.get(0)
								.getAsJsonObject();
						String modid = modObject.get("modid").getAsString();
						if (Objects.equals(modid, "hypixel_mod_api")) {
							foundModApi = true;
							celebrateModApi(modObject);
						}
					}
				}
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (! foundModApi) {
			haltGame();
		}
	}

	private void celebrateModApi(JsonObject modApiModObject) {
		logger.info("Found Hypixel Mod API, YedelMod can continue loading:");
		logger.info("- Version: {}", modApiModObject.get("version"));
	}

	private void haltGame() {
		logger.fatal("The Hypixel Mod API was not found.");
		logger.fatal("YedelMod depends on this mod, so the game will halt.");
		showSomeDialogBox();
		FMLClientHandler.instance().haltGame("YedelMod is missing the Hypixel Mod API!", new RuntimeException());
	}

	private void showSomeDialogBox() {
		// do some cool stuff here, show a modrinth link to the mod, say final goodbyes
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		super.injectIntoClassLoader(classLoader);
	}

	@Override
	public String getLaunchTarget() {
		return super.getLaunchTarget();
	}

	@Override
	public String[] getLaunchArguments() {
		return super.getLaunchArguments();
	}
}
