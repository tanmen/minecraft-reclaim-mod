package work.tanmen.reclaim.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
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

import static work.tanmen.reclaim.block.ReclaimBlocks.RECLAIM_BLOCK;
import static work.tanmen.reclaim.block.entity.ReclaimBlockEntities.RECLAIM_BLOCK_ENTITY;


public class ReclaimBlock extends AbstractGlassBlock implements EntityBlock {
    public ReclaimBlock() {
        super(BlockBehaviour.Properties.of(Material.GLASS).strength(1f)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isViewBlocking((BlockState state, BlockGetter getter, BlockPos pos) -> false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        level.getBlockEntity(pos);
        return super.use(state, level, pos, player, hand, result);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        AirBlockFinder finder = new AirBlockFinder(level, pos);
        finder.start();
    }

    public BlockEntity newBlockEntity(BlockPos p_153064_, BlockState p_153065_) {
        return new ReclaimBlockEntity(p_153064_, p_153065_);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState _state, boolean bool) {
        Optional<ReclaimBlockEntity> entity = level.getBlockEntity(pos, RECLAIM_BLOCK_ENTITY.get());
        entity.ifPresent(e -> {
            List<BlockPos> positions = e.getPositions();
            LOGGER.info("Include Blocks: {}", positions.size());
            positions.forEach(p -> level.setBlockAndUpdate(p, Blocks.AIR.defaultBlockState()));
        });
        super.onRemove(state, level, pos, _state, bool);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        drops.add(new ItemStack(RECLAIM_BLOCK.get()));
        return drops;
    }
}
