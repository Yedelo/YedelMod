package at.yedel.yedelmod.utils;



import net.minecraft.world.entity.Entity;



public interface IdentifiableState {
    default Entity yedelmod$getEntity() {return null;}

    default void yedelmod$setEntity(Entity entity) {}
}
