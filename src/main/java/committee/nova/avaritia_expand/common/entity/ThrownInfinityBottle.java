package committee.nova.avaritia_expand.common.entity;

import committee.nova.avaritia_expand.init.registry.AEItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

public class ThrownInfinityBottle extends ThrowableItemProjectile {

    private static final List<Holder<MobEffect>> POSITIVE_EFFECTS = new ArrayList<>();
    private static final List<Holder<MobEffect>> NEGATIVE_EFFECTS = new ArrayList<>();

    static {
        for (Holder<MobEffect> effectHolder : BuiltInRegistries.MOB_EFFECT.holders().toList()) {
            MobEffect effect = effectHolder.value();
            if (effect.getCategory() == MobEffectCategory.BENEFICIAL) {
                POSITIVE_EFFECTS.add(effectHolder);
            } else if (effect.getCategory() == MobEffectCategory.HARMFUL) {
                NEGATIVE_EFFECTS.add(effectHolder);
            }
        }
    }

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
        if (this.level() instanceof ServerLevel serverLevel) {
            RandomSource random = this.level().random;
            int color = random.nextInt(0xFFFFFF);
            serverLevel.levelEvent(2002, this.blockPosition(), color);
            int i = 45 + this.level().random.nextInt(10) + this.level().random.nextInt(10);
            if (result instanceof EntityHitResult entityHit) {
                Entity target = entityHit.getEntity();

                if (target instanceof LivingEntity living) {
                    if (target == this.getOwner()) {
                        if (!POSITIVE_EFFECTS.isEmpty()) {
                            Holder<MobEffect> effect = POSITIVE_EFFECTS.get(random.nextInt(POSITIVE_EFFECTS.size()));
                            living.addEffect(new MobEffectInstance(effect, 200, 1));
                        }
                    } else {
                        if (!NEGATIVE_EFFECTS.isEmpty()) {
                            Holder<MobEffect> effect = NEGATIVE_EFFECTS.get(random.nextInt(NEGATIVE_EFFECTS.size()));
                            living.addEffect(new MobEffectInstance(effect, 100, 0));
                        }
                    }
                }
            }
            ExperienceOrb.award((ServerLevel)this.level(), this.position(), i);
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return AEItems.infinity_bottle.get();
    }
}
