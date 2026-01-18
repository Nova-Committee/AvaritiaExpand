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
            .icon(AEItems.infinity_bottle.get()::getDefaultInstance)
            .displayItems((parameters, output) -> {
                output.accept(AEItems.neutron_sword.get());
                output.accept(AEItems.neutron_axe.get());
                output.accept(AEItems.neutron_pickaxe.get());
                output.accept(AEItems.neutron_shovel.get());
                output.accept(AEItems.neutron_hoe.get());
                output.accept(AEItems.neutron_mobile.get());
                output.accept(AEItems.star_dessert.get());
                output.accept(AEItems.singularity_stew.get());
                output.accept(AEItems.infinity_bottle.get());
                output.accept(AEItems.infinity_shears.get());
                output.accept(AEItems.blaze_helmet.get());
                output.accept(AEItems.blaze_chestplate.get());
                output.accept(AEItems.blaze_leggings.get());
                output.accept(AEItems.blaze_boots.get());
                output.accept(AEItems.crystal_helmet.get());
                output.accept(AEItems.crystal_chestplate.get());
                output.accept(AEItems.crystal_leggings.get());
                output.accept(AEItems.crystal_boots.get());
                output.accept(AEBlocks.neutron_decompose.get());
                output.accept(AEBlocks.infinity_tnt_block.get());
                output.accept(AEBlocks.jeb_wool.get());
                output.accept(AEBlocks.jeb_carpet.get());
            })
            .build());
    public static void register(IEventBus bus){
        TABS.register(bus);
    }
}
