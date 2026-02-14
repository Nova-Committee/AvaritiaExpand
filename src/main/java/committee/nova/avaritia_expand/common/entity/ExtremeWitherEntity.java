package committee.nova.avaritia_expand.common.entity;

import com.google.common.collect.ImmutableList;
import committee.nova.avaritia_expand.init.registry.AEEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.swing.*;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ExtremeWitherEntity extends Monster implements PowerableMob, RangedAttackMob {
    private static final EntityDataAccessor<Integer> DATA_TARGET_A = SynchedEntityData.defineId(ExtremeWitherEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_B = SynchedEntityData.defineId(ExtremeWitherEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_C = SynchedEntityData.defineId(ExtremeWitherEntity.class, EntityDataSerializers.INT);
    private static final List<EntityDataAccessor<Integer>> DATA_TARGETS = ImmutableList.of(DATA_TARGET_A, DATA_TARGET_B, DATA_TARGET_C);
    private static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(ExtremeWitherEntity.class, EntityDataSerializers.INT);
    private boolean secondPhase = false;
    private boolean isSpawning = true;
    private boolean isInSpawnAnimation = true;
    private int spawnTimer = 200;
    private static final int INVULNERABLE_TICKS = 220;
    private final int[] nextHeadUpdate = new int[2];
    private final int[] idleHeadUpdates = new int[2];
    private final float[] xRotHeads = new float[2];
    private final float[] yRotHeads = new float[2];
    private final float[] xRotOHeads = new float[2];
    private final float[] yRotOHeads = new float[2];
    private int destroyBlocksTick;
    private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(
            this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);
    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = p_348303_ -> !p_348303_.getType().is(EntityTypeTags.WITHER_FRIENDS)
            && p_348303_.attackable();
    private static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forCombat().range(20.0).selector(LIVING_ENTITY_SELECTOR);

    public ExtremeWitherEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.bossEvent.setVisible(true);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 4096)
                .add(Attributes.MAX_ABSORPTION, 2048)
                .add(Attributes.MOVEMENT_SPEED, 2.5F)
                .add(Attributes.FLYING_SPEED, 2.5F)
                .add(Attributes.FOLLOW_RANGE, 150.0)
                .add(Attributes.ARMOR, 128.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new ExtremeWitherEntity.WitherDoNothingGoal());
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0, 40, 20.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, LIVING_ENTITY_SELECTOR));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Invul", this.getInvulnerableTicks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setInvulnerableTicks(compound.getInt("Invul"));
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    public static boolean spawnExtremeWither(ServerLevel level, BlockPos pos) {
        if (level.isClientSide()) return false;

        ExtremeWitherEntity boss = AEEntities.EXTREME_WITHER.get().create(level);
        if (boss == null) return false;

        boss.moveTo(
                pos.getX() + 0.5,
                pos.getY() + 0.55,
                pos.getZ() + 0.5,
                0,
                0
        );

        boss.isSpawning = true;
        boss.spawnTimer = 200; // 10秒生成时间
        boss.isInSpawnAnimation = true;

        boss.setHealth(1.0F);

        boss.makeInvulnerable();

        level.levelEvent(1023, pos, 0);

        for (int i = 0; i < 100; i++) {
            double x = pos.getX() + level.random.nextGaussian();
            double y = pos.getY() + level.random.nextGaussian();
            double z = pos.getZ() + level.random.nextGaussian();
            level.sendParticles(
                    net.minecraft.core.particles.ParticleTypes.EXPLOSION_EMITTER,
                    x, y, z, 1, 0, 0, 0, 0
            );
        }

        level.addFreshEntity(boss);
        return true;
    }


    public void die(DamageSource source) {
        super.die(source);
        if (!this.level().isClientSide()) {
            this.level().playSound(
                    null,
                    this.blockPosition(),
                    SoundEvents.WITHER_AMBIENT,
                    SoundSource.HOSTILE,
                    1.5F,
                    0F
            );

            if (this.level() instanceof ServerLevel serverLevel) {

                for (int i = 0; i < 25; i++) {
                    serverLevel.sendParticles(
                            ParticleTypes.SMOKE,
                            this.getX() + (this.random.nextDouble() - 0.5) * 3.0,
                            this.getY() + this.random.nextDouble() * 2.0,
                            this.getZ() + (this.random.nextDouble() - 0.5) * 3.0,
                            1, 0, 0, 0, 0
                    );
                }
                for (int i = 0; i < 25; i++) {
                    serverLevel.sendParticles(
                            ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS,
                            this.getX() + (this.random.nextDouble() - 0.5) * 3.0,
                            this.getY() + this.random.nextDouble() * 2.0,
                            this.getZ() + (this.random.nextDouble() - 0.5) * 3.0,
                            1, 0, 0, 0, 0
                    );
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getInvulnerableTicks() > 0 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.getInvulnerableTicks() > 0 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        } else {
            if (this.isPowered()) {
                Entity entity = source.getDirectEntity();
                if (entity instanceof AbstractArrow || entity instanceof WindCharge) {
                    return false;
                }
            }

            Entity entity1 = source.getEntity();
            if (entity1 != null && entity1.getType().is(EntityTypeTags.WITHER_FRIENDS)) {
                return false;
            } else {
                if (this.destroyBlocksTick <= 0) {
                    this.destroyBlocksTick = 20;
                }

                for (int i = 0; i < this.idleHeadUpdates.length; i++) {
                    this.idleHeadUpdates[i] = this.idleHeadUpdates[i] + 3;
                }
            }

            if (!secondPhase && this.getHealth() - amount <= 1.0F) {
                enterSecondPhase();
                return false;
            }
            if (secondPhase && source.getDirectEntity() instanceof Projectile)
                return false;

            return super.hurt(source, amount);
        }
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
    public void aiStep() {
        if (isSpawning) {
            spawnTimer--;

            if (spawnTimer % 10 == 0) {
                float healAmount = this.getMaxHealth() / 20.0F;
                this.heal(healAmount);

                if (level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS,
                            this.getX() + (this.random.nextDouble() - 0.5) * 2.0,
                            this.getY() + this.random.nextDouble() * 3.0,
                            this.getZ() + (this.random.nextDouble() - 0.5) * 2.0,
                            1, 0, 0, 0, 0
                    );
                    if (spawnTimer % 50 == 0) {
                        this.level().playSound(
                                null,
                                this.blockPosition(),
                                SoundEvents.WITHER_SPAWN,
                                SoundSource.HOSTILE,
                                1.0F,
                                1.0F
                        );
                    }
                }
            }

            if (spawnTimer <= 0) {
                isSpawning = false;
                isInSpawnAnimation = false;
                this.setInvulnerableTicks(0);
                this.setHealth(this.getMaxHealth());

                if (!this.isSilent()) {
                    this.level().levelEvent(1023, this.blockPosition(), 0);
                }
            }

            return; // 生成期间不执行其他AI逻辑
        }

        Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.6, 1.0);
        if (!this.level().isClientSide && this.getAlternativeTarget(0) > 0) {
            Entity entity = this.level().getEntity(this.getAlternativeTarget(0));
            if (entity != null) {
                double d0 = vec3.y;
                if (this.getY() < entity.getY() || !this.isPowered() && this.getY() < entity.getY() + 5.0) {
                    d0 = Math.max(0.0, d0);
                    d0 += 0.3 - d0 * 0.6F;
                }

                vec3 = new Vec3(vec3.x, d0, vec3.z);
                Vec3 vec31 = new Vec3(entity.getX() - this.getX(), 0.0, entity.getZ() - this.getZ());
                if (vec31.horizontalDistanceSqr() > 9.0) {
                    Vec3 vec32 = vec31.normalize();
                    vec3 = vec3.add(vec32.x * 0.3 - vec3.x * 0.6, 0.0, vec32.z * 0.3 - vec3.z * 0.6);
                }
            }
        }

        this.setDeltaMovement(vec3);

        if (vec3.horizontalDistanceSqr() > 0.05) {
            this.setYRot((float) Mth.atan2(vec3.z, vec3.x) * (180F / (float)Math.PI) - 90F);
        }

        super.aiStep();

        for (int i = 0; i < 2; i++) {
            this.yRotOHeads[i] = this.yRotHeads[i];
            this.xRotOHeads[i] = this.xRotHeads[i];
        }

        for (int j = 0; j < 2; j++) {
            int k = this.getAlternativeTarget(j + 1);
            Entity entity1 = k > 0 ? this.level().getEntity(k) : null;

            if (entity1 != null) {
                double dx = entity1.getX() - this.getHeadX(j + 1);
                double dy = entity1.getEyeY() - this.getHeadY(j + 1);
                double dz = entity1.getZ() - this.getHeadZ(j + 1);
                double dist = Math.sqrt(dx * dx + dz * dz);

                float yaw = (float)(Mth.atan2(dz, dx) * 180.0F / Math.PI) - 90F;
                float pitch = (float)(-(Mth.atan2(dy, dist) * 180.0F / Math.PI));

                this.xRotHeads[j] = this.rotlerp(this.xRotHeads[j], pitch, 40F);
                this.yRotHeads[j] = this.rotlerp(this.yRotHeads[j], yaw, 10F);
            } else {
                this.yRotHeads[j] = this.rotlerp(this.yRotHeads[j], this.yBodyRot, 10F);
            }
        }

        boolean powered = this.isPowered();

        for (int i = 0; i < 3; i++) {
            double x = this.getHeadX(i);
            double y = this.getHeadY(i);
            double z = this.getHeadZ(i);

            float f = 0.3F * this.getScale();

            this.level().addParticle(ParticleTypes.SMOKE,
                    x + this.random.nextGaussian() * f,
                    y + this.random.nextGaussian() * f,
                    z + this.random.nextGaussian() * f,
                    0, 0, 0);

            if (powered && this.random.nextInt(4) == 0) {
                this.level().addParticle(
                        ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.7F, 0.7F, 0.5F),
                        x + this.random.nextGaussian() * f,
                        y + this.random.nextGaussian() * f,
                        z + this.random.nextGaussian() * f,
                        0, 0, 0);
            }
        }


        this.setDeltaMovement(vec3);
        if (vec3.horizontalDistanceSqr() > 0.05) {
            this.setYRot((float)Mth.atan2(vec3.z, vec3.x) * (180.0F / (float)Math.PI) - 90.0F);
        }

        super.aiStep();

        for (int i = 0; i < 2; i++) {
            this.yRotOHeads[i] = this.yRotHeads[i];
            this.xRotOHeads[i] = this.xRotHeads[i];
        }

        for (int j = 0; j < 2; j++) {
            int k = this.getAlternativeTarget(j + 1);
            Entity entity1 = null;
            if (k > 0) {
                entity1 = this.level().getEntity(k);
            }

            if (entity1 != null) {
                double d9 = this.getHeadX(j + 1);
                double d1 = this.getHeadY(j + 1);
                double d3 = this.getHeadZ(j + 1);
                double d4 = entity1.getX() - d9;
                double d5 = entity1.getEyeY() - d1;
                double d6 = entity1.getZ() - d3;
                double d7 = Math.sqrt(d4 * d4 + d6 * d6);
                float f1 = (float)(Mth.atan2(d6, d4) * 180.0F / (float)Math.PI) - 90.0F;
                float f2 = (float)(-(Mth.atan2(d5, d7) * 180.0F / (float)Math.PI));
                this.xRotHeads[j] = this.rotlerp(this.xRotHeads[j], f2, 40.0F);
                this.yRotHeads[j] = this.rotlerp(this.yRotHeads[j], f1, 10.0F);
            } else {
                this.yRotHeads[j] = this.rotlerp(this.yRotHeads[j], this.yBodyRot, 10.0F);
            }
        }

        boolean flag = this.isPowered();

        for (int l = 0; l < 3; l++) {
            double d8 = this.getHeadX(l);
            double d10 = this.getHeadY(l);
            double d2 = this.getHeadZ(l);
            float f = 1.5F * this.getScale();
            this.level()
                    .addParticle(
                            ParticleTypes.WHITE_ASH,
                            d8 + this.random.nextGaussian() * (double)f,
                            d10 + this.random.nextGaussian() * (double)f,
                            d2 + this.random.nextGaussian() * (double)f,
                            0.0,
                            0.0,
                            0.0
                    );
            if (flag && this.level().random.nextInt(4) == 0) {
                this.level()
                        .addParticle(
                                ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.7F, 0.7F, 0.5F),
                                d8 + this.random.nextGaussian() * (double)f,
                                d10 + this.random.nextGaussian() * (double)f,
                                d2 + this.random.nextGaussian() * (double)f,
                                0.0,
                                0.0,
                                0.0
                        );
            }
        }

        if (this.getInvulnerableTicks() > 0) {
            float f3 = 3.3F * this.getScale();

            for (int i1 = 0; i1 < 3; i1++) {
                this.level()
                        .addParticle(
                                ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.7F, 0.7F, 0.9F),
                                this.getX() + this.random.nextGaussian(),
                                this.getY() + (double)(this.random.nextFloat() * f3),
                                this.getZ() + this.random.nextGaussian(),
                                0.0,
                                0.0,
                                0.0
                        );
            }
        }
    }

    private float rotlerp(float angle, float targetAngle, float max) {
        float f = Mth.wrapDegrees(targetAngle - angle);
        if (f > max) {
            f = max;
        }

        if (f < -max) {
            f = -max;
        }

        return angle + f;
    }

    @Override
    protected void customServerAiStep() {
        if (this.getInvulnerableTicks() > 0) {
            int j = this.getInvulnerableTicks() - 1;
            this.bossEvent.setProgress(1.0F - (float)j / 220.0F);
            if (j <= 0) {
                this.level().explode(this, this.getX(), this.getEyeY(), this.getZ(), 7.0F, false, Level.ExplosionInteraction.MOB);
                if (!this.isSilent()) {
                    this.level().globalLevelEvent(1023, this.blockPosition(), 0);
                }
            }

            this.setInvulnerableTicks(j);
            if (this.tickCount % 10 == 0) {
                this.heal(10.0F);
            }
        } else {
            super.customServerAiStep();

            for (int i = 1; i < 3; i++) {
                if (this.tickCount >= this.nextHeadUpdate[i - 1]) {
                    this.nextHeadUpdate[i - 1] = this.tickCount + 10 + this.random.nextInt(10);
                    if ((this.level().getDifficulty() == Difficulty.NORMAL || this.level().getDifficulty() == Difficulty.HARD)
                            && this.idleHeadUpdates[i - 1]++ > 15) {
                        float f = 10.0F;
                        float f1 = 5.0F;
                        double d0 = Mth.nextDouble(this.random, this.getX() - 10.0, this.getX() + 10.0);
                        double d1 = Mth.nextDouble(this.random, this.getY() - 5.0, this.getY() + 5.0);
                        double d2 = Mth.nextDouble(this.random, this.getZ() - 10.0, this.getZ() + 10.0);
                        this.performRangedAttack(i + 1, d0, d1, d2, true);
                        this.idleHeadUpdates[i - 1] = 0;
                    }

                    int k = this.getAlternativeTarget(i);
                    if (k > 0) {
                        LivingEntity livingentity = (LivingEntity)this.level().getEntity(k);
                        if (livingentity != null
                                && this.canAttack(livingentity)
                                && !(this.distanceToSqr(livingentity) > 900.0)
                                && this.hasLineOfSight(livingentity)) {
                            this.performRangedAttack(i + 1, livingentity);
                            this.nextHeadUpdate[i - 1] = this.tickCount + 40 + this.random.nextInt(20);
                            this.idleHeadUpdates[i - 1] = 0;
                        } else {
                            this.setAlternativeTarget(i, 0);
                        }
                    } else {
                        List<LivingEntity> list = this.level()
                                .getNearbyEntities(LivingEntity.class, TARGETING_CONDITIONS, this, this.getBoundingBox().inflate(20.0, 8.0, 20.0));
                        if (!list.isEmpty()) {
                            LivingEntity livingentity1 = list.get(this.random.nextInt(list.size()));
                            this.setAlternativeTarget(i, livingentity1.getId());
                        }
                    }
                }
            }

            if (this.getTarget() != null) {
                this.setAlternativeTarget(0, this.getTarget().getId());
            } else {
                this.setAlternativeTarget(0, 0);
            }

            if (this.destroyBlocksTick > 0) {
                this.destroyBlocksTick--;
                if (this.destroyBlocksTick == 0 && net.neoforged.neoforge.event.EventHooks.canEntityGrief(this.level(), this)) {
                    boolean flag = false;
                    int l = Mth.floor(this.getBbWidth() / 2.0F + 1.0F);
                    int i1 = Mth.floor(this.getBbHeight());

                    for (BlockPos blockpos : BlockPos.betweenClosed(
                            this.getBlockX() - l, this.getBlockY(), this.getBlockZ() - l, this.getBlockX() + l, this.getBlockY() + i1, this.getBlockZ() + l
                    )) {
                        BlockState blockstate = this.level().getBlockState(blockpos);
                        if (blockstate.canEntityDestroy(this.level(), blockpos, this) && net.neoforged.neoforge.event.EventHooks.onEntityDestroyBlock(this, blockpos, blockstate)) {
                            flag = this.level().destroyBlock(blockpos, true, this) || flag;
                        }
                    }

                    if (flag) {
                        this.level().levelEvent(null, 1022, this.blockPosition(), 0);
                    }
                }
            }

            if (this.tickCount % 20 == 0) {
                this.heal(5.0F);
            }

            this.bossEvent.setProgress(Math.max(0.0F, Math.min(1.0F, this.getHealth() / this.getMaxHealth())));
        }
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

    public float getHeadYRot(int head) {
        return this.yRotHeads[head];
    }

    public float getHeadXRot(int head) {
        return this.xRotHeads[head];
    }

    public boolean isInSpawnAnimation() {
        return this.isInSpawnAnimation;
    }

    public float getSpawnAnimationProgress() {
        if (!isSpawning) return 1.0F;
        return 1.0F - (float)spawnTimer / 200.0F;
    }
    public int getAlternativeTarget(int head) {
        return this.entityData.get(DATA_TARGETS.get(head));
    }

    public void setInvulnerableTicks(int invulnerableTicks) {
        this.entityData.set(DATA_ID_INV, invulnerableTicks);
    }

    public int getInvulnerableTicks() {
        return this.entityData.get(DATA_ID_INV);
    }

    public void setAlternativeTarget(int targetOffset, int newId) {
        this.entityData.set(DATA_TARGETS.get(targetOffset), newId);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TARGET_A, 0);
        builder.define(DATA_TARGET_B, 0);
        builder.define(DATA_TARGET_C, 0);
        builder.define(DATA_ID_INV, 0);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.WITHER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
    }

    public void makeInvulnerable() {
        this.setInvulnerableTicks(220);
        this.bossEvent.setProgress(0.0F);
        this.setHealth(this.getMaxHealth() / 3.0F);
    }

    @Deprecated
    public static boolean canDestroy(BlockState state) {
        return !state.isAir() && !state.is(BlockTags.WITHER_IMMUNE);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return false;
    }

    @Override
    public boolean isPowered() {
        return this.getHealth() <= this.getMaxHealth() / 2.0F;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return false;
    }


    class WitherDoNothingGoal extends Goal {
        public WitherDoNothingGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return ExtremeWitherEntity.this.getInvulnerableTicks() > 0;
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffect) {
        return potioneffect.is(MobEffects.WITHER) ? false : super.canBeAffected(potioneffect);
    }
}
