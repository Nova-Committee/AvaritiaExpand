package committee.nova.avaritia_expand.common.entity;

import committee.nova.avaritia_expand.init.registry.AEEntities;
import committee.nova.mods.avaritia.client.particle.ShockwaveParticleOptions;
import committee.nova.mods.avaritia.init.registry.ModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.function.Function;

public class BlazeWindCharge extends AbstractWindCharge {

    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR =
            new SimpleExplosionDamageCalculator(
                    true,
                    false,
                    Optional.of(2.4F),
                    BuiltInRegistries.BLOCK.getTag(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
            );

    private int noDeflectTicks = 5;

    public BlazeWindCharge(EntityType<? extends AbstractWindCharge> type, Level level) {
        super(type, level);
    }

    public BlazeWindCharge(Player player, Level level, double x, double y, double z) {
        super(AEEntities.BLAZE_WIND_CHARGE.get(), level, player, x, y, z);
    }

    public BlazeWindCharge(Level level, double x, double y, double z, Vec3 vec3) {
        super(AEEntities.BLAZE_WIND_CHARGE.get(), level);
    }


    @Override
    public void tick() {
        super.tick();
        if (noDeflectTicks > 0) noDeflectTicks--;
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, Entity entity, Entity owner, boolean byPlayer) {
        return noDeflectTicks <= 0 && super.deflect(deflection, entity, owner, byPlayer);
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
                Level.ExplosionInteraction.TRIGGER,
                ParticleTypes.GUST_EMITTER_SMALL,
                ParticleTypes.GUST_EMITTER_LARGE,
                SoundEvents.WIND_CHARGE_BURST
        );

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            Entity owner = this.getOwner();
            BlockPos center = BlockPos.containing(pos);

            if (owner instanceof ServerPlayer player) {

                var entities = level.getEntitiesOfClass(
                        LivingEntity.class,
                        new AABB(center).inflate(10),
                        e -> !e.isSpectator() && e.isAlive() && e != player
                );

                for (LivingEntity entity : entities) {
                    entity.addEffect(new MobEffectInstance(ModMobEffects.BURNING, 600, 3));
                }

                spawnParticles(
                        serverLevel,
                        new ShockwaveParticleOptions(
                                new Vector3f(1F, 0f, 0f),
                                10F,
                                true,
                                "minecraft:flame"
                        ),
                        pos.x(),
                        pos.y(),
                        pos.z(),
                        1,
                        0,0,0,
                        0,
                        true
                );
            }
        }
    }

    private static void spawnParticles(
            ServerLevel level,
            ParticleOptions particle,
            double x, double y, double z,
            int count,
            double dx, double dy, double dz,
            double speed,
            boolean force
    ) {
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            level.sendParticles(player, particle, force, x, y, z, count, dx, dy, dz, speed);
        }
    }
}