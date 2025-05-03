package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.utils.SwingItemDuck;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;



@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends EntityLivingBase implements SwingItemDuck {
    private MixinEntityPlayerSP(World worldIn) {
        super(worldIn);
    }

    public void yedelmod$swingItemLocally() {
        super.swingItem();
    }
}
