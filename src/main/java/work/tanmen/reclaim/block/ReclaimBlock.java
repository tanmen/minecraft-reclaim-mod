package work.tanmen.reclaim.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;


public class ReclaimBlock extends AbstractGlassBlock {
    public ReclaimBlock() {
        super(BlockBehaviour.Properties.of(Material.GLASS).strength(1f)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isViewBlocking((BlockState state, BlockGetter getter, BlockPos pos) -> false));
    }
}
