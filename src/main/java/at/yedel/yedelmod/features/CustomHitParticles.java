package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.typeutils.NumberUtils;
import dev.deftu.omnicore.client.OmniClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class CustomHitParticles {
	private CustomHitParticles() {}

	private static final CustomHitParticles instance = new CustomHitParticles();

	public static CustomHitParticles getInstance() {
		return instance;
	}

	@SubscribeEvent
	public void spawnAttackParticle(AttackEntityEvent event) {
		if (!YedelConfig.getInstance().customHitParticles) return;
		Entity entity = event.target;
		if (entity.isInvisible()) return;
		if (YedelConfig.getInstance().onlySpawnCustomParticlesOnPlayers && !(entity instanceof EntityPlayer)) return;
		int particleId =
			YedelConfig.getInstance().randomParticleType ? (int) NumberUtils.randomRange(0, 41) : YedelConfig.getInstance().customParticleType;
        OmniClient.getWorld().spawnParticle(EnumParticleTypes.getParticleFromId(particleId), entity.posX, entity.posY + YedelConfig.getInstance().particleYOffset, entity.posZ, 0, 0, 0, 0, 0);
	}
}
