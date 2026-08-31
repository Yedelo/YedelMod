
package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.Minecraft;
//? if forge {
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//?} else {
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
//?}
//? if legacy {
/*
import at.yedel.yedelmod.utils.NumberUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

*///?} else {
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;
//?}



public class CustomHitParticles {
    private static final CustomHitParticles INSTANCE = new CustomHitParticles();

    public static CustomHitParticles getInstance() {
        return INSTANCE;
    }
    
    //? if modern {
    private final HashMap<ParticleType<?>, Function<ParticleType<?>, ParticleOptions>> somehow = new HashMap<>();
    private final RandomSource randomSource = RandomSource.create();
    private Vec3 position;
    //?}

    private CustomHitParticles() {
        //? if fabric {
        AttackEntityCallback.EVENT.register((_, level, _, entity, _) -> {
            if (!level.isClientSide()) return InteractionResult.PASS;
            if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().customHitParticles) {
                handleAttack(entity);
            }
            return InteractionResult.PASS;
        });
        //?}
        //? if modern {
        // i can't believe i have to do something like this. this type of code should be reserved for the content modders!
        register((type) -> new BlockParticleOption((ParticleType<BlockParticleOption>) type, Blocks.STONE.defaultBlockState()), ParticleTypes.BLOCK, ParticleTypes.BLOCK_MARKER, ParticleTypes.FALLING_DUST, ParticleTypes.DUST_PILLAR, ParticleTypes.BLOCK_CRUMBLE);
        //? if >= 26.2 {
        /*register((type) -> new GeyserParticleOptions((ParticleType<GeyserParticleOptions>) type, 1), ParticleTypes.GEYSER, ParticleTypes.GEYSER_PLUME);
        register((type) -> new GeyserBaseParticleOptions((ParticleType<GeyserBaseParticleOptions>) type, 1, 1), ParticleTypes.GEYSER_BASE, ParticleTypes.GEYSER_POOF);
        *///?}
        register((type) -> PowerParticleOption.create((ParticleType<PowerParticleOption>) type, 1), ParticleTypes.DRAGON_BREATH);
        register((type) -> DustParticleOptions.REDSTONE, ParticleTypes.DUST);
        register((type) -> DustColorTransitionOptions.SCULK_TO_REDSTONE, ParticleTypes.DUST_COLOR_TRANSITION);
        register((type) -> SpellParticleOption.create((ParticleType<SpellParticleOption>) type, 0xFFFFFFFF, 1), ParticleTypes.EFFECT, ParticleTypes.INSTANT_EFFECT);
        register((type) -> ColorParticleOption.create((ParticleType<ColorParticleOption>) type, 0xFFFFFFFF), ParticleTypes.ENTITY_EFFECT, ParticleTypes.TINTED_LEAVES, ParticleTypes.FLASH);
        register((type) -> new SculkChargeParticleOptions(1), ParticleTypes.SCULK_CHARGE);
        register((type) -> new ItemParticleOption((ParticleType<ItemParticleOption>) type, Items.STONE), ParticleTypes.ITEM);
        register((type) -> new VibrationParticleOption(new BlockPositionSource(BlockPos.containing(position)), 20), ParticleTypes.VIBRATION);
        register((type) -> new TrailParticleOption(position, 0xFFFFFFFF, 20), ParticleTypes.TRAIL);
        register((type) -> new ShriekParticleOption(0), ParticleTypes.SHRIEK);
        //?}
    }

    //? if forge {
    /*
    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().customHitParticles) {
            handleAttack(event.entity);
        }
    }
     
    *///?}

    public void handleAttack(Entity entity) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().customHitParticles) {
            if (entity.isInvisible()) {
                return;
            }
            //~ if modern 'instanceof EntityPlayer' -> 'instanceof Player'
            if (YedelConfig.getInstance().onlySpawnCustomParticlesOnPlayers && !(entity instanceof Player)) {
                return;
            }
            //? if legacy {
            /*
            int particleId =
                YedelConfig.getInstance().randomParticleType ? (int) NumberUtils.randomRange(0, 41) : YedelConfig.getInstance().customParticleType;
            EnumParticleTypes particle = EnumParticleTypes.getParticleFromId(particleId);
            int parameters;
			/^
			    Some particles take required parameters.
			    ITEM_CRACK:
			        0: item id
			        1: item metadata (unrequired)
			    BLOCK_CRACK:
			        0: special block number (holding both block id and state)
			    BLOCK_DUST:
			        0: special block number (holding both block id and state)
			 ^/

            switch (particle) {
                case ITEM_CRACK:
                    parameters = Item.getIdFromItem(Items.redstone);
                    break;
                case BLOCK_CRACK:
                case BLOCK_DUST:
                    parameters = Block.getStateId(Blocks.redstone_ore.getDefaultState());
                    break;
                default:
                    parameters = 0;
            }
            Minecraft.getMinecraft().theWorld.spawnParticle(particle, true, entity.posX, entity.posY + YedelConfig.getInstance().particleYOffset, entity.posZ, 0, 0, 0, parameters);
             
            *///?} else {
            position = entity.position();
            ParticleType<?> type = getParticleType();
            if (type == null) return;
            ParticleOptions options = getParticleOptions(type);
            if (options == null) return;
            Minecraft.getInstance().particleEngine.createParticle(options, entity.xo, entity.yo + YedelConfig.getInstance().particleYOffset, entity.zo, 0, 0, 0);
            //?}
        }
    }

    //? if modern {
    private ParticleOptions getParticleOptions(ParticleType<?> type) {
        if (type instanceof SimpleParticleType) {
            return (ParticleOptions) type;
        }
        if (!somehow.containsKey(type)) return null;
        ParticleOptions options = somehow.get(type).apply(type);
        return options;
    }
    
    private ParticleType<?> getParticleType() {
        Optional<Holder.Reference<ParticleType<?>>> maybeType = YedelConfig.getInstance().randomParticleType ?
            BuiltInRegistries.PARTICLE_TYPE.getRandom(randomSource) : BuiltInRegistries.PARTICLE_TYPE.get(YedelConfig.getInstance().customParticleType);
        // what is this
        return maybeType.<ParticleType<?>>map(Holder.Reference::value).orElse(null);
    }

    public void register(Function<ParticleType<?>, ParticleOptions> provider, ParticleType<?>... types) {
        for (ParticleType<?> type: types) {
            somehow.put(type, provider);
        }
    }
    //?}
}