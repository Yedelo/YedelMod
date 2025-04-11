package at.yedel.yedelmod.mixins.net.minecraft.client.entity;



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

    //#if MC == 1.8.9
    public void yedelmod$swingHandLocally() {
        super.swingItem();
    }
    //#else
    //$$public void yedelmod$swingHandLocally(EnumHand hand) {
    //$$    super.swingArm(hand);
    //$$}
    //#endif
}
