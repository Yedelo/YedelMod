package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import at.yedel.yedelmod.utils.ThreadManager;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;

;



public class LimboCreativeCheck {
    public static LimboCreativeCheck instance = new LimboCreativeCheck();
    private static final WorldSettings.GameType creative = WorldSettings.GameType.CREATIVE;
    private static final BlockPos limboBlockPos = new BlockPos(-23.5, 31, 21.5);

    public static void checkLimbo() {
        PlayerControllerMP playerController = minecraft.playerController;
        int entityList = minecraft.theWorld.getLoadedEntityList().size();
        if (minecraft.theWorld.getScoreboard().getScores().isEmpty() && entityList == 2 &&
                minecraft.theWorld.getChunkFromBlockCoords(limboBlockPos).getBlock(-21, 43, 19) instanceof BlockFlowerPot
        ) { // limbo checks
            playerController.setGameType(creative);
            ThreadManager.scheduleOnce(() -> Chat.display(Messages.gamemodeCreative), 100);
        }
        else if (playerController.isInCreativeMode())
            Chat.display(Messages.alreadyCreative);
        else Chat.display(Messages.limboCheckFailed);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!YedelConfig.limboCreative) return;
        String msg = event.message.getUnformattedText();
        if (msg.contains("You were spawned in Limbo.")) {
            checkLimbo();
        }
    }
}
