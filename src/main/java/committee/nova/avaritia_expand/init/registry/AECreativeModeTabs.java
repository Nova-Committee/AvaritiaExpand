package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AECreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AvaritiaExpand.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = TABS.register("expand_group", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tab.avaritia_expand"))
            .icon(Items.NETHERITE_INGOT::getDefaultInstance)
            .displayItems((parameters, output) -> {
                output.accept(AEItems.neutron_sword.get());
            })
            .build());
    public static void register(IEventBus bus){
        TABS.register(bus);
    }
}
