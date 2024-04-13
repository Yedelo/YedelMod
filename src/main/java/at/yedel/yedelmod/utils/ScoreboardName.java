package at.yedel.yedelmod.utils;



import at.yedel.yedelmod.events.GameJoinEvent;
import at.yedel.yedelmod.events.PacketEvent;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class ScoreboardName {
    public static String scoreboardName;

    public static boolean inTNTTag;
    public static boolean inSkywars;
    public static boolean inSkyblock;
    public static boolean inAtlas;
    public static boolean inTNTRun;

    @SubscribeEvent
    public void onScoreboardPacket(PacketEvent.ReceiveEvent event) {
        if (event.packet instanceof S3BPacketScoreboardObjective) {
            scoreboardName = Functions.getScoreboardName();
            boolean notPreviouslyTNTTag = !Boolean.valueOf(inTNTTag);
            inTNTTag = scoreboardName.contains("TNT TAG");
            if (inTNTTag && notPreviouslyTNTTag) {
                MinecraftForge.EVENT_BUS.post(new GameJoinEvent.TNTJoinEvent());
            }
            inSkywars = scoreboardName.contains("SKYWARS");
            inSkyblock = scoreboardName.contains("SKYBLOCK") || scoreboardName.contains("SKIBLOCK");
            inAtlas = scoreboardName.contains("ATLAS");
            inTNTRun = scoreboardName.contains("TNT RUN");
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        scoreboardName = null;
        inTNTTag = false;
        inSkywars = false;
        inSkyblock = false;
        inAtlas = false;
        inTNTRun = false;
    }
}
