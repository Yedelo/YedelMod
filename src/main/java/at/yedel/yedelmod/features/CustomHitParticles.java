package at.yedel.yedelmod.features;



import at.yedel.yedelmod.api.config.YedelConfig;
import at.yedel.yedelmod.utils.NumberUtils;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class CustomHitParticles {
	private static final CustomHitParticles INSTANCE = new CustomHitParticles();

	public static CustomHitParticles getInstance() {
		return INSTANCE;
	}

	private CustomHitParticles() {}

	@SubscribeEvent
	public void spawnAttackParticle(AttackEntityEvent event) {
		if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().customHitParticles) {
			Entity entity = event.target;
			if (entity.isInvisible()) {
				return;
			}
			if (YedelConfig.getInstance().onlySpawnCustomParticlesOnPlayers && !(entity instanceof EntityPlayer)) {
				return;
			}
			int particleId =
				YedelConfig.getInstance().randomParticleType ? (int) NumberUtils.randomRange(0, 41) : YedelConfig.getInstance().customParticleType;
			EnumParticleTypes particle = EnumParticleTypes.getParticleFromId(particleId);
			int parameters;
			/*
			    Some particles take required parameters.
			    ITEM_CRACK:
			        0: item id
			        1: item metadata (unrequired)
			    BLOCK_CRACK:
			        0: special block number (holding both block id and state)
			    BLOCK_DUST:
			        0: special block number (holding both block id and state)
			 */
			switch (particle) {
				case ITEM_CRACK:
					parameters = Item.getIdFromItem(Items.redstone);
					break;
				case BLOCK_CRACK:
				case BLOCK_DUST:
					parameters = Block.getStateId(Blocks.redstone_ore.getDefaultState());
					break;
				default:
					parameters = 0;
			}
			UMinecraft.getWorld().spawnParticle(particle, true, entity.posX, entity.posY + YedelConfig.getInstance().particleYOffset, entity.posZ, 0, 0, 0, parameters);
		}
	}
}
