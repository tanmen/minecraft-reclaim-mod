package work.tanmen.reclaim.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;


public class ReclaimPreviewBlock extends AbstractGlassBlock {
    public ReclaimPreviewBlock() {
        super(Properties.of(Material.AIR)
                .air()
                .noCollission()
                .noOcclusion()
                .isViewBlocking((BlockState state, BlockGetter getter, BlockPos pos) -> false));
    }
}
