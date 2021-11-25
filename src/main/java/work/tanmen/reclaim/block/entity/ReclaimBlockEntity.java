package work.tanmen.reclaim.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReclaimBlockEntity extends BlockEntity {
    private List<BlockPos> positions = new ArrayList<>();

    public ReclaimBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ReclaimBlockEntities.RECLAIM_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ListTag listtag = tag.getList("Positions", 10);
        for (int i = 0; i < listtag.size(); ++i) {
            CompoundTag t = listtag.getCompound(i);
            int[] array = t.getIntArray("Position");
            this.positions.add(new BlockPos(array[0], array[1], array[2]));
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag tags = new ListTag();
        this.positions.forEach(pos -> {
            CompoundTag t = new CompoundTag();
            t.putIntArray("Position", Arrays.asList(pos.getX(), pos.getY(), pos.getZ()));
            tags.add(t);
        });

        tag.put("Positions", tags);
        return super.save(tag);
    }

    public void setPositions(List<BlockPos> positions) {
        this.positions = positions;
    }

    public List<BlockPos> getPositions() {
        return this.positions;
    }
}
