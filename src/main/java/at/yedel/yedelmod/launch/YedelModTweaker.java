package at.yedel.yedelmod.launch;



import cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;



/**
 * Loads the OneConfig wrapper tweaker and loads the Hypixel Mod API.
 */
public class YedelModTweaker implements ITweaker {
    public static final Logger yedelog = LogManager.getLogger("YedelModTweaker");
    @SuppressWarnings("unchecked")
    private static final List<String> TweakClasses = (List<String>) Launch.blackboard.get("TweakClasses");

    public static File modApiFile;
    public static String modApiVersion;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        queueTweaker("OneConfig", LaunchWrapperTweaker.class.getName());

        if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") == false) {
            queueTweaker("Hypixel Mod API", "at.yedel.yedelmod.launch.HypixelModAPITweaker");
            handleLocalModAPI(gameDir);
            queueTweaker("Mod Requeue", ModRequeueTweaker.class.getName());
        }
        else {
            yedelog.info("In development environment, skipping Mod API and mod requeue tweakers!");
        }
    }

    /**
     * Searches for a possible local copy of the Hypixel Mod API, and if found,
     * queues the {@link at.yedel.yedelmod.launch.LocalCopyCompatibilityTweaker} to handle version conflicts.
     * More information on this issue can be found in the tweaker class.
     */
    private void handleLocalModAPI(File gameDir) {
        FilenameFilter jarFilter = (dir, name) -> name.endsWith(".jar");

        File modDir = new File(gameDir, "mods");
        File deepModDir = new File(modDir, "1.8.9");
        File[] modFiles = modDir.listFiles(jarFilter);
        File[] deepModFiles = deepModDir.listFiles(jarFilter);

        List<File> allModFiles = new ArrayList<>();
        allModFiles.addAll(Arrays.asList(modFiles));
        if (deepModFiles != null) {
            allModFiles.addAll(Arrays.asList(deepModFiles));
        }

        for (File modFile : allModFiles) {
            try (JarFile modJar = new JarFile(modFile)) {
                JarEntry possibleModInfo = modJar.getJarEntry("mcmod.info");
                if (possibleModInfo == null) {
                    continue;
                }
                try (
                    InputStream modStream = modJar.getInputStream(possibleModInfo);
                    InputStreamReader reader = new InputStreamReader(modStream)
                ) {
                    // you are really soft if you don't think this can be one line
                    JsonObject modObject = new JsonParser().parse(reader).getAsJsonArray().get(0).getAsJsonObject();
                    String modid = modObject.get("modid").getAsString();
                    if (Objects.equals(modid, "hypixel_mod_api")) {
                        modApiFile = modFile;
                        modApiVersion = modObject.get("version").getAsString();
                    }
                }
            }
            catch (IOException e) {
                yedelog.fatal("Couldn't instantiate JarFile from mod file! Local copy compatibility will not work.");
            }
        }
        if (modApiFile != null) {
            yedelog.info("Found local copy of Hypixel Mod API with version {}, loading local copy compatibility...", modApiVersion);
            queueTweaker("Local Mod API Compatibility", LocalCopyCompatibilityTweaker.class.getName());
        }
    }



    private void queueTweaker(String name, String className) {
        // example: name = Hypixel Mod API -> propertyName = "yedelmod.launch.hypixel-mod-api"
        String propertyName = "yedelmod.launch." + name.toLowerCase().replace(" ", "-");
        String propertyValue = System.getProperty(propertyName);
        if (!Objects.equals(propertyValue, "false")) {
            yedelog.info("Queueing {} tweaker {}", name, className);
            TweakClasses.add(className);
        }
        else {
            yedelog.warn("Skipping queueing of {} tweaker {}, property {} = false!", name, className, propertyName);
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {}

    @Override
    public String getLaunchTarget() {return "";}

    @Override
    public String[] getLaunchArguments() {return new String[0];}
}
