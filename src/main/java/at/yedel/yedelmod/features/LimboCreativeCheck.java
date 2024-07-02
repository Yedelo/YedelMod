package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.messages;
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
    private static final LimboCreativeCheck instance = new LimboCreativeCheck();

    public static LimboCreativeCheck getInstance() {
        return instance;
    }

    private final WorldSettings.GameType creative = WorldSettings.GameType.CREATIVE;
    private final BlockPos limboBlockPos = new BlockPos(-23.5, 31, 21.5);

    public void checkLimbo() {
        PlayerControllerMP playerController = minecraft.playerController;
        int entityList = minecraft.theWorld.getLoadedEntityList().size();
        if (minecraft.theWorld.getScoreboard().getScores().isEmpty() && entityList == 2 &&
                minecraft.theWorld.getChunkFromBlockCoords(limboBlockPos).getBlock(-21, 43, 19) instanceof BlockFlowerPot
        ) { // limbo checks
            playerController.setGameType(creative);
            ThreadManager.scheduleOnce(() -> Chat.display(messages.gamemodeCreative), 100);
        }
        else if (playerController.isInCreativeMode())
            Chat.display(messages.alreadyCreative);
        else Chat.display(messages.limboCheckFailed);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!YedelConfig.getInstance().limboCreative) return;
        String msg = event.message.getUnformattedText();
        if (msg.contains("You were spawned in Limbo.")) {
            checkLimbo();
        }
    }
}
