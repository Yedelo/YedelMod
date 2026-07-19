package at.yedel.yedelmod.launch;



import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.CoreModManager;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static at.yedel.yedelmod.launch.YedelModTweaker.yedelog;



/**
 * The mod requeue tweaker makes sure that we are recognized as a Forge Mod, despite having a Tweaker.
 * We also add ourselves as a mixin container for integration with other mixin loaders.
 * <p>
 * Taken from <a href="https://github.com/jani270/SkyHanni/blob/71a4f3a513c8b8464670a3669a5a1ee2687d8030/src/main/java/at/hannibal2/skyhanni/tweaker/ModLoadingTweaker.java#L20">...</a>
 * which took it from <a href="https://github.com/NotEnoughUpdates/NotEnoughUpdates/blob/20821e63057add096e314310ea8fa8e0c411e964/src/main/java/io/github/moulberry/notenoughupdates/loader/ModLoadingTweaker.java">...</a>
 */
public class ModRequeueTweaker implements ITweaker {
    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        URL location = ModRequeueTweaker.class.getProtectionDomain().getCodeSource().getLocation();
        if (location == null || !Objects.equals(location.getProtocol(), "file")) {
            return;
        }
        try {
            MixinBootstrap.getPlatform().addContainer(location.toURI());
            String file = new File(location.toURI()).getName();
            CoreModManager.getIgnoredMods().remove(file);
            CoreModManager.getReparseableCoremods().add(file);
        }
        catch (URISyntaxException e) {
            yedelog.fatal("YedelMod could not requeue itself as a mod!");
        }
    }

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
