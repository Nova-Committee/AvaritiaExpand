package committee.nova.avaritia_expand.init.data.provider.recipe;

import committee.nova.avaritia_expand.common.crafting.recipe.SingularityStewCraftRecipe;
import committee.nova.avaritia_expand.init.registry.AEItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModSingularityRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final int count;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private ICondition[] conditions;

    public ModSingularityRecipeBuilder(RecipeCategory recipeCategory, int count) {
        this.category = recipeCategory;
        this.count = count;
    }

    public static @NotNull ModSingularityRecipeBuilder shapeless(RecipeCategory recipeCategory) {
        return new ModSingularityRecipeBuilder(recipeCategory, 1);
    }

    public static @NotNull ModSingularityRecipeBuilder shapeless(RecipeCategory recipeCategory, int count) {
        return new ModSingularityRecipeBuilder(recipeCategory, count);
    }


    public ModSingularityRecipeBuilder requires(TagKey<Item> itemTagKey) {
        return this.requires(Ingredient.of(itemTagKey));
    }

    public ModSingularityRecipeBuilder requires(ItemLike itemLike) {
        return this.requires(itemLike, 1);
    }

    public ModSingularityRecipeBuilder requires(ItemLike itemLike, int p_126213_) {
        for (int i = 0; i < p_126213_; ++i) {
            this.requires(Ingredient.of(itemLike));
        }

        return this;
    }

    public ModSingularityRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public ModSingularityRecipeBuilder requires(Ingredient ingredient, int i1) {
        for (int i = 0; i < i1; ++i) {
            this.ingredients.add(ingredient);
        }

        return this;
    }

    @Override
    public @NotNull ModSingularityRecipeBuilder unlockedBy(@NotNull String name, @NotNull Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }


    @Override
    public @NotNull ModSingularityRecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return AEItems.singularity_stew.get();
    }

    public @NotNull ModSingularityRecipeBuilder conditions(@Nullable ICondition... conditions) {
        this.conditions = conditions;
        return this;
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
        this.ensureValid(id);
        Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        SingularityStewCraftRecipe shapelessrecipe = new SingularityStewCraftRecipe(
                this.ingredients,
                this.count
        );
        if (this.conditions != null) {
            recipeOutput.accept(id, shapelessrecipe, advancement$builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")), this.conditions);
        } else {
            recipeOutput.accept(id, shapelessrecipe, advancement$builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
        }
    }

    private void ensureValid(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }
}
