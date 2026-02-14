package committee.nova.avaritia_expand.common.entity;

import committee.nova.avaritia_expand.init.registry.AEEntities;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ExtremeWitherEntity extends WitherBoss implements PowerableMob, RangedAttackMob {
    private boolean secondPhase = false;

    public ExtremeWitherEntity(EntityType<? extends WitherBoss> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 4096)
                .add(Attributes.MAX_ABSORPTION, 2048)
                .add(Attributes.MOVEMENT_SPEED, 1.5F)
                .add(Attributes.FLYING_SPEED, 1.5F)
                .add(Attributes.FOLLOW_RANGE, 150.0)
                .add(Attributes.ARMOR, 128.0);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!secondPhase && this.getHealth() - amount <= 1.0F) {
            enterSecondPhase();
            return false;
        }
        if (secondPhase && source.getDirectEntity() instanceof Projectile)
            return false;

        return super.hurt(source, amount);
    }

    private void enterSecondPhase() {
        secondPhase = true;
        this.setHealth(Math.min(this.getMaxHealth(), this.getHealth() + 2048));
        this.setInvulnerableTicks(0);

        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(2.2);
        Objects.requireNonNull(this.getAttribute(Attributes.FLYING_SPEED)).setBaseValue(2.2);

        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 6F, false, Level.ExplosionInteraction.MOB);
    }

    private void performRangedAttack(int head, double x, double y, double z, boolean isDangerous) {
        if (!this.isSilent()) {
            this.level().levelEvent(null, 1024, this.blockPosition(), 0);
        }

        double d0 = this.getHeadX(head);
        double d1 = this.getHeadY(head);
        double d2 = this.getHeadZ(head);
        double d3 = x - d0;
        double d4 = y - d1;
        double d5 = z - d2;
        Vec3 vec3 = new Vec3(d3, d4, d5);

        ExtremeWitherSkull witherskull = new ExtremeWitherSkull(AEEntities.EXTREME_WITHER_SKULL.get(), this.level());
        witherskull.setOwner(this);
        witherskull.setDangerous(isDangerous);
        witherskull.setPos(d0, d1, d2);
        witherskull.shoot(vec3.x, vec3.y, vec3.z, 0.2F, 1.0F);

        this.level().addFreshEntity(witherskull);
    }

    private void performRangedAttack(int head, LivingEntity target) {
        this.performRangedAttack(
                head,
                target.getX(),
                target.getY() + target.getEyeHeight() * 0.5,
                target.getZ(),
                head == 0 && this.random.nextFloat() < 0.001F
        );
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        this.performRangedAttack(0, target);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if (secondPhase && tickCount % 20 == 0)
            heal(2);

        if (!secondPhase) return;

        if (this.tickCount % 10 == 0) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                for (int i = 0; i < 3; i++) {
                    this.performRangedAttack(target, i);
                }
            }
        }
    }

    private void performRangedAttack(LivingEntity target, int headIndex) {
        this.performRangedAttack(
                headIndex,
                target.getX(),
                target.getY() + target.getEyeHeight() * 0.5,
                target.getZ(),
                headIndex == 0 && this.random.nextFloat() < 0.001F
        );
    }

    private double getHeadX(int head) {
        if (head <= 0) {
            return this.getX();
        } else {
            float f = (this.yBodyRot + (float)(180 * (head - 1))) * (float) (Math.PI / 180.0);
            float f1 = Mth.cos(f);
            return this.getX() + (double)f1 * 1.3 * (double)this.getScale();
        }
    }

    private double getHeadY(int head) {
        float f = head <= 0 ? 3.0F : 2.2F;
        return this.getY() + (double)(f * this.getScale());
    }

    private double getHeadZ(int head) {
        if (head <= 0) {
            return this.getZ();
        } else {
            float f = (this.yBodyRot + (float)(180 * (head - 1))) * (float) (Math.PI / 180.0);
            float f1 = Mth.sin(f);
            return this.getZ() + (double)f1 * 1.3 * (double)this.getScale();
        }
    }
}
