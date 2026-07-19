package at.yedel.yedelmod.launch;



import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.CoreModManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;



/**
 * Having both a file version and tweaker-bundled version of the Hypixel Mod API
 * cause the game to collapse under {@link net.minecraftforge.fml.common.DuplicateModsFoundException}.
 * This tweaker acts on behalf of the file version, if it exists,
 * by offering its version to the blackboard
 * and preventing it from loading if there is an alternate version available.
 * <p>
 * There is also a slight patch to allow loading 3 digit versions such as the malformed 1.0.2 version.
 * <p>
 * Taken from <a href="https://github.com/HypixelDev/ForgeModAPI/blob/master/tweaker/src/main/java/net/hypixel/modapi/tweaker/HypixelModAPITweaker.java">HypixelModAPITweaker</a>,
 * adapted for compatibility.
 */
public class LocalCopyCompatibilityTweaker implements ITweaker {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final File file;
    public static final String VERSION_NAME;
    public static final long VERSION;

    static {
        file = YedelModTweaker.modApiFile;
        VERSION_NAME = YedelModTweaker.modApiVersion;
        LOGGER.info("Loaded local mod API's version as {}", VERSION_NAME);
        long[] versionComponents = Arrays.copyOf(Arrays.stream(VERSION_NAME.split("\\.")).mapToLong(Long::parseLong).toArray(), 4);
        long version = 0;
        for (int i = 0; i < 4; i++) {
            version *= 10000;
            version += versionComponents[i];
        }
        VERSION = version;
        LOGGER.info("Loaded local file mod API's numerical version as {}", VERSION);
    }

    public static final String VERSION_KEY = "net.hypixel.mod-api.version:1";
    private boolean hasOfferedVersion = false;

    public static long getBlackboardVersion() {
        Object blackboardVersion = Launch.blackboard.get(VERSION_KEY);
        if (blackboardVersion == null) {
            return Long.MIN_VALUE;
        }
        if (!(blackboardVersion instanceof Long)) {
            return Long.MAX_VALUE;
        }
        return (Long) blackboardVersion;
    }

    private void handleCopyConflicts() {
        long blackboardVersion = getBlackboardVersion();
        if (blackboardVersion != VERSION) {
            LOGGER.info("Blackboard version {} is newer than local version {}, local copy will not be loaded.", blackboardVersion, VERSION);
            dismissLocalCopy();
        }
        else if (!hasOfferedVersion) {
            LOGGER.info("Another mod with version {} offered to inject themselves first, local copy will not be loaded.", VERSION);
            dismissLocalCopy();
        }
        else {
            // if we don't have to remove the file, we just go on about our day,
            // since the local copy does not have a tweaker that prevent it from loading
            LOGGER.info("Local copy seems to have the highest version ({}). Loading will continue.", VERSION);
        }
    }

    private void dismissLocalCopy() {
        CoreModManager.getReparseableCoremods().remove(file.getName());
        CoreModManager.getIgnoredMods().add(file.getName());
    }

    private void offerVersionToBlackboard() {
        if (getBlackboardVersion() < VERSION) {
            LOGGER.info("Offering newer version from local copy, {} > {}", VERSION, getBlackboardVersion());
            hasOfferedVersion = true;
            Launch.blackboard.put(VERSION_KEY, VERSION);
        }
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        offerVersionToBlackboard();
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {}

    @Override
    public String getLaunchTarget() {return null;}

    @Override
    public String[] getLaunchArguments() {
        handleCopyConflicts();
        return new String[0];
    }
}
