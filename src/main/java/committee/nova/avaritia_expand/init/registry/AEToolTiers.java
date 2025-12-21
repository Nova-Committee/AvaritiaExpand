package committee.nova.avaritia_expand.init.registry;

import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class AEToolTiers {

    public static final Tier NEUTRON =new SimpleTier(AETags.NEEDS_NEUTRON_TOOL,9999, -3f, 70f,  500,
            () -> Ingredient.of(ModItems.neutron_ingot.get()));
}
