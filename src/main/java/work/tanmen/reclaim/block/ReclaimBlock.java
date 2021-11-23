package work.tanmen.reclaim.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

import static work.tanmen.reclaim.block.ReclaimBlocks.RECLAIM_BLOCK;


public class ReclaimBlock extends AbstractGlassBlock {
    public ReclaimBlock() {
        super(BlockBehaviour.Properties.of(Material.GLASS).strength(1f)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isViewBlocking((BlockState state, BlockGetter getter, BlockPos pos) -> false));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        drops.add(new ItemStack(RECLAIM_BLOCK.get()));
        return drops;
    }
}
