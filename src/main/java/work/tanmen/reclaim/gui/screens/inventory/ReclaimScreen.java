package work.tanmen.reclaim.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.tanmen.reclaim.inventory.ReclaimContainerMenu;

import static work.tanmen.reclaim.ReclaimMod.MOD_ID;
import static work.tanmen.reclaim.inventory.ReclaimContainerMenu.ROW;

@OnlyIn(Dist.CLIENT)
public class ReclaimScreen extends AbstractContainerScreen<ReclaimContainerMenu> implements MenuAccess<ReclaimContainerMenu> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/reclaim_table.png");

    public ReclaimScreen(ReclaimContainerMenu p_98409_, Inventory p_98410_, Component p_98411_) {
        super(p_98409_, p_98410_, p_98411_);
        this.passEvents = false;
        this.imageHeight = 114 + ROW * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new ImageButton(this.leftPos + 149, this.topPos + 4, 20, 12, 0, 224, 13, CONTAINER_BACKGROUND, (p_98484_) -> {
            this.menu.executeReclaim();
            this.onClose();
        }));
    }

    public void render(PoseStack p_98418_, int p_98419_, int p_98420_, float p_98421_) {
        this.renderBackground(p_98418_);
        super.render(p_98418_, p_98419_, p_98420_, p_98421_);

        int count = 0;

        Container container = this.menu.getContainer();
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack item = container.getItem(i);
            if (item.is(Items.AIR)) {
                continue;
            }
            count += item.getCount();
        }

        this.font.draw(p_98418_,
                String.format("%d/%d", count, this.menu.getPositionCount()),
                this.leftPos + 85 - Integer.toString(count).length() * 6,
                this.topPos + 7,
                4210752);
        this.renderTooltip(p_98418_, p_98419_, p_98420_);
    }

    protected void renderBg(PoseStack p_98413_, float p_98414_, int p_98415_, int p_98416_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(p_98413_, i, j, 0, 0, this.imageWidth, ROW * 18 + 17);
        this.blit(p_98413_, i, j + ROW * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}
