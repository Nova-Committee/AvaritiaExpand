package committee.nova.avaritia_expand.init.data.provider;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.init.data.provider.recipe.ModSingularityRecipeBuilder;
import committee.nova.avaritia_expand.init.registry.AEBlocks;
import committee.nova.avaritia_expand.init.registry.AEItems;
import committee.nova.mods.avaritia.init.data.provider.recipe.ModShapedRecipeBuilder;
import committee.nova.mods.avaritia.init.data.provider.recipe.ModShapelessRecipeBuilder;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class AERecipes extends RecipeProvider implements IConditionBuilder {
    public AERecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }
    protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(@NotNull TagKey<Item> tagKey) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(tagKey).build());
    }

    protected static String getModItemName(ItemLike pItemLike) {
        return BuiltInRegistries.ITEM.getKey(pItemLike.asItem()).getPath();
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput consumer) {
        var lul = has(Items.AIR);
        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AEItems.infinity_shears, 1, 1)
                .pattern(" a")
                .pattern("a ")
                .define('a', ModItems.infinity_ingot.get())
                .unlockedBy("has_item", has(ModItems.infinity_ingot.get())).save(consumer);

        //Blaze Armor
        ModShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AEItems.blaze_helmet.get(),2)
                .pattern("A   A")
                .pattern("ABBBA")
                .pattern("CB BC")
                .pattern("CBBBC")
                .pattern("C   C")
                .define('A', Items.BONE_BLOCK)
                .define('B', ModItems.blaze_cube.get())
                .define('C', Items.BLAZE_POWDER)
                .unlockedBy("has_item", has(ModItems.blaze_cube.get())).save(consumer);

        ModShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AEItems.blaze_chestplate.get(),2)
                .pattern("A   A")
                .pattern("ABBBA")
                .pattern("CABAC")
                .pattern("CBBBC")
                .pattern(" BBB ")
                .define('A', Items.BONE_BLOCK)
                .define('B', ModItems.blaze_cube.get())
                .define('C', Items.BLAZE_POWDER)
                .unlockedBy("has_item", has(ModItems.blaze_cube.get())).save(consumer);

        ModShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AEItems.blaze_leggings.get(),2)
                .pattern("BBBBB")
                .pattern("CBABC")
                .pattern("AB BA")
                .pattern("BA AB")
                .pattern("BB BB")
                .define('A', Items.BONE_BLOCK)
                .define('B', ModItems.blaze_cube.get())
                .define('C', Items.BLAZE_POWDER)
                .unlockedBy("has_item", has(ModItems.blaze_cube.get())).save(consumer);

        ModShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AEItems.blaze_boots.get(),2)
                .pattern("     ")
                .pattern(" B B ")
                .pattern(" B B ")
                .pattern("CB BC")
                .pattern("AA AA")
                .define('A', Items.BONE_BLOCK)
                .define('B', ModItems.blaze_cube.get())
                .define('C', Items.BLAZE_POWDER)
                .unlockedBy("has_item", has(ModItems.blaze_cube.get())).save(consumer);

        // Crystal Armor
        ModShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AEItems.crystal_helmet.get(),3)
                .pattern(" BBBBB ")
                .pattern("B BAB B")
                .pattern("BCBBBCB")
                .pattern("CDDDDDC")
                .pattern("CBCCCBC")
                .pattern(" BDCDB ")
                .pattern("       ")
                .define('A', Items.NETHER_STAR)
                .define('B', ModItems.crystal_matrix_ingot.get())
                .define('C', ModBlocks.crystal_matrix.get())
                .define('D', ModItems.neutron_ingot.get())
                .unlockedBy("has_item", has(ModItems.crystal_matrix_ingot.get())).save(consumer);

        ModShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AEItems.crystal_chestplate.get(),3)
                .pattern("       ")
                .pattern("BABCBAB")
                .pattern("BCCCCCB")
                .pattern("BCCACCB")
                .pattern(" BCCCB ")
                .pattern(" BCCCB ")
                .pattern(" C   C ")
                .define('A', Items.NETHER_STAR)
                .define('B', ModItems.crystal_matrix_ingot.get())
                .define('C', ModBlocks.crystal_matrix.get())
                .unlockedBy("has_item", has(ModItems.crystal_matrix_ingot.get())).save(consumer);

        ModShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AEItems.crystal_leggings.get(),3)
                .pattern(" CCCCC ")
                .pattern(" CBBBC ")
                .pattern(" CBBBC ")
                .pattern(" CB BC ")
                .pattern(" CC CC ")
                .pattern(" DB BD ")
                .define('B', ModItems.crystal_matrix_ingot.get())
                .define('C', ModBlocks.crystal_matrix.get())
                .define('D', ModItems.neutron_ingot.get())
                .unlockedBy("has_item", has(ModItems.crystal_matrix_ingot.get())).save(consumer);

        ModShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AEItems.crystal_boots.get(),3)
                .pattern("       ")
                .pattern(" CC CC ")
                .pattern(" DC CD ")
                .pattern(" DC CD ")
                .pattern("BBC CBB")
                .pattern("BAC CAB")
                .pattern("       ")
                .define('A', Items.NETHER_STAR)
                .define('B', ModItems.crystal_matrix_ingot.get())
                .define('C', ModBlocks.crystal_matrix.get())
                .define('D', ModItems.neutron_ingot.get())
                .unlockedBy("has_item", has(ModItems.crystal_matrix_ingot.get())).save(consumer);

        //Infinity TNT
        ModShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AEBlocks.infinity_tnt_block.get())
                .pattern("AAAAAAAAA")
                .pattern("AAAAAAAAA")
                .pattern("AAAAAAAAA")
                .pattern("AAACBCAAA")
                .pattern("AAABEBAAA")
                .pattern("AAACBCAAA")
                .pattern("AAAAAAAAA")
                .pattern("AAAAAAAAA")
                .pattern("AAAAAAAAA")
                .define('A', Items.TNT)
                .define('B', ModItems.neutron_pile.get())
                .define('C', ModItems.infinity_nugget.get())
                .define('E', ModItems.infinity_catalyst.get())
                .unlockedBy("has_item", has(ModItems.neutron_pile.get())).save(consumer);

        // Infinity Bottle
        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AEItems.infinity_bottle.get())
                .pattern("   ABA   ")
                .pattern("   ADA   ")
                .pattern("   A A   ")
                .pattern("  A   A  ")
                .pattern(" A  E  A ")
                .pattern(" ACCCCCA ")
                .pattern(" ACCCCCA ")
                .pattern(" AFFFFFA ")
                .pattern("  AAAAA  ")
                .define('A', Items.GLASS)
                .define('B', ModBlocks.neutron.get())
                .define('C', Items.EXPERIENCE_BOTTLE)
                .define('D', ModItems.neutron_ingot.get())
                .define('E', ModItems.infinity_catalyst.get())
                .define('F', Items.DRAGON_BREATH)
                .unlockedBy("has_item", has(ModBlocks.neutron.get())).save(consumer);

        //Neutron Decomposer

        ModShapedRecipeBuilder.shaped(RecipeCategory.MISC, AEBlocks.neutron_decompose.get())
                .pattern("AAAEEEAAA")
                .pattern("E ADDDA E")
                .pattern("A  DDD  A")
                .pattern("E  DDD  E")
                .pattern("C  DDD  C")
                .pattern("E  DBD  E")
                .pattern("A DDDDD A")
                .pattern("E       E")
                .pattern("AAAEAEAAA")
                .define('A', Items.IRON_BLOCK)
                .define('B', ModBlocks.neutron.get())
                .define('C', Items.REDSTONE_BLOCK)
                .define('D', ModItems.neutron_ingot.get())
                .define('E', ModItems.crystal_matrix_ingot.get())
                .unlockedBy("has_item", has(ModItems.singularity.get())).save(consumer);

        // Neutron Tools
        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AEItems.neutron_sword.get(),3)
                .pattern("     DD")
                .pattern("    DDD")
                .pattern(" D DDD ")
                .pattern(" DDAD  ")
                .pattern("  DD   ")
                .pattern(" D DD  ")
                .pattern("D      ")
                .define('A', ModBlocks.neutron.get())
                .define('D', ModItems.neutron_ingot.get())
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get())).save(consumer);

        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AEItems.neutron_axe.get(),3)
                .pattern("  DDD  ")
                .pattern(" DAD   ")
                .pattern(" DDDDD ")
                .pattern(" D D D ")
                .pattern("  D    ")
                .pattern(" D     ")
                .pattern("D      ")
                .define('A', ModBlocks.neutron.get())
                .define('D', ModItems.neutron_ingot.get())
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get())).save(consumer);

        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AEItems.neutron_hoe.get(),3)
                .pattern(" DDDD A")
                .pattern("    DD ")
                .pattern("    DD ")
                .pattern("   D  D")
                .pattern("  D    ")
                .pattern(" D     ")
                .pattern("D      ")
                .define('A', ModBlocks.neutron.get())
                .define('D', ModItems.neutron_ingot.get())
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get())).save(consumer);

        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AEItems.neutron_pickaxe.get(),3)
                .pattern(" DDDD  ")
                .pattern("   DDA ")
                .pattern("    DDD")
                .pattern("   D DD")
                .pattern("  D   D")
                .pattern(" D    D")
                .pattern("D      ")
                .define('A', ModBlocks.neutron.get())
                .define('D', ModItems.neutron_ingot.get())
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get())).save(consumer);

        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AEItems.neutron_shovel.get(),3)
                .pattern("    DDD")
                .pattern("   DDAD")
                .pattern("    DDD")
                .pattern("   D D ")
                .pattern("  D    ")
                .pattern(" D     ")
                .pattern("D      ")
                .define('A', ModBlocks.neutron.get())
                .define('D', ModItems.neutron_ingot.get())
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get())).save(consumer);

        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AEItems.neutron_mobile.get(),3)
                .pattern("A      ")
                .pattern("DDD    ")
                .pattern("DDD    ")
                .pattern("DDD    ")
                .pattern("DDDDDD ")
                .pattern(" DBCEDD")
                .pattern("  DDDD ")
                .define('A', Items.RECOVERY_COMPASS)
                .define('B', Items.COMPASS)
                .define('C', Items.BLAZE_POWDER)
                .define('E', Items.ENDER_EYE)
                .define('D', ModItems.neutron_ingot.get())
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get())).save(consumer);


        //Blaze PortableBurner

        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AEItems.blaze_portable_burner.get(),2)
                .pattern(" EEE ")
                .pattern("E A E")
                .pattern("EADAE")
                .pattern("EBBBE")
                .pattern(" CCC ")
                .define('A', Items.BLAZE_POWDER)
                .define('B', Items.NETHERRACK)
                .define('C', Items.OBSIDIAN)
                .define('E', Items.NETHER_BRICK)
                .define('D', ModItems.blaze_cube.get())
                .unlockedBy("has_item", has(ModItems.blaze_cube.get())).save(consumer);

        // Wither Star

        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AEItems.wither_star.get(),2)
                .pattern("  D  ")
                .pattern(" DCD ")
                .pattern("DEEED")
                .pattern(" DED ")
                .pattern("  D  ")
                .define('C', ModBlocks.blaze_cube_block)
                .define('E', ModBlocks.neutron)
                .define('D', ModItems.crystal_matrix_ingot.get())
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get())).save(consumer);

        // Singularity Stew
        ModSingularityRecipeBuilder.shapeless(RecipeCategory.MISC)
                .requires(ModItems.neutron_nugget)
                .unlockedBy("has_item", has(ModItems.singularity.get())).save(consumer);

        ModShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AEItems.singularity_stew.get())
                .requires(ModItems.neutron_nugget.get())
                .requires(ModItems.eternal_singularity.get())
                .group("singularity_stew_eternal")
                .unlockedBy("has_item", has(ModItems.eternal_singularity.get())).save(consumer, AvaritiaExpand.rl("singularity_stew_eternal"));
    }
}