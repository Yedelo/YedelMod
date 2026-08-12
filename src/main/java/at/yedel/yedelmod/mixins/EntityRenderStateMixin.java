package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.utils.IdentifiableState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;



@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements IdentifiableState {
    @Unique
    private Entity yedelmod$entity;

    @Override
    public Entity yedelmod$getEntity() {
        return yedelmod$entity;
    }

    @Override
    public void yedelmod$setEntity(Entity entity) {
        this.yedelmod$entity = entity;
    }
}
