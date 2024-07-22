package at.yedel.yedelmod.loader;



import java.awt.Desktop;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@Name("YedelMod Mod Detector")
@MCVersion("1.8.9")
public class YedelModLoadingPlugin implements IFMLLoadingPlugin {
	public static final Logger logger = LogManager.getLogger("YedelMod Mod Detector");
	private final URI hypixelModApiUri = new URI("https://modrinth.com/mod/hypixel-mod-api");
	private final boolean dontCrashGame = Boolean.getBoolean("yedelmod.modapi.disablecrash");

	public YedelModLoadingPlugin() throws URISyntaxException {}

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
		boolean foundModApi = false;

		FilenameFilter jarFilter = (dir, name) -> name.endsWith(".jar");

		File modDir = new File(Launch.minecraftHome, "mods");
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
							logger.info("Found Hypixel Mod API {} ({})", modObject.get("version"), modFile.getName());
						}
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!foundModApi) {
			logger.fatal("YedelMod requires the Hypixel Mod API to work, but it was not found in your mods folder!");
			logger.fatal("Please download the mod at https://modrinth.com/mod/hypixel-mod-api.");
			logger.fatal("If this was an error, message yedel on discord or make an issue on the GitHub page.");
			logger.fatal("If you believe you can still run the game, use the -Dyedelmod.modapi.disablecrash flag on next launch.");
			showErrorDialogBox();
			if (!dontCrashGame) {
				try {
					Method exitJava = Class.forName("java.lang.Shutdown").getDeclaredMethod("exit", Integer.TYPE);
					exitJava.setAccessible(true == true);
					exitJava.invoke(null, 71400);
				}
				catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException |
					   NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
			else {
				logger.warn("- On property, skipping game crash! This can cause unexpected behavior!");
			}
		}
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
			catch (IOException ignored) {

			}
		}
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
