package committee.nova.avaritia_expand.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.client.model.entity.ExtremeWitherModel;
import committee.nova.avaritia_expand.client.model.geo.AEModelLayers;
import committee.nova.avaritia_expand.client.model.layers.ExtremeWitherArmorLayer;
import committee.nova.avaritia_expand.common.entity.ExtremeWitherEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ExtremeWitherRender extends MobRenderer<ExtremeWitherEntity, ExtremeWitherModel<ExtremeWitherEntity>> {


    private static final ResourceLocation WITHER_INVULNERABLE_LOCATION = AvaritiaExpand.rl("textures/entity/extreme_wither/wither_sprite.png");
    private static final ResourceLocation WITHER_LOCATION = AvaritiaExpand.rl("textures/entity/extreme_wither/extreme_wither.png");

    public ExtremeWitherRender(EntityRendererProvider.Context context) {
        super(context, new ExtremeWitherModel<>(context.bakeLayer(AEModelLayers.EXTREME_WITHER)), 1.0F);
        this.addLayer(new ExtremeWitherArmorLayer(this, context.getModelSet()));
    }

    protected int getBlockLightLevel(ExtremeWitherEntity entity, BlockPos pos){
        return 15;
    }

    public ResourceLocation getTextureLocation(ExtremeWitherEntity entity) {

        if (entity.isInSpawnAnimation() || entity.getInvulnerableTicks() > 0) {
            int i = entity.getInvulnerableTicks();
            return i > 0 && (i > 80 || i / 5 % 2 != 1) ? WITHER_INVULNERABLE_LOCATION : WITHER_INVULNERABLE_LOCATION;
        }
        return WITHER_LOCATION;
    }

    protected void scale(ExtremeWitherEntity livingEntity, PoseStack poseStack, float partialTickTime){
        float f = 2.0F;
        if (livingEntity.isInSpawnAnimation()) {
            float progress = livingEntity.getSpawnAnimationProgress();
            f = 0.5F + progress * 1.5F;
        } else {
            int i = livingEntity.getInvulnerableTicks();
            if (i > 0) {
                f -= ((float)i - partialTickTime) / 220.0F * 0.5F;
            }
        }

        poseStack.scale(f, f, f);
    }
}
