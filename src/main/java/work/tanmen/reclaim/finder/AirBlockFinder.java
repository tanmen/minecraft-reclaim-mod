package work.tanmen.reclaim.finder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static work.tanmen.reclaim.block.ReclaimBlocks.RECLAIM_PREVIEW_BLOCK;

public class AirBlockFinder extends Thread {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Level level;
    private final BlockPos pos;
    private final List<BlockPos> searched = new ArrayList<>();
    private final Integer max;
    private Integer count = 0;

    public AirBlockFinder(Level level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
        this.max = 64 * 10 * 3;
    }

    public AirBlockFinder(Level level, BlockPos pos, Integer max) {
        this.level = level;
        this.pos = pos;
        this.max = max;
    }

    @Override
    public void run() {
        List<BlockPos> positions = searchAirs(this.pos);

        LOGGER.info("Airs: {}", (long) positions.size());

        positions.forEach(pos -> level.setBlock(pos, RECLAIM_PREVIEW_BLOCK.get().defaultBlockState(), 0));
    }

    private List<BlockPos> searchAirs(BlockPos pos) {
        List<BlockPos> positions = searchHorizonAirs(pos);
        List<BlockPos> abovePoses = positions.stream()
                .flatMap(p -> searchHorizonAirs(p.above(-1)).stream())
                .collect(Collectors.toList());

        positions.addAll(abovePoses);

        if (abovePoses.size() > 0 && this.count < this.max) {
            abovePoses.forEach(p -> positions.addAll(searchAirs(p.above(-1))));
        }

        return positions;
    }

    private List<BlockPos> searchHorizonAirs(BlockPos pos) {
        List<BlockPos> airs = findAirs(pos);
        List<BlockPos> positions = new ArrayList<>(airs);

        this.count = this.count + airs.size();

        if (airs.size() > 0 && this.count < this.max) {
            airs.forEach(air -> positions.addAll(searchHorizonAirs(air)));
        }
        return positions;
    }

    private List<BlockPos> findAirs(BlockPos pos) {
        return Stream.of(pos, pos.north(1), pos.west(1), pos.south(1), pos.east(1))
                .filter(p -> !this.searched.contains(p))
                .filter(this::isAir)
                .peek(this.searched::add)
                .collect(Collectors.toList());
    }

    private boolean isAir(BlockPos p) {
        BlockState state = level.getBlockState(p);
        return state.is(Blocks.AIR) ||
                state.is(Blocks.CAVE_AIR) ||
                state.is(Blocks.VOID_AIR) ||
                state.is(Blocks.GRASS);

    }
}
