package work.tanmen.reclaim.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import work.tanmen.reclaim.finder.AirBlockFinder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static work.tanmen.reclaim.block.ReclaimBlocks.RECLAIM_BLOCK;


public class ReclaimBlock extends AbstractGlassBlock {
    public List<BlockPos> positions = new ArrayList<>();

    public ReclaimBlock() {
        super(BlockBehaviour.Properties.of(Material.GLASS).strength(1f)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isViewBlocking((BlockState state, BlockGetter getter, BlockPos pos) -> false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {

        return super.use(state, level, pos, player, hand, result);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState place = super.getStateForPlacement(context);
        Level getter = context.getLevel();
        BlockPos pos = context.getClickedPos();
        AirBlockFinder finder = new AirBlockFinder(getter, pos);
        finder.start();
        return place;
    }

    @Override
    public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
        super.destroy(accessor, pos, state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        drops.add(new ItemStack(RECLAIM_BLOCK.get()));
        return drops;
    }

    public void setPositions(List<BlockPos> positions) {
        this.positions = positions;
    }
}
