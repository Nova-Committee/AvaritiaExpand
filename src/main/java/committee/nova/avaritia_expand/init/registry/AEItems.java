package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AEItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems( AvaritiaExpand.MOD_ID);

        public static final DeferredItem<Item> test = ITEMS.register("test",()-> new Item(new Item.Properties().stacksTo(2)));


    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
