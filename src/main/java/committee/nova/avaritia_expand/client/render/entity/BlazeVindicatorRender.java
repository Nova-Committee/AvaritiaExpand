package committee.nova.avaritia_expand.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.client.model.geo.AEModelLayers;
import committee.nova.avaritia_expand.common.entity.BlazeVindicatorEntity;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlazeVindicatorRender extends IllagerRenderer<BlazeVindicatorEntity> {
    private static final ResourceLocation VINDICATOR = AvaritiaExpand.rl("textures/entity/blaze_vindicator/blaze_vindicator.png");

    public BlazeVindicatorRender(EntityRendererProvider.Context p_174439_) {
        super(p_174439_, new IllagerModel<>(p_174439_.bakeLayer(AEModelLayers.BLAZE_VINDICATOR)), 0.5F);
        this.addLayer(
                new ItemInHandLayer<BlazeVindicatorEntity, IllagerModel<BlazeVindicatorEntity>>(this, p_174439_.getItemInHandRenderer()) {
                    public void render(
                            PoseStack p_116352_,
                            MultiBufferSource p_116353_,
                            int p_116354_,
                            BlazeVindicatorEntity p_116355_,
                            float p_116356_,
                            float p_116357_,
                            float p_116358_,
                            float p_116359_,
                            float p_116360_,
                            float p_116361_
                    ) {
                        if (p_116355_.isAggressive()) {
                            super.render(p_116352_, p_116353_, p_116354_, p_116355_, p_116356_, p_116357_, p_116358_, p_116359_, p_116360_, p_116361_);
                        }
                    }
                }
        );
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(BlazeVindicatorEntity entity) {
        return VINDICATOR;
    }
}
