package committee.nova.avaritia_expand.init.compat;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class AEJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = AvaritiaExpand.rl( "jei_plugin");
    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world != null) {
            registration.addIngredientInfo(new ItemStack(ModItems.blaze_axe.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.tooltip.avaritia_expand.blaze_axe"));
        }
    }
}
