package committee.nova.avaritia_expand.client.model.entity;

import committee.nova.avaritia_expand.common.entity.ExtremeWitherEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ExtremeWitherModel<T extends ExtremeWitherEntity> extends HierarchicalModel<T> {

    private static final String RIBCAGE = "ribcage";
    private static final String CENTER_HEAD = "center_head";
    private static final String RIGHT_HEAD = "right_head";
    private static final String LEFT_HEAD = "left_head";
    private static final float RIBCAGE_X_ROT_OFFSET = 0.065F;
    private static final float TAIL_X_ROT_OFFSET = 0.265F;
    private final ModelPart root;
    private final ModelPart centerHead;
    private final ModelPart rightHead;
    private final ModelPart leftHead;
    private final ModelPart ribcage;
    private final ModelPart tail;

    public ExtremeWitherModel(ModelPart root) {
        this.root = root;
        this.ribcage = root.getChild("ribcage");
        this.tail = root.getChild("tail");
        this.centerHead = root.getChild("center_head");
        this.rightHead = root.getChild("right_head");
        this.leftHead = root.getChild("left_head");
    }

    public static LayerDefinition createBodyLayer() {
        CubeDeformation cubeDeformation = CubeDeformation.NONE;

        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "shoulders",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-10.0F, 3.9F, -0.5F, 20.0F, 3.0F, 3.0F, cubeDeformation),
                PartPose.ZERO
        );

        float f = 0.20420352F;

        partdefinition.addOrReplaceChild(
                "ribcage",
                CubeListBuilder.create()
                        .texOffs(0, 22)
                        .addBox(0,0,0,3,10,3, cubeDeformation)
                        .texOffs(24,22).addBox(-4,1.5F,0.5F,11,2,2, cubeDeformation)
                        .texOffs(24,22).addBox(-4,4F,0.5F,11,2,2, cubeDeformation)
                        .texOffs(24,22).addBox(-4,6.5F,0.5F,11,2,2, cubeDeformation),
                PartPose.offsetAndRotation(-2F, 6.9F, -0.5F, f,0,0)
        );

        partdefinition.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(12,22)
                        .addBox(0,0,0,3,6,3, cubeDeformation),
                PartPose.offsetAndRotation(
                        -2F,
                        6.9F + Mth.cos(f) * 10F,
                        -0.5F + Mth.sin(f) * 10F,
                        0.83252203F,0,0
                )
        );

        partdefinition.addOrReplaceChild(
                "center_head",
                CubeListBuilder.create().texOffs(0,0)
                        .addBox(-4,-4,-4,8,8,8, cubeDeformation),
                PartPose.ZERO
        );

        CubeListBuilder head = CubeListBuilder.create().texOffs(32,0)
                .addBox(-4,-4,-4,6,6,6, cubeDeformation);

        partdefinition.addOrReplaceChild("right_head", head, PartPose.offset(-8,4,0));
        partdefinition.addOrReplaceChild("left_head", head, PartPose.offset(10,4,0));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Sets this entity's model rotation angles
     */
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = Mth.cos(ageInTicks * 0.1F);
        this.ribcage.xRot = (0.065F + 0.05F * f) * (float) Math.PI;
        this.tail.setPos(-2.0F, 6.9F + Mth.cos(this.ribcage.xRot) * 10.0F, -0.5F + Mth.sin(this.ribcage.xRot) * 10.0F);
        this.tail.xRot = (0.265F + 0.1F * f) * (float) Math.PI;
        this.centerHead.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        this.centerHead.xRot = headPitch * (float) (Math.PI / 180.0);
    }

    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        setupHeadRotation(entity, this.rightHead, 0);
        setupHeadRotation(entity, this.leftHead, 1);
    }

    private static <T extends ExtremeWitherEntity> void setupHeadRotation(T wither, ModelPart part, int head) {
        part.yRot = (wither.getHeadYRot(head) - wither.yBodyRot) * (float) (Math.PI / 180.0);
        part.xRot = wither.getHeadXRot(head) * (float) (Math.PI / 180.0);
    }
}
