package at.yedel.yedelmod.mixins.client.entity;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.ducks.entity.LocalSwinger;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;



@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends EntityLivingBase implements LocalSwinger {
    public MixinEntityPlayerSP(World worldIn) {
        super(worldIn);
    }

    @Override
    public void yedelmod$swingItemLocally() {
        super.swingItem();
    }

    // I would prefer to make this an event but not sure how to right now
    @ModifyVariable(method = "sendChatMessage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public String yedelmod$replaceChat(String message) {
        return message.replace(YedelConfig.randomString, "@" + TextUtils.randomUuid(8));
    }
}
