package committee.nova.avaritia_expand.client.model.layers;

import committee.nova.avaritia_expand.client.model.entity.ExtremeWitherModel;
import committee.nova.avaritia_expand.client.model.geo.AEModelLayers;
import committee.nova.avaritia_expand.common.entity.ExtremeWitherEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ExtremeWitherArmorLayer extends EnergySwirlLayer<ExtremeWitherEntity, ExtremeWitherModel<ExtremeWitherEntity>> {

    private static final ResourceLocation WITHER_ARMOR_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/wither/wither_armor.png");
    private final ExtremeWitherModel<ExtremeWitherEntity> model;

    public ExtremeWitherArmorLayer(RenderLayerParent<ExtremeWitherEntity, ExtremeWitherModel<ExtremeWitherEntity>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new ExtremeWitherModel<>(modelSet.bakeLayer(AEModelLayers.EXTREME_WITHER_ARMOR));
    }

    @Override
    protected float xOffset(float tickCount) {
        return Mth.cos(tickCount * 0.02F) * 3.0F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return WITHER_ARMOR_LOCATION;
    }

    @Override
    protected EntityModel<ExtremeWitherEntity> model() {
        return this.model;
    }
}