package committee.nova.avaritia_expand.init.data.provider.loot;

import committee.nova.avaritia_expand.init.registry.AEEntities;
import committee.nova.avaritia_expand.init.registry.AEItems;
import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;


public class AEEntityLootTables extends EntityLootSubProvider {

    protected AEEntityLootTables(HolderLookup.Provider registries) {
        super( FeatureFlags.REGISTRY.allFlags(),registries);
    }

    @Override
    public void generate() {
        this.add(AEEntities.EXTREME_WITHER.get(), LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        LootItem.lootTableItem(AEItems.star_dessert)
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(5.0F))) // 100%掉落5个
                                )
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                )
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        LootItem.lootTableItem(ModItems.crystal_matrix_ingot)
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(30.0F)))
                                )
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                )
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        LootItem.lootTableItem(ModItems.neutron_ingot)
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(30.0F)))
                                )
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                )
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        LootItem.lootTableItem(AEItems.wither_totem)
                                                .when(LootItemRandomChanceCondition.randomChance(0.1F)) // 10%概率掉落
                                )
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                )
        );
    }
    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return AEEntities.ENTITIES.getEntries().stream()
                .map(DeferredHolder::get);
    }

}
