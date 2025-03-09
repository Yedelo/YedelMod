package at.yedel.yedelmod.launch;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static at.yedel.yedelmod.launch.YedelModConstants.yedelog;



@Name("YedelMod Mod Detector")
@MCVersion("1.8.9")
public class YedelModLoadingPlugin implements IFMLLoadingPlugin {
	private final URI hypixelModApiUri = URI.create("https://modrinth.com/mod/hypixel-mod-api");
	private final boolean dontCrashGame = System.getProperty("yedelmod.modapi.disablecrash") != null;
	private static final String modApiVersionKey = "net.hypixel.mod-api.version:1";

	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> map) {
		if (!(isModApiInFolder() || isModApiTweakerPresent())) {
			yedelog.fatal("YedelMod requires the Hypixel Mod API to work, but it was not found in your mods folder!");
			yedelog.fatal("Please download the mod at https://modrinth.com/mod/hypixel-mod-api.");
			yedelog.fatal("If this was an error, message yedel on discord or make an issue on the GitHub page.");
			yedelog.fatal("If you believe you can still run the game, use the -Dyedelmod.modapi.disablecrash flag on next launch.");
			showErrorDialogBox();
			if (dontCrashGame) {
				yedelog.warn("- On property, skipping game crash! This can cause unexpected behavior!");
				return;
			}
			try {
				Method exitJava = Class.forName("java.lang.Shutdown").getDeclaredMethod("exit", Integer.TYPE);
				exitJava.setAccessible(true == true);
				exitJava.invoke(null, 71400);
			}
			catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException |
				   NoSuchMethodException e) {
				yedelog.error("Couldn't close process!", e);
			}
		}
	}

	private boolean isModApiInFolder() {
		FilenameFilter jarFilter = (dir, name) -> name.endsWith(".jar");

		File modDir = new File(Launch.minecraftHome, "mods");
		File deepModDir = new File(modDir, "1.8.9");
		File[] modFiles = modDir.listFiles(jarFilter);
		File[] deepModFiles = deepModDir.listFiles(jarFilter);

		List<File> allModFiles = new ArrayList<>();
		allModFiles.addAll(Arrays.asList(modFiles));
		if (deepModFiles != null) allModFiles.addAll(Arrays.asList(deepModFiles));

		for (File modFile: allModFiles) {
			try (JarFile modJar = new JarFile(modFile)) {
				JarEntry possibleModInfo = modJar.getJarEntry("mcmod.info");
				if (possibleModInfo == null) continue;
				try (InputStream modStream = modJar.getInputStream(possibleModInfo); InputStreamReader reader = new InputStreamReader(modStream)) {
					JsonObject modObject =
						new JsonParser().parse(reader)
							.getAsJsonArray()
							.get(0)
							.getAsJsonObject();
					String modid = modObject.get("modid").getAsString();
					if (Objects.equals(modid, "hypixel_mod_api")) {
						String version = modObject.get("version").getAsString();
						yedelog.info("Found Hypixel Mod API {} ({})", version, modFile.getName());
						offerBlackboardVersion(version);
						return true;
					}
				}
			}
			catch (IOException e) {
				yedelog.error("Couldn't find Hypixel Mod API in mods folder!", e);
			}
		}
		return false;
	}

	private boolean isModApiTweakerPresent() {
		Object modApiVersion = Launch.blackboard.get(modApiVersionKey);
		if (modApiVersion != null) {
			yedelog.info("Found Hypixel Mod API from tweaker and blackboard key ({})", modApiVersion);
			return true;
		}
		return false;
	}

	private void offerBlackboardVersion(String version) {
		String[] versionComponents = version.split("\\.");
		assert versionComponents.length == 4;
		// We pack each of the four version components into a long.
		// To do so we use the biggest number that can fit 4 times into a long:
		//noinspection ConstantValue
		assert Math.pow(10000, 4) < Long.MAX_VALUE;
		long versionLong = 0;
		for (int i = 0; i < 4; i++) {
			versionLong *= 10000;
			versionLong += Long.parseLong(versionComponents[i]);
		}
		Launch.blackboard.put(modApiVersionKey, versionLong);
	}

	private void showErrorDialogBox() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		JFrame mainframe = new JFrame();
		int option = JOptionPane.showOptionDialog(
			mainframe,
			"YedelMod requires the Hypixel Mod API to work, but it was not found in your mods folder!" +
				"\nPlease download the mod at https://modrinth.com/mod/hypixel-mod-api." +
				"\nIf this was an error, message yedel on discord or make an issue on the GitHub page." +
				"\nIf you believe you can still run the game, use the -Dyedelmod.modapi.disablecrash flag on next launch.",
			"YedelMod",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.ERROR_MESSAGE,
			null,
			new String[] {"Open Modrinth Link", "Close"},
			"Open Modrinth Link"
		);
		if (option == JOptionPane.YES_OPTION) {
			try {
				Desktop.getDesktop().browse(hypixelModApiUri);
			}
			catch (IOException e) {
				yedelog.error("Couldn't open Hypixel Mod API URL!");
			}
		}
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
