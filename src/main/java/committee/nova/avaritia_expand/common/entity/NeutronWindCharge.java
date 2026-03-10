package committee.nova.avaritia_expand.common.entity;

import committee.nova.avaritia_expand.init.registry.AEEntities;
import committee.nova.mods.avaritia.common.entity.BladeSlashEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class NeutronWindCharge extends AbstractWindCharge {
    public NeutronWindCharge(EntityType<? extends AbstractWindCharge> entityType, Level level) {
        super(entityType, level);
    }
    public NeutronWindCharge(Player player, Level level, double x, double y, double z) {
        super(AEEntities.NEUTRON_WIND_CHARGE.get(), level, player, x, y, z);
    }

    public NeutronWindCharge(Level level, double x, double y, double z, Vec3 vec3) {
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
