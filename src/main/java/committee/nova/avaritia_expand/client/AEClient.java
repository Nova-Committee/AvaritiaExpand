package committee.nova.avaritia_expand.client;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.client.screen.AEConfigScreen;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalArmorItem;
import committee.nova.avaritia_expand.init.registry.AEEntities;
import net.minecraft.client.model.ArmorStandModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = AvaritiaExpand.MOD_ID, value = Dist.CLIENT)
public class AEClient {

    @SubscribeEvent
    public static void clientSetUp(FMLClientSetupEvent event) {
        ModList.get().getModContainerById(AvaritiaExpand.MOD_ID).orElseThrow().registerExtensionPoint(IConfigScreenFactory.class,
                (container, last) -> new AEConfigScreen(last));
        AEEntities.onClientSetup();
    }

    /**
     * Adapted from <a href="https://github.com/mekanism/Mekanism">Mekanism</a>
     */
    @SubscribeEvent
    public static void renderEntityPre(RenderLivingEvent.Pre<?, ?> event) {
        if (event.getRenderer().getModel() instanceof HumanoidModel<?> humanoidModel) {
            // If the entity has a biped model, then see if it is wearing a crystal armor,
            // in which case we want to hide various parts of the model
            setModelVisibility(event.getEntity(), humanoidModel, false);
        }
    }

    /**
     * Adapted from <a href="https://github.com/mekanism/Mekanism">Mekanism</a>
     */
    @SubscribeEvent
    public static void renderEntityPost(RenderLivingEvent.Post<?, ?> event) {
        if (event.getRenderer().getModel() instanceof HumanoidModel<?> humanoidModel) {
            // Undo model visibility changes we made to ensure that other entities of the same type are properly visible
            setModelVisibility(event.getEntity(), humanoidModel, true);
        }
    }

    /**
     * Adapted from <a href="https://github.com/mekanism/Mekanism">Mekanism</a>
     */
    private static void setModelVisibility(LivingEntity entity, HumanoidModel<?> entityModel, boolean showModel) {
        Item helmet = entity.getItemBySlot(EquipmentSlot.HEAD).getItem();
        Item chest = entity.getItemBySlot(EquipmentSlot.CHEST).getItem();
        Item legs = entity.getItemBySlot(EquipmentSlot.LEGS).getItem();

        if (helmet instanceof CrystalArmorItem) {
            entityModel.head.visible = showModel;
            entityModel.hat.visible = showModel;
            if (entityModel instanceof PlayerModel<?> playerModel) {
                playerModel.ear.visible = showModel;
            }
        }
        if (chest instanceof CrystalArmorItem) {
            entityModel.body.visible = showModel;
            if (entityModel instanceof ArmorStandModel armorStandModel) {
                armorStandModel.rightBodyStick.visible = showModel;
                armorStandModel.leftBodyStick.visible = showModel;
                armorStandModel.shoulderStick.visible = showModel;
            } else {
                // Don't adjust arms for armor stands as the model will end up changing them anyway,
                // and then we may incorrectly activate them
                entityModel.leftArm.visible = showModel;
                entityModel.rightArm.visible = showModel;
                if (entityModel instanceof PlayerModel<?> playerModel) {
                    playerModel.cloak.visible = showModel;
                    playerModel.jacket.visible = showModel;
                    playerModel.leftSleeve.visible = showModel;
                    playerModel.rightSleeve.visible = showModel;
                }
            }
        }
        if (legs instanceof CrystalArmorItem) {
            entityModel.leftLeg.visible = showModel;
            entityModel.rightLeg.visible = showModel;
            if (entityModel instanceof PlayerModel<?> playerModel) {
                playerModel.leftPants.visible = showModel;
                playerModel.rightPants.visible = showModel;
            }
        }
    }
}