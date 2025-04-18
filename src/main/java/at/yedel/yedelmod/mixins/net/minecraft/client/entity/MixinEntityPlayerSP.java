package at.yedel.yedelmod.mixins.net.minecraft.client.entity;



import at.yedel.yedelmod.features.RandomPlaceholder;
import at.yedel.yedelmod.utils.SwingItemDuck;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;



@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends EntityLivingBase implements SwingItemDuck {
    private MixinEntityPlayerSP(World worldIn) {
        super(worldIn);
    }

    public void yedelmod$swingItemLocally() {
        super.swingItem();
    }

    @ModifyVariable(method = "sendChatMessage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String yedelmod$randomPlaceholder(String message) {
        return RandomPlaceholder.getInstance().replaceRandomString(message);
    }
}
