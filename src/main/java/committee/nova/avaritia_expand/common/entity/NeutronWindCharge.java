package committee.nova.avaritia_expand.common.entity;

import committee.nova.avaritia_expand.init.registry.AEEntities;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class NeutronWindCharge extends AbstractWindCharge {

    private int noDeflectTicks = 5;

    public NeutronWindCharge(EntityType<? extends AbstractWindCharge> entityType, Level level) {
        super(entityType, level);
    }
    public NeutronWindCharge(Player player, Level level, double x, double y, double z) {
        super(AEEntities.NEUTRON_WIND_CHARGE.get(), level, player, x, y, z);
    }

    public NeutronWindCharge(Level level, double x, double y, double z, Vec3 movement) {
        super(AEEntities.NEUTRON_WIND_CHARGE.get(), level);
    }
    @Override
    protected void explode(Vec3 pos) {
        Level level = this.level();

        level.explode(
                this,
                null,
                EXPLOSION_DAMAGE_CALCULATOR,
                pos.x(),
                pos.y(),
                pos.z(),
                1.2F,
                false,
                Level.ExplosionInteraction.TRIGGER
        );

    }

    @Override
    public void tick() {
        super.tick();
        if (this.noDeflectTicks > 0) {
            this.noDeflectTicks--;
        }
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity entity, @Nullable Entity owner, boolean deflectedByPlayer) {
        return this.noDeflectTicks > 0 ? false : super.deflect(deflection, entity, owner, deflectedByPlayer);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        var entity = result.getEntity();
        if (!this.level().isClientSide) {
            if (entity instanceof LivingEntity livingEntity){

                livingEntity.hurt(this.level().damageSources().fallingBlock(this), 50);
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));

            }
        }
    }
}
