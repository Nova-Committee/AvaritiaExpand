package committee.nova.avaritia_expand.init.data.provider;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.item.armor.blaze.BlazeArmorItem;
import committee.nova.avaritia_expand.init.registry.AEItems;
import committee.nova.mods.avaritia.init.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class AEItemTags extends IntrinsicHolderTagsProvider<Item> {

    public AEItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.ITEM, future, block -> block.builtInRegistryHolder().key(), AvaritiaExpand.MOD_ID, existingFileHelper);
    }

    @Override
    public @NotNull String getName() {
        return "Avaritia Expand Item Tags";
    }


    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
        tag(ModTags.IMMORTAL_ITEM).add(AEItems.wither_star.get());
        tag(ModTags.IMMORTAL_ITEM).add(AEItems.wither_totem.get());
        tag(ModTags.IMMORTAL_ITEM).add(AEItems.blaze_totem.get());
        tag(ItemTags.ARMOR_ENCHANTABLE).add(AEItems.blaze_helmet.get());
        tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(AEItems.blaze_helmet.get());
        tag(ItemTags.ARMOR_ENCHANTABLE).add(AEItems.blaze_chestplate.get());
        tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(AEItems.blaze_chestplate.get());
        tag(ItemTags.ARMOR_ENCHANTABLE).add(AEItems.blaze_leggings.get());
        tag(ItemTags.LEG_ARMOR_ENCHANTABLE).add(AEItems.blaze_leggings.get());
        tag(ItemTags.ARMOR_ENCHANTABLE).add(AEItems.blaze_boots.get());
        tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(AEItems.blaze_boots.get());
        tag(ItemTags.ARMOR_ENCHANTABLE).add(AEItems.crystal_helmet.get());
        tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(AEItems.crystal_helmet.get());
        tag(ItemTags.ARMOR_ENCHANTABLE).add(AEItems.crystal_helmet.get());
        tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(AEItems.crystal_chestplate.get());
        tag(ItemTags.ARMOR_ENCHANTABLE).add(AEItems.crystal_leggings.get());
        tag(ItemTags.LEG_ARMOR_ENCHANTABLE).add(AEItems.crystal_leggings.get());
        tag(ItemTags.ARMOR_ENCHANTABLE).add(AEItems.crystal_boots.get());
        tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(AEItems.crystal_boots.get());
    }
}
