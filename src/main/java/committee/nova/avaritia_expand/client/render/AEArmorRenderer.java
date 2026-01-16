package committee.nova.avaritia_expand.client.render;

import committee.nova.avaritia_expand.AvaritiaExpand;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtil;

public class AEArmorRenderer<T extends Item & GeoItem> extends GeoArmorRenderer<T> {

    protected GeoBone waist;

    public AEArmorRenderer(String path) {
        super(new DefaultedGeoModel<>(AvaritiaExpand.rl(path)) {
            @Override
            protected String subtype() {
                return "armor";
            }
        });
    }

    public GeoBone getWaist(GeoModel<T> model) {
        return model.getBone("armorWaist").orElse(null);
    }

    @Override
    protected void grabRelevantBones(BakedGeoModel bakedModel) {
        super.grabRelevantBones(bakedModel);
        this.waist = getWaist(getGeoModel());
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        super.applyBoneVisibilityBySlot(currentSlot);
        HumanoidModel<?> model = this;
        if (currentSlot == EquipmentSlot.LEGS) {
            setBoneVisible(waist, model.body.visible);
        }
    }

    @Override
    public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, HumanoidModel<?> model) {
        super.applyBoneVisibilityByPart(currentSlot, currentPart, model);
        if (currentSlot == EquipmentSlot.LEGS && currentPart == model.body) {
            setBoneVisible(waist, true);
        }
    }

    @Override
    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        super.applyBaseTransformations(baseModel);
        if (waist != null) {
            ModelPart bodyPart = baseModel.body;
            RenderUtil.matchModelPartRot(bodyPart, waist);
            waist.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
        }
    }

    @Override
    protected void setAllBonesVisible(boolean visible) {
        super.setAllBonesVisible(visible);
        setBoneVisible(waist, visible);
    }
}
