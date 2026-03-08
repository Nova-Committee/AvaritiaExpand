package committee.nova.avaritia_expand.util;

import committee.nova.mods.avaritia.init.registry.ModEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;

import java.util.ArrayList;
import java.util.List;

public class ProjectileRegistry {

    public static final List<EntityType<? extends Projectile>> PROJECTILES = new ArrayList<>();

    static {

        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {

            if (!type.canSummon()) continue;

            if (type.is(EntityTypeTags.IMPACT_PROJECTILES)) {

                @SuppressWarnings("unchecked")
                EntityType<? extends Projectile> p =
                        (EntityType<? extends Projectile>) type;

                PROJECTILES.add(p);
            }
        }

        PROJECTILES.removeIf(type -> type == ModEntities.ENDER_PEARL.get());

        System.out.println("Loaded Projectiles: " + PROJECTILES.size());
    }
}
