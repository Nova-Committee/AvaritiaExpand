package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class AEArmorMaterials {
    public static final Holder<ArmorMaterial> BLAZE = register(
            "blaze_cube", Util.make(
            new EnumMap<>(ArmorItem.Type.class),attribute -> {
                    attribute.put(ArmorItem.Type.BOOTS, 32);
                    attribute.put(ArmorItem.Type.LEGGINGS, 35);
                    attribute.put(ArmorItem.Type.CHESTPLATE, 32);
                    attribute.put(ArmorItem.Type.HELMET, 30);


                    }),25,3f,0.1f,()-> ModItems.blaze_cube.get());
    public static final Holder<ArmorMaterial> CRYSTAL = register(
            "crystal", Util.make(
                    new EnumMap<>(ArmorItem.Type.class),attribute -> {
                        attribute.put(ArmorItem.Type.BOOTS, 58);
                        attribute.put(ArmorItem.Type.LEGGINGS, 60);
                        attribute.put(ArmorItem.Type.CHESTPLATE, 58);
                        attribute.put(ArmorItem.Type.HELMET, 56);


                    }),25,5f,0.5f,()-> ModItems.blaze_cube.get());

    private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> typeProtection,
                                                  int enchantability, float toughness, float knockbackResistance,
                                                  Supplier<Item> ingredientItem) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(AvaritiaExpand.MOD_ID, name);
        Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_NETHERITE;
        Supplier<Ingredient> ingredient = () -> Ingredient.of(ingredientItem.get());
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(location));

        EnumMap<ArmorItem.Type, Integer> typeMap = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            typeMap.put(type, typeProtection.get(type));
        }

        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, location,
                new ArmorMaterial(typeProtection, enchantability, equipSound, ingredient, layers, toughness, knockbackResistance));
    }
}
