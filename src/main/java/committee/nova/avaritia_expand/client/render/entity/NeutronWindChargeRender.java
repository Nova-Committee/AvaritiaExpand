package committee.nova.avaritia_expand.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.entity.NeutronWindCharge;
import net.minecraft.client.model.WindChargeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NeutronWindChargeRender extends EntityRenderer<NeutronWindCharge> {

    private static final float MIN_CAMERA_DISTANCE_SQUARED = Mth.square(3.5F);
    private static final ResourceLocation TEXTURE_LOCATION = AvaritiaExpand.rl("textures/entity/wind_charge/neutron_wind_charge.png");
    private final WindChargeModel model;

    public NeutronWindChargeRender(EntityRendererProvider.Context context) {
        super(context);
        this.model = new WindChargeModel(context.bakeLayer(ModelLayers.WIND_CHARGE));
    }
    public void render(NeutronWindCharge entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < (double)MIN_CAMERA_DISTANCE_SQUARED)) {
            float f = (float)entity.tickCount + partialTick;
            VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.breezeWind(TEXTURE_LOCATION, this.xOffset(f) % 1.0F, 0.0F));
            this.model.setupAnim(entity, 0.0F, 0.0F, f, 0.0F, 0.0F);
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        }
    }

    protected float xOffset(float tickCount) {
        return tickCount * 0.03F;
    }

    public ResourceLocation getTextureLocation(NeutronWindCharge entity) {
        return TEXTURE_LOCATION;
    }
}
