package work.tanmen.reclaim.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.MenuType;

import javax.annotation.Nullable;

import static work.tanmen.reclaim.gui.screens.ReclaimScreens.RECLAIM_CONTAINER_TYPE;

public class ReclaimContainerMenu extends AbstractContainerMenu {
    private final Container container;

    public ReclaimContainerMenu(int windowId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(RECLAIM_CONTAINER_TYPE.get(), windowId, new SimpleContainer(9 * 5));
    }

    public ReclaimContainerMenu(int windowId, Inventory playerInv, Container container) {
        this(RECLAIM_CONTAINER_TYPE.get(), windowId, container);
    }

    protected ReclaimContainerMenu(@Nullable MenuType<?> p_38851_, int p_38852_, Container container) {
        super(p_38851_, p_38852_);
        this.container = container;
    }

    @Override
    public boolean stillValid(Player p_39242_) {
        return this.container.stillValid(p_39242_);
    }

    public Container getContainer() {
        return container;
    }
}
