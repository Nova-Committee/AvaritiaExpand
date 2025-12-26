package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.block.InfinityTntBlock;
import committee.nova.avaritia_expand.common.block.JebCarpetBlock;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AEBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AvaritiaExpand.MOD_ID);


    public static DeferredBlock<Block> infinity_tnt_block = itemBlock("infinity_tnt_block", () -> new InfinityTntBlock(BlockBehaviour.Properties.of()),ModRarities.COSMIC.getValue());
    public static DeferredBlock<Block> jeb_wool = itemBlock("jeb_wool", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static DeferredBlock<Block> jeb_carpet = itemBlock("jeb_carpet", () -> new JebCarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CARPET)));



    private static <T extends Block> DeferredBlock<T> itemBlock(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        itemBlock(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> DeferredBlock<T> itemBlock(String name, Supplier<T> block, Rarity rarity) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        itemBlock(name, toReturn, rarity);
        return toReturn;
    }

    private static <T extends Block> void itemBlock(String name, DeferredBlock<T> block, Rarity rarity) {
        AEItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().rarity(rarity)));
    }


    private static <T extends Block> void itemBlock(String name, DeferredBlock<T> block){
        AEItems.ITEMS.register(name,()-> new BlockItem(block.get(),new Item.Properties()));
    }

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
}
