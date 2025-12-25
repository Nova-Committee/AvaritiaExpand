package committee.nova.avaritia_expand.common.entity;

import committee.nova.avaritia_expand.init.registry.AEEntities;
import committee.nova.avaritia_expand.init.registry.AEItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ThrownInfinityBottle extends ThrowableItemProjectile {
    public ThrownInfinityBottle(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownInfinityBottle(Level level, LivingEntity shooter) {
        super(EntityType.EXPERIENCE_BOTTLE, shooter, level);
    }

    public ThrownInfinityBottle(Level level, double x, double y, double z) {
        super(EntityType.EXPERIENCE_BOTTLE, x, y, z,level);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.07;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level() instanceof ServerLevel) {
            this.level().levelEvent(2002, this.blockPosition(), PotionContents.getColor(Potions.WATER));
            int i = 45 + this.level().random.nextInt(10) + this.level().random.nextInt(10);
            ExperienceOrb.award((ServerLevel)this.level(), this.position(), i);
            this.discard();
        }
    }
    @Override
    protected Item getDefaultItem() {
        return AEItems.infinity_bottle.get();
    }
}
