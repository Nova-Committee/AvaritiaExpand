package committee.nova.avaritia_expand.init.registry.enums;

import committee.nova.avaritia_expand.init.registry.AETags;
import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class AEToolTiers {

    public static final Tier NEUTRON =new SimpleTier(AETags.NEEDS_NEUTRON_TOOL,9999, 75f, 75f,  500,
            () -> Ingredient.of(ModItems.neutron_ingot.get()));
}
