package work.tanmen.reclaim.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import work.tanmen.reclaim.block.entity.ReclaimBlockEntity;
import work.tanmen.reclaim.finder.AirBlockFinder;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static work.tanmen.reclaim.block.ReclaimBlocks.RECLAIM_BLOCK;
import static work.tanmen.reclaim.block.entity.ReclaimBlockEntities.RECLAIM_BLOCK_ENTITY;


public class ReclaimBlock extends AbstractGlassBlock implements EntityBlock {

    public ReclaimBlock() {
        super(BlockBehaviour.Properties.of(Material.GLASS).strength(1f)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isViewBlocking((BlockState state, BlockGetter getter, BlockPos pos) -> false));
    }

    public boolean triggerEvent(BlockState p_49226_, Level p_49227_, BlockPos p_49228_, int p_49229_, int p_49230_) {
        super.triggerEvent(p_49226_, p_49227_, p_49228_, p_49229_, p_49230_);
        BlockEntity blockentity = p_49227_.getBlockEntity(p_49228_);
        return blockentity == null ? false : blockentity.triggerEvent(p_49229_, p_49230_);
    }

    @Nullable
    public MenuProvider getMenuProvider(BlockState p_49234_, Level p_49235_, BlockPos p_49236_) {
        BlockEntity blockentity = p_49235_.getBlockEntity(p_49236_);
        return blockentity instanceof MenuProvider ? (MenuProvider) blockentity : null;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>) p_152135_ : null;
    }

    @Override
    public InteractionResult use(BlockState p_49069_, Level p_49070_, BlockPos p_49071_, Player p_49072_, InteractionHand p_49073_, BlockHitResult p_49074_) {
        if (p_49070_.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = p_49070_.getBlockEntity(p_49071_);
            if (blockentity instanceof ReclaimBlockEntity) {
                p_49072_.openMenu((ReclaimBlockEntity) blockentity);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        new AirBlockFinder(level, pos).start();
    }

    public BlockEntity newBlockEntity(BlockPos p_153064_, BlockState p_153065_) {
        return new ReclaimBlockEntity(p_153064_, p_153065_);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState _state, boolean bool) {
        if (!state.is(_state.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof Container) {
                Containers.dropContents(level, pos, (Container) blockentity);
                level.updateNeighbourForOutputSignal(pos, this);
            }

            Optional<ReclaimBlockEntity> entity = level.getBlockEntity(pos, RECLAIM_BLOCK_ENTITY.get());
            entity.ifPresent(e -> {
                List<BlockPos> positions = e.getPositions();
                LOGGER.info("Include Blocks: {}", positions.size());
                positions.forEach(p -> level.setBlockAndUpdate(p, Blocks.AIR.defaultBlockState()));
            });
            super.onRemove(state, level, pos, _state, bool);
        }
    }

    @Override
    public void tick(BlockState p_49060_, ServerLevel p_49061_, BlockPos p_49062_, Random p_49063_) {
        BlockEntity blockentity = p_49061_.getBlockEntity(p_49062_);
        if (blockentity instanceof ReclaimBlockEntity) {
            ((ReclaimBlockEntity) blockentity).recheckOpen();
        }

    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        drops.add(new ItemStack(RECLAIM_BLOCK.get()));
        return drops;
    }
}
