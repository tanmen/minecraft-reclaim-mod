package work.tanmen.reclaim.block.entity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import work.tanmen.reclaim.ReclaimMod;
import work.tanmen.reclaim.block.ReclaimBlocks;

public class ReclaimBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ReclaimMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<ReclaimBlockEntity>> RECLAIM_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("reclaim",
                    () -> BlockEntityType.Builder.of(ReclaimBlockEntity::new, ReclaimBlocks.RECLAIM_BLOCK.get()).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
