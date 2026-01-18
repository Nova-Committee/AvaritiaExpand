package committee.nova.avaritia_expand.init.handler;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.item.tool.neutron.NeutronMobileItem;
import committee.nova.avaritia_expand.init.registry.AEItems;
import committee.nova.mods.avaritia.Const;
import committee.nova.mods.avaritia.api.iface.item.ISwitchable;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = AvaritiaExpand.MOD_ID, value = Dist.CLIENT)
public class AEItemOverrideHandler {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(AEItems.neutron_mobile.get(),
                    Const.rl("mode"),
                    (stack, world, entity, seed) -> {
                        return ISwitchable.getCurrentMode(stack, NeutronMobileItem.MODES);
                    });
        });
    }
    public static void setPropertyOverride(Item itemProvider, ResourceLocation override, ItemPropertyFunction propertyGetter) {
        ItemProperties.register(itemProvider.asItem(), override, propertyGetter);
    }
}
