package committee.nova.avaritia_expand.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import committee.nova.avaritia_expand.client.shader.AERenderTypes;
import committee.nova.mods.avaritia.api.client.model.bakedmodels.WrappedItemModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class GlowBakeModel extends WrappedItemModel {

    public GlowBakeModel(BakedModel wrapped) {
        super(wrapped);
    }

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext transformType, PoseStack pStack,
                           MultiBufferSource source, int packedLight, int packedOverlay) {

        if (source instanceof MultiBufferSource.BufferSource bs) {
            bs.endBatch();
        }

        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, null, null, 0);
        VertexConsumer glowConsumer = source.getBuffer(AERenderTypes.GLOW_SHADER_TYPE);

        pStack.pushPose();

        pStack.scale(1.01f, 1.01f, 1.01f);

        Minecraft.getInstance().getItemRenderer().renderModelLists(
                model, stack, 0xF00000, OverlayTexture.NO_OVERLAY, pStack, glowConsumer
        );

        pStack.popPose();
    }
}
