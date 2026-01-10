package committee.nova.avaritia_expand.init.data.provider.loot;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.init.registry.AEBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AEBlockLootTables extends BlockLootSubProvider {
    protected AEBlockLootTables(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        for (var block : AEBlocks.BLOCKS.getEntries()) {
            dropSelf(block.get());
        }
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.stream()
                .filter(block -> AvaritiaExpand.MOD_ID.equals(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)).getNamespace()))
                .collect(Collectors.toSet());
    }
}
