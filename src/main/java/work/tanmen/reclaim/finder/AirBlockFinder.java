package work.tanmen.reclaim.finder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.tanmen.reclaim.block.entity.ReclaimBlockEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static work.tanmen.reclaim.block.ReclaimBlocks.RECLAIM_PREVIEW_BLOCK;
import static work.tanmen.reclaim.block.entity.ReclaimBlockEntities.RECLAIM_BLOCK_ENTITY;

public class AirBlockFinder extends Thread {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Level level;
    private final Optional<ReclaimBlockEntity> entity;
    private final BlockPos pos;
    private final List<BlockPos> searched = new ArrayList<>();
    private final Integer max;
    private Integer count = 0;

    public AirBlockFinder(Level level, BlockPos pos) {
        this(level, pos, 64 * 9 * 5);
    }

    public AirBlockFinder(Level level, BlockPos pos, Integer max) {
        this.level = level;
        this.pos = pos;
        this.max = max;
        this.entity = this.level.getBlockEntity(this.pos, RECLAIM_BLOCK_ENTITY.get());
        LOGGER.info("Entity is {}", this.entity.isPresent() ? "found" : "not found....");
    }

    @Override
    public void run() {
        List<BlockPos> positions = searchAirs(this.pos);

        LOGGER.info("Airs: {}", (long) positions.size());

        this.entity.ifPresent(e -> e.setPositions(positions));
        positions.forEach(pos -> level.setBlock(pos, RECLAIM_PREVIEW_BLOCK.get().defaultBlockState(), 0));
    }

    private List<BlockPos> searchAirs(BlockPos pos) {
        if (this.count >= this.max) {
            return Collections.emptyList();
        }
        List<BlockPos> positions = searchHorizonAirs(pos);
        List<BlockPos> abovePoses = positions.stream()
                .flatMap(p -> {
                    if (this.count >= this.max) {
                        return Stream.empty();
                    }
                    return searchHorizonAirs(p.above(-1)).stream();
                })
                .collect(Collectors.toList());

        positions.addAll(abovePoses);

        if (abovePoses.size() > 0 && this.count < this.max) {
            abovePoses.forEach(p -> positions.addAll(searchAirs(p.above(-1))));
        }

        return positions;
    }

    private List<BlockPos> searchHorizonAirs(BlockPos pos) {
        List<BlockPos> airs = findAirs(pos);

        this.count = this.count + airs.size();
        int over = this.count - this.max;

        List<BlockPos> positions = new ArrayList<>(over == airs.size()
                ? Collections.emptyList()
                : over > 0
                ? airs.subList(0, airs.size() - over)
                : airs);

        if (airs.size() > 0 && this.count < this.max) {
            airs.forEach(air -> {
                if (this.count < this.max) {
                    positions.addAll(searchHorizonAirs(air));
                }
            });
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
