package work.tanmen.reclaim;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.tanmen.reclaim.block.ReclaimBlocks;
import work.tanmen.reclaim.block.entity.ReclaimBlockEntities;
import work.tanmen.reclaim.gui.screens.ReclaimScreens;
import work.tanmen.reclaim.item.ReclaimItems;


@Mod(ReclaimMod.MOD_ID)
public class ReclaimMod {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "reclaim";

    public ReclaimMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ReclaimBlocks.register(bus);
        ReclaimItems.register(bus);
        ReclaimBlockEntities.register(bus);
        ReclaimScreens.register(bus);

        bus.addListener(ReclaimBlocks::registerRender);

        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueWork);
    }

    private void enqueueWork(final InterModEnqueueEvent event) {
        ReclaimScreens.enqueueWork();
    }
}
