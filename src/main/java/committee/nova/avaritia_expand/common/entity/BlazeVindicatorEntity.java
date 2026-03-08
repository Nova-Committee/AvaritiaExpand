package committee.nova.avaritia_expand.common.entity;

import javax.annotation.Nullable;

import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

public class BlazeVindicatorEntity extends AbstractIllager {

    private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(
            this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);

    private int teleportCooldown = 0;

    public BlazeVindicatorEntity(EntityType<? extends BlazeVindicatorEntity> entityType, Level level) {
        super(entityType, level);
        this.bossEvent.setVisible(true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreakBlocksGoal(this));
        this.goalSelector.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.55F)
                .add(Attributes.FOLLOW_RANGE, 64.0F)
                .add(Attributes.MAX_HEALTH, 4096.0F)
                .add(Attributes.ARMOR, 64.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7F)
                .add(Attributes.ATTACK_DAMAGE, 5.0F);
    }


    @Override
    public boolean hurt(DamageSource source, float amount) {

        if (source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.LAVA)) {
            return false;
        }

        if (amount > 100F) {
            amount = 100F;
        }

        return super.hurt(source, amount);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected void customServerAiStep() {

        super.customServerAiStep();

        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

        if (!this.level().isClientSide) {

            ServerLevel serverLevel = (ServerLevel)this.level();

            if (!serverLevel.isThundering()) {

                serverLevel.setWeatherParameters(
                        0,
                        6000,
                        true,
                        true
                );
            }

            if (teleportCooldown > 0) {
                teleportCooldown--;
            }

            Player player = this.level().getNearestPlayer(this, 64);

            if (player != null && this.distanceTo(player) > 8 && teleportCooldown == 0) {

                double x = player.getX() + (this.random.nextDouble() - 0.5) * 2;
                double y = player.getY();
                double z = player.getZ() + (this.random.nextDouble() - 0.5) * 2;

                this.teleportTo(x, y, z);

                ((ServerLevel)this.level()).sendParticles(
                        ParticleTypes.FLAME,
                        this.getX(),
                        this.getY() + 1,
                        this.getZ(),
                        40,
                        0.3,0.3,0.3,
                        0.02
                );

                teleportCooldown = 55;
            }
        }
    }



    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (!this.level().isClientSide()) {

            ServerLevel serverLevel = (ServerLevel)this.level();

            serverLevel.setWeatherParameters(
                    6000,
                    0,
                    false,
                    false
            );

            this.level().playSound(
                    null,
                    this.blockPosition(),
                    SoundEvents.VINDICATOR_DEATH,
                    SoundSource.HOSTILE,
                    3.0F,
                    0.0F
            );
        }

        this.bossEvent.removeAllPlayers();
        this.bossEvent.setVisible(false);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public AbstractIllager.IllagerArmPose getArmPose() {
        if (this.isAggressive()) {
            return AbstractIllager.IllagerArmPose.ATTACKING;
        } else {
            return this.isCelebrating() ? AbstractIllager.IllagerArmPose.CELEBRATING : AbstractIllager.IllagerArmPose.CROSSED;
        }
    }



    @Override
    public @NotNull SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        RandomSource randomsource = level.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, difficulty);
        this.populateDefaultEquipmentEnchantments(level, randomsource, difficulty);
        ((ServerLevel)this.level()).sendParticles(
                ParticleTypes.LAVA,
                this.getX(),
                this.getY() + 1,
                this.getZ(),
                80,
                0.3,0.3,0.3,
                0.02
        );
        this.playSound(SoundEvents.BLAZE_AMBIENT, 2.0F, 0.0F);

        return spawngroupdata;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.blaze_axe.get()));
    }


    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VINDICATOR_HURT;
    }

    @Override
    public void applyRaidBuffs(ServerLevel level, int wave, boolean unused) {
        ItemStack itemstack = new ItemStack(Items.IRON_AXE);
        Raid raid = this.getCurrentRaid();
        boolean flag = this.random.nextFloat() <= raid.getEnchantOdds();
        if (flag) {
            ResourceKey<EnchantmentProvider> resourcekey = wave > raid.getNumGroups(Difficulty.NORMAL)
                    ? VanillaEnchantmentProviders.RAID_VINDICATOR_POST_WAVE_5
                    : VanillaEnchantmentProviders.RAID_VINDICATOR;
            EnchantmentHelper.enchantItemFromProvider(
                    itemstack, level.registryAccess(), resourcekey, level.getCurrentDifficultyAt(this.blockPosition()), this.random
            );
        }

        this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
    }

    static class BreakBlocksGoal extends Goal {

        private final Mob mob;

        public BreakBlocksGoal(Mob mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return mob.getTarget() != null;
        }

        @Override
        public void tick() {

            if (mob.getTarget() == null) return;

            BlockPos basePos = mob.blockPosition().relative(mob.getDirection());

            for (int y = 0; y < 2; y++) {
                BlockPos blockPos = basePos.above(y);

                if (!mob.level().isEmptyBlock(blockPos)) {
                    mob.level().destroyBlock(blockPos, true, mob);
                }
            }
        }
    }

}
