package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AEBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AvaritiaExpand.MOD_ID);


    private static <T extends Block> DeferredBlock<T> itemBlock(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        itemBlock(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> void itemBlock(String name, DeferredBlock<T> block){
        AEItems.ITEMS.register(name,()-> new BlockItem(block.get(),new Item.Properties()));
    }
    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
}
