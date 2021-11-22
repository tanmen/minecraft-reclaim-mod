package work.tanmen.reclaim;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import work.tanmen.reclaim.block.ReclaimBlocks;
import work.tanmen.reclaim.item.ReclaimItems;


@Mod(ReclaimMod.MOD_ID)
public class ReclaimMod {
    public static final String MOD_ID = "reclaim";
//    private static final Logger LOGGER = LogManager.getLogger();

    public ReclaimMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ReclaimBlocks.register(bus);
        ReclaimItems.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
