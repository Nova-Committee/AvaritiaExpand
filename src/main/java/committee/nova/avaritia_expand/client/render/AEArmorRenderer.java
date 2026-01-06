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

    protected GeoBone wrist;

    public AEArmorRenderer(String path) {
        super(new DefaultedGeoModel<>(AvaritiaExpand.rl(path)) {
            @Override
            protected String subtype() {
                return "armor";
            }
        });
    }

    public GeoBone getWrist(GeoModel<T> model) {
        return model.getBone("armorWrist").orElse(null);
    }

    @Override
    protected void grabRelevantBones(BakedGeoModel bakedModel) {
        super.grabRelevantBones(bakedModel);
        this.wrist = getWrist(getGeoModel());
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        super.applyBoneVisibilityBySlot(currentSlot);
        HumanoidModel<?> model = this;
        if (currentSlot == EquipmentSlot.LEGS) {
            setBoneVisible(wrist, model.body.visible);
        }
    }

    @Override
    public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, HumanoidModel<?> model) {
        super.applyBoneVisibilityByPart(currentSlot, currentPart, model);
        if (currentSlot == EquipmentSlot.LEGS && currentPart == model.body) {
            setBoneVisible(wrist, true);
        }
    }

    @Override
    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        super.applyBaseTransformations(baseModel);
        if (wrist != null) {
            ModelPart bodyPart = baseModel.body;
            RenderUtil.matchModelPartRot(bodyPart, wrist);
            wrist.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
        }
    }

    @Override
    protected void setAllBonesVisible(boolean visible) {
        super.setAllBonesVisible(visible);
        setBoneVisible(wrist, visible);
    }
}
