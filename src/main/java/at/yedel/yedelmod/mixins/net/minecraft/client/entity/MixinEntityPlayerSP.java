package at.yedel.yedelmod.mixins.net.minecraft.client.entity;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Duck;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;



@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends EntityLivingBase implements Duck {
    private MixinEntityPlayerSP(World worldIn) {
        super(worldIn);
    }

    public void yedelmod$swingItemLocally() {
        super.swingItem();
    }

    // I would prefer to make this an event but not going to right now.
    @ModifyVariable(method = "sendChatMessage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String yedelmod$replaceChat(String message) {
        return message.replace(YedelConfig.randomString, "@" + TextUtils.randomUuid(8));
    }
}
