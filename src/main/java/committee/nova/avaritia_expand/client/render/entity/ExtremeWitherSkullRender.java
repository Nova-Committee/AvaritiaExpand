package committee.nova.avaritia_expand.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.client.model.entity.ExtremeWitherSkullModel;
import committee.nova.avaritia_expand.client.model.geo.AEModelLayers;
import committee.nova.avaritia_expand.common.entity.ExtremeWitherSkull;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ExtremeWitherSkullRender extends EntityRenderer<ExtremeWitherSkull> {
    private static final ResourceLocation WITHER_INVULNERABLE_LOCATION = AvaritiaExpand.rl("textures/entity/extreme_wither/wither_sprite.png");
    private static final ResourceLocation WITHER_LOCATION = AvaritiaExpand.rl("textures/entity/extreme_wither/extreme_wither.png");
    private final ExtremeWitherSkullModel model;


    public ExtremeWitherSkullRender(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ExtremeWitherSkullModel(context.bakeLayer(AEModelLayers.EXTREME_WITHER_SKULL));
    }

    public static LayerDefinition createSkullLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 35).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    protected int getBlockLightLevel(ExtremeWitherSkull entity, BlockPos pos) {
        return 15;
    }

    public void render(ExtremeWitherSkull entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        float f = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float f1 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.setupAnim(0.0F, f, f1);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(ExtremeWitherSkull entity) {
        return entity.isDangerous() ? WITHER_INVULNERABLE_LOCATION : WITHER_LOCATION;
    }
}
