package committee.nova.avaritia_expand.client.screen;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.menu.BurnerMenu;
import committee.nova.mods.avaritia.api.client.screen.BaseContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class BurnerScreen extends BaseContainerScreen<BurnerMenu> {

    private static final ResourceLocation BACKGROUND = AvaritiaExpand.rl( "textures/gui/blaze_portable_burner_gui.png");

    public BurnerScreen(BurnerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, BACKGROUND, 256, 256, 256, 256);
        this.inventoryLabelY = 112;
        this.titleLabelY = 99999;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

}