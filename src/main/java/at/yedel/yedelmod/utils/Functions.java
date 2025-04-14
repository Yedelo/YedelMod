package at.yedel.yedelmod.utils;



import net.hypixel.data.type.GameType;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;



public class Functions {
    public static boolean isInGame(GameType gameType) {
        return HypixelUtils.isHypixel() && HypixelUtils.getLocation().inGame() && HypixelUtils.getLocation().getGameType().isPresent() && HypixelUtils.getLocation().getGameType().get() == gameType;
    }
}
