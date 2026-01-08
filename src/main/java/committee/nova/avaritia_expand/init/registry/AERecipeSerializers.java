package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.crafting.recipe.SingularityStewCraftRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AERecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, AvaritiaExpand.MOD_ID);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> SINGULARITY_STEW_CRAFT_SERIALIZER = serializer("singularity_stew", SingularityStewCraftRecipe.Serializer::new);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> serializer(String name, Supplier<RecipeSerializer<?>> serializer) {
        return SERIALIZERS.register(name, serializer);
    }
}
