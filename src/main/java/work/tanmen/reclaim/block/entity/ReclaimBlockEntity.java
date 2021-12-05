package work.tanmen.reclaim.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import work.tanmen.reclaim.inventory.ReclaimContainerMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReclaimBlockEntity extends RandomizableContainerBlockEntity {
    private List<BlockPos> positions = new ArrayList<>();
    private NonNullList<ItemStack> items = NonNullList.withSize(9 * 5, ItemStack.EMPTY);
    private int required = 0;
    private ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level p_155062_, BlockPos p_155063_, BlockState p_155064_) {
        }

        protected void onClose(Level p_155072_, BlockPos p_155073_, BlockState p_155074_) {
        }

        protected void openerCountChanged(Level p_155066_, BlockPos p_155067_, BlockState p_155068_, int p_155069_, int p_155070_) {
        }

        protected boolean isOwnContainer(Player p_155060_) {
            if (p_155060_.containerMenu instanceof ReclaimContainerMenu) {
                Container container = ((ReclaimContainerMenu) p_155060_.containerMenu).getContainer();
                return container == ReclaimBlockEntity.this;
            } else {
                return false;
            }
        }
    };
    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int p_58431_) {
            switch (p_58431_) {
                case 0:
                    return ReclaimBlockEntity.this.required;
                default:
                    return 0;
            }
        }

        public void set(int p_58433_, int p_58434_) {
            switch (p_58433_) {
                case 0:
                    ReclaimBlockEntity.this.required = p_58434_;
            }

        }

        public int getCount() {
            return 1;
        }
    };

    public ReclaimBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ReclaimBlockEntities.RECLAIM_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }

        ListTag listtag = tag.getList("Positions", 10);
        for (int i = 0; i < listtag.size(); ++i) {
            CompoundTag t = listtag.getCompound(i);
            int[] array = t.getIntArray("Position");
            this.positions.add(new BlockPos(array[0], array[1], array[2]));
        }

        this.required = this.positions.size();
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

        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items);
        }

        return super.save(tag);
    }

    public int getContainerSize() {
        return 9 * 5;
    }

    protected Component getDefaultName() {
        return new TranslatableComponent("container.reclaim");
    }

    public void setPositions(List<BlockPos> positions) {
        this.positions = positions;
        this.required = positions.size();
    }

    public List<BlockPos> getPositions() {
        return this.positions;
    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public Integer getItemCount() {
        return this.items.stream().map(item -> item.getCount()).reduce((accum, value) -> accum + value).get();
    }

    protected void setItems(NonNullList<ItemStack> p_58610_) {
        this.items = p_58610_;
    }

    protected AbstractContainerMenu createMenu(int p_58598_, Inventory p_58599_) {
        return new ReclaimContainerMenu(p_58598_, p_58599_, this, this.dataAccess);
    }

    public void startOpen(Player p_58616_) {
        if (!this.remove && !p_58616_.isSpectator()) {
            this.openersCounter.incrementOpeners(p_58616_, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void stopOpen(Player p_58614_) {
        if (!this.remove && !p_58614_.isSpectator()) {
            this.openersCounter.decrementOpeners(p_58614_, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }
}
