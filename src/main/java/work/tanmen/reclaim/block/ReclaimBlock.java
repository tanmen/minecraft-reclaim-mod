package work.tanmen.reclaim.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;


public class ReclaimBlock extends Block {
    public ReclaimBlock() {
        super(BlockBehaviour.Properties.of(Material.AIR).strength(0f));
    }
}
