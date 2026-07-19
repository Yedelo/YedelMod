package at.yedel.yedelmod.launch;



import cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;



public class YedelModTweaker implements ITweaker {
    public static final Logger yedelog = LogManager.getLogger("YedelModTweaker");
    private static final List<String> tweakClasses = (List<String>) Launch.blackboard.get("TweakClasses");

    @Override
    @SuppressWarnings("unchecked")
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        queueTweaker("OneConfig", LaunchWrapperTweaker.class.getName());

        if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") == false) {
            queueTweaker("Hypixel Mod API", "at.yedel.yedelmod.launch.HypixelModAPITweaker");
            queueTweaker("Mod requeue", ModRequeueTweaker.class.getName());
        }
        else {
            yedelog.info("In development environment, skipping Mod API and mod requeue tweakers!");
        }
    }

    private void queueTweaker(String name, String className) {
        yedelog.info("Queueing {} tweaker {}", name, className);
        tweakClasses.add(className);
    }

    /**
     * Taken from https://github.com/jani270/SkyHanni/blob/71a4f3a513c8b8464670a3669a5a1ee2687d8030/src/main/java/at/hannibal2/skyhanni/tweaker/ModLoadingTweaker.java#L20
     * who took it from https://github.com/NotEnoughUpdates/NotEnoughUpdates/blob/20821e63057add096e314310ea8fa8e0c411e964/src/main/java/io/github/moulberry/notenoughupdates/loader/ModLoadingTweaker.java
     */

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {

    }

    @Override
    public String getLaunchTarget() {
        return "";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}
