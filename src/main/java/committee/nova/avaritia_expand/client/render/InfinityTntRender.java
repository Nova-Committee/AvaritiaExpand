package committee.nova.avaritia_expand.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import committee.nova.avaritia_expand.common.entity.InfinityTntEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class InfinityTntRender extends EntityRenderer<InfinityTntEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public InfinityTntRender(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0F;
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(InfinityTntEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.5F, 0.0F);
        int i = entity.getFuse();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.translate(-0.5F, -0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, entity.getBlockState(), poseStack, buffer, packedLight, i / 5 % 2 == 0);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

    }


    @Override
    public ResourceLocation getTextureLocation(InfinityTntEntity infinityTntEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
