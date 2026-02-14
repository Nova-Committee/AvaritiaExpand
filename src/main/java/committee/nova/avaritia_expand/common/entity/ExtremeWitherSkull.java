package committee.nova.avaritia_expand.common.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class ExtremeWitherSkull extends WitherSkull {

    public ExtremeWitherSkull(EntityType<? extends WitherSkull> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.level() instanceof ServerLevel serverlevel) {
            Entity entity = result.getEntity();
            boolean flag;
            if (this.getOwner() instanceof LivingEntity livingentity) {
                DamageSource damagesource = this.damageSources().witherSkull(this, livingentity);
                flag = entity.hurt(damagesource, 30.0F);
                if (flag) {
                    if (entity.isAlive()) {
                        EnchantmentHelper.doPostAttackEffects(serverlevel, entity, damagesource);
                    } else {
                        livingentity.heal(30.0F);
                    }
                }
            } else {
                flag = entity.hurt(this.damageSources().magic(), 5.0F);
            }

            if (flag && entity instanceof LivingEntity livingentity1) {
                int i = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 10;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 40;
                }

                if (i > 0) {
                    livingentity1.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * i, 2), this.getEffectSource());
                }
            }
        }
    }
}
