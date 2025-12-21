package committee.nova.avaritia_expand.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import committee.nova.avaritia_expand.client.shader.AERenderTypes;
import committee.nova.mods.avaritia.api.client.model.PerspectiveModelState;
import committee.nova.mods.avaritia.api.client.model.bakedmodels.WrappedItemModel;
import committee.nova.mods.avaritia.api.client.util.TransformUtils;
import committee.nova.mods.avaritia.api.iface.transform.IBowTransform;
import committee.nova.mods.avaritia.api.iface.transform.IToolTransform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class GlowEdgeBakedModel extends WrappedItemModel{
    private final BakedModel parentModel;
    private final int glowColor;
    private final float glowWidth;
    private final float glowOffset;

    public GlowEdgeBakedModel(BakedModel wrapped, int glowColor, float glowWidth, float glowOffset) {
        super(wrapped);
        this.parentModel = wrapped;
        this.glowColor = glowColor;
        this.glowWidth = Math.max(0.5f, glowWidth);
        this.glowOffset = glowOffset;
    }

    public GlowEdgeBakedModel(BakedModel wrapped, int glowColor, float glowWidth) {
        this(wrapped, glowColor, glowWidth, 0.02f);
    }

    @Override
    public @Nullable PerspectiveModelState getModelState() {
        return (PerspectiveModelState) this.parentState;
    }

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext ctx, PoseStack pStack, MultiBufferSource source, int light, int overlay) {
        if (stack.getItem() instanceof IToolTransform) {
            this.parentState = TransformUtils.DEFAULT_TOOL;
        } else if (stack.getItem() instanceof IBowTransform) {
            this.parentState = TransformUtils.DEFAULT_BOW;
        } else{
            this.parentState = TransformUtils.DEFAULT_ITEM;
        }
        this.renderWrapped(stack, pStack, source, light, overlay, true);

        renderGlowEdge(stack, ctx, pStack, source, light, overlay);
    }

    private void renderGlowEdge(ItemStack stack, ItemDisplayContext ctx, PoseStack pStack, MultiBufferSource source, int light, int overlay) {

        VertexConsumer consumer = source.getBuffer(AERenderTypes.GLOWING_OUTLINE);

        PoseStack tempStack = new PoseStack();
        tempStack.mulPose(pStack.last().pose());

        tempStack.translate(0.0f, glowOffset, 0.0f);

        float expansionFactor = 1.0f + (glowWidth * 0.05f);
        tempStack.scale(expansionFactor, expansionFactor, expansionFactor);

        Minecraft.getInstance().getItemRenderer()
                .renderModelLists(parentModel, stack, light, overlay, tempStack, consumer);
    }
}
