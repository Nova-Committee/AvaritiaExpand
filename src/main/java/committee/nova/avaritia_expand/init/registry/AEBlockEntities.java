package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.block.entity.NeutronDecomposeTile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AEBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, AvaritiaExpand.MOD_ID);


    public static final Supplier<BlockEntityType<NeutronDecomposeTile>> NEUTRON_DECOMPOSE_BE = BLOCK_ENTITIES.register("neutron_decompose", () -> BlockEntityType.Builder.of(NeutronDecomposeTile::new, AEBlocks.neutron_decompose.get()).build( null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
