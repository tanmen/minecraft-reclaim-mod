package work.tanmen.reclaim.gui.screens;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import work.tanmen.reclaim.gui.screens.inventory.ReclaimScreen;
import work.tanmen.reclaim.inventory.ReclaimContainerMenu;

import static work.tanmen.reclaim.ReclaimMod.MOD_ID;

public class ReclaimScreens {
    private static final DeferredRegister<MenuType<?>> SCREENS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);

    public static final RegistryObject<MenuType<ReclaimContainerMenu>> RECLAIM_CONTAINER_TYPE =
            SCREENS.register("reclaim", () -> IForgeContainerType.create(ReclaimContainerMenu::new));

    public static void register(IEventBus eventBus) {
        SCREENS.register(eventBus);
    }

    public static void enqueueWork() {
        MenuScreens.register(RECLAIM_CONTAINER_TYPE.get(), ReclaimScreen::new);
    }
}
