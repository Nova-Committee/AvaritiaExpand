package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AEItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(    AvaritiaExpand.MOD_ID);

        public static DeferredItem<Item> neutron_sword = ITEMS.register("neutron_sword",()-> new SwordItem(Tiers.DIAMOND,new Item.Properties().stacksTo(1)));


    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
