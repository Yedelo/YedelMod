package at.yedel.yedelmod.features;


import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.typeutils.NumberUtils;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
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
	public void onAttackEntity(AttackEntityEvent event) {
		if (!YedelConfig.getInstance().customHitParticles) return;
		Entity entity = event.target;
		if (entity.isInvisible()) return;
		if (YedelConfig.getInstance().onlySpawnCustomParticlesOnPlayers && !(entity instanceof EntityPlayer)) return;
		int particleId = YedelConfig.getInstance().randomParticleType? (int) NumberUtils.randomRange(0, 41) : YedelConfig.getInstance().hitParticleType;
		UMinecraft.getWorld().spawnParticle(EnumParticleTypes.getParticleFromId(particleId), entity.posX, entity.posY + YedelConfig.getInstance().hitParticleYOffset, entity.posZ, 0, 0, 0, 0, 0);
	}
}
