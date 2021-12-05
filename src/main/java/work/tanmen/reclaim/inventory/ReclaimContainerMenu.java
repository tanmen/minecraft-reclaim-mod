package work.tanmen.reclaim.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

import static work.tanmen.reclaim.gui.screens.ReclaimScreens.RECLAIM_CONTAINER_TYPE;

public class ReclaimContainerMenu extends AbstractContainerMenu {
    public static final Integer ROW = 5;
    public static final Integer SLOT = 9;
    private final Container container;

    public ReclaimContainerMenu(int windowId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(RECLAIM_CONTAINER_TYPE.get(), windowId, playerInv, new SimpleContainer(ROW * SLOT));
    }

    public ReclaimContainerMenu(int windowId, Inventory playerInv, Container container) {
        this(RECLAIM_CONTAINER_TYPE.get(), windowId, playerInv, container);
    }

    protected ReclaimContainerMenu(@Nullable MenuType<?> p_38851_, int p_38852_, Inventory playerInv, Container container) {
        super(p_38851_, p_38852_);
        this.container = container;
        container.startOpen(playerInv.player);

        int i = (ROW - 4) * 18;
        for (int j = 0; j < ROW; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(container, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInv, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInv, i1, 8 + i1 * 18, 161 + i));
        }
    }


    @Override
    public boolean stillValid(Player p_39242_) {
        return this.container.stillValid(p_39242_);
    }

    public ItemStack quickMoveStack(Player p_39253_, int p_39254_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_39254_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (p_39254_ < ROW * SLOT) {
                if (!this.moveItemStackTo(itemstack1, ROW * SLOT, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, ROW * SLOT, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public void removed(Player p_39251_) {
        super.removed(p_39251_);
        this.container.stopOpen(p_39251_);
    }

    public Container getContainer() {
        return this.container;
    }
}
