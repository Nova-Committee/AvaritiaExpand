package committee.nova.avaritia_expand.common.entity;

import committee.nova.avaritia_expand.init.registry.AEEntities;
import committee.nova.mods.avaritia.common.entity.BladeSlashEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Function;

public class CrystalWindCharge extends AbstractWindCharge {

    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR =
            new SimpleExplosionDamageCalculator(
                    true,
                    false,
                    Optional.of(3.6F),
                    BuiltInRegistries.BLOCK.getTag(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
            );

    public CrystalWindCharge(EntityType<? extends AbstractWindCharge> entityType, Level level) {
        super(entityType, level);
    }

    public CrystalWindCharge(Player player, Level level, double x, double y, double z) {
        super(AEEntities.CRYSTAL_WIND_CHARGE.get(), level, player, x, y, z);
    }

    public CrystalWindCharge(Level level, double x, double y, double z, Vec3 vec3) {
        super(AEEntities.CRYSTAL_WIND_CHARGE.get(), level);
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
                1.0F,
                false,
                Level.ExplosionInteraction.TRIGGER
        );

        if (!level.isClientSide) {

            for (int i = 0; i < 12; i++) {
                double angle = (2 * Math.PI * i) / 12;
                double xd = Math.cos(angle) * 2.0;
                double zd = Math.sin(angle) * 2.0;
                double yd = 0.1;

                BladeSlashEntity slash = new BladeSlashEntity(level, pos.x(), pos.y(), pos.z());

                slash.setPos(pos.x(), pos.y(), pos.z());
                slash.setDeltaMovement(xd, yd, zd);
                level.addFreshEntity(slash);
            }
        }
    }
}
