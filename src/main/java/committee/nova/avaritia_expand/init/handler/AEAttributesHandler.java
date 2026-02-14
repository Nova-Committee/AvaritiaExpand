package committee.nova.avaritia_expand.init.handler;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.entity.ExtremeWitherEntity;
import committee.nova.avaritia_expand.init.registry.AEEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = AvaritiaExpand.MOD_ID)
public class AEAttributesHandler {

    @SubscribeEvent
    public static void addAttributes(EntityAttributeCreationEvent event) {
        event.put(AEEntities.EXTREME_WITHER.get(), ExtremeWitherEntity.createAttributes().build());
    }
}
