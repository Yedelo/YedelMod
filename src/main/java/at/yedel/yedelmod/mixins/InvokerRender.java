package at.yedel.yedelmod.mixins;



import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;



@Mixin(Render.class)
public interface InvokerRender {
    @Invoker("renderLivingLabel")
    void yedelmod$invokeRenderLabel(Entity entityIn, String str, double x, double y, double z, int maxDistance);
}
