package committee.nova.avaritia_expand.common.entity;

import committee.nova.avaritia_expand.init.registry.AEBlocks;
import committee.nova.avaritia_expand.init.registry.AEConfig;
import committee.nova.avaritia_expand.init.registry.AEEntities;
import committee.nova.mods.avaritia.init.registry.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InfinityTntEntity extends Entity implements TraceableEntity {

    private static final EntityDataAccessor<Integer> DATA_FUSE_ID =
            SynchedEntityData.defineId(InfinityTntEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID =
            SynchedEntityData.defineId(InfinityTntEntity.class, EntityDataSerializers.BLOCK_STATE);


    private static final int BASE_BLOCKS_PER_TICK = AEConfig.infinityTntMaxExplosionTime.get();

    private boolean exploding = false;
    private int explosionIndex = 0;
    private int explosionRadius = 0;
    private final int fuseTime = 80;
    private static final float DAMAGE_PER_TICK = 10000.0f;

    private List<BlockPos> cachedExplosionBlocks;
    private boolean playedExplosionStartSound = false;

    private int currentTick = 0;
    private int totalTicks = 0;

    @Nullable
    private LivingEntity owner;

    public InfinityTntEntity(EntityType<? extends InfinityTntEntity> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
    }

    public InfinityTntEntity(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(AEEntities.INFINITY_TNT_ENTITY.get(), level);
        this.setPos(x, y, z);
        this.setDeltaMovement(
                -Math.sin(level.random.nextDouble() * Math.PI * 2) * 0.02,
                0.2,
                -Math.cos(level.random.nextDouble() * Math.PI * 2) * 0.02
        );
        this.setFuse(fuseTime);
        this.owner = owner;

        if (!level.isClientSide) {
            level.playSound(null, x, y, z, SoundEvents.BEACON_ACTIVATE, this.getSoundSource(), 1.0F, 1.0F);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_FUSE_ID, fuseTime);
        builder.define(DATA_BLOCK_STATE_ID, AEBlocks.infinity_tnt_block.get().defaultBlockState());
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04;
    }

    @Override
    public void tick() {
        if (!this.exploding) {

            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.98));

            int fuse = this.getFuse() - 1;
            this.setFuse(fuse);

            if (fuse <= 0 && !this.level().isClientSide) {
                this.startExplosion();
            }
        }

        if (this.exploding && !this.level().isClientSide) {

            this.processExplosionTick();
            this.applyDamageToEntities();
        }
    }

    private void applyDamageToEntities() {
        double progress = (double) this.explosionIndex / Math.max(1, this.cachedExplosionBlocks.size());
        double currentRadius = progress * this.explosionRadius;

        AABB aabb = new AABB(
                this.blockPosition().getX() - currentRadius,
                this.blockPosition().getY() - currentRadius,
                this.blockPosition().getZ() - currentRadius,
                this.blockPosition().getX() + currentRadius,
                this.blockPosition().getY() + currentRadius,
                this.blockPosition().getZ() + currentRadius
        );
        List<Entity> entities = this.level().getEntities(this, aabb);
        DamageSource damageSource = ModDamageTypes.causeRandomDamage(this);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.hurt(damageSource, DAMAGE_PER_TICK);
            } else if (entity instanceof Player player) {
                return;
            }
        }
    }

    private void startExplosion() {
        if (this.exploding) return;

        this.exploding = true;
        this.setDeltaMovement(Vec3.ZERO);

        this.explosionRadius = Math.toIntExact(AEConfig.infinityTntMaxRange.get());

        BlockPos center = this.blockPosition();
        this.cachedExplosionBlocks = collectSphereBlocks(center, this.explosionRadius);

        this.currentTick = 0;
        this.totalTicks = calculateOptimalTicks(this.cachedExplosionBlocks.size());

        if (!this.level().isClientSide) {
            Vec3 pos = this.position();
            this.level().playSound(
                    null,
                    pos.x, pos.y, pos.z,
                    SoundEvents.GENERIC_EXPLODE,
                    SoundSource.BLOCKS,
                    10.0F,
                    0.8F
            );

            this.level().playSound(
                    null,
                    pos.x, pos.y, pos.z,
                    SoundEvents.DRAGON_FIREBALL_EXPLODE,
                    SoundSource.BLOCKS,
                    8.0F,
                    1.0F
            );
            this.playedExplosionStartSound = true;
        }
    }

    private int calculateOptimalTicks(int totalBlocks) {
        double targetSeconds = 15.0;
        return (int) (targetSeconds * 20);
    }

    private void processExplosionTick() {
        this.currentTick++;

        double progress = (double) this.currentTick / this.totalTicks;
        double accelerationMultiplier = 1.0 + (progress * progress * 4.0);

        int blocksToProcess = (int) (BASE_BLOCKS_PER_TICK * accelerationMultiplier);
        blocksToProcess = Math.max(BASE_BLOCKS_PER_TICK, blocksToProcess);
        blocksToProcess = Math.min(blocksToProcess, 500000);

        int end = Math.min(
                this.explosionIndex + blocksToProcess,
                this.cachedExplosionBlocks.size()
        );

        for (int i = this.explosionIndex; i < end; i++) {
            BlockPos pos = this.cachedExplosionBlocks.get(i);
            BlockState state = this.level().getBlockState(pos);

            if (!state.isAir() && !state.is(Blocks.BEDROCK)) {
                this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 18);
            }
        }

        this.explosionIndex = end;

        if (this.explosionIndex >= this.cachedExplosionBlocks.size()) {
            finishExplosion();
        }
    }

    private void finishExplosion() {

        if (!this.level().isClientSide) {
            Vec3 pos = this.position();
            this.level().playSound(
                    null,
                    pos.x, pos.y, pos.z,
                    SoundEvents.GENERIC_EXPLODE,
                    SoundSource.BLOCKS,
                    12.0F,
                    0.7F
            );

            this.level().playSound(
                    null,
                    pos.x, pos.y, pos.z,
                    SoundEvents.WARDEN_SONIC_BOOM,
                    SoundSource.BLOCKS,
                    8.0F,
                    1.2F
            );
        }

        this.level().levelEvent(2001, this.blockPosition(), Block.getId(Blocks.TNT.defaultBlockState()));
        this.discard();
    }

    private List<BlockPos> collectSphereBlocks(BlockPos center, int radius) {
        List<BlockPos> list = new ArrayList<>(radius * radius * 2);
        int r2 = radius * radius;

        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= r2) {
                        list.add(new BlockPos(cx + x, cy + y, cz + z));
                    }
                }
            }
        }

        list.sort((pos1, pos2) -> {
            double dist1 = pos1.distSqr(center);
            double dist2 = pos2.distSqr(center);
            return Double.compare(dist1, dist2);
        });

        return list;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putShort("fuse", (short) this.getFuse());
        tag.put("block_state", NbtUtils.writeBlockState(this.getBlockState()));
        tag.putBoolean("played_explosion_start_sound", this.playedExplosionStartSound);
        tag.putInt("explosion_index", this.explosionIndex);
        tag.putInt("current_tick", this.currentTick);
        tag.putInt("total_ticks", this.totalTicks);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setFuse(tag.getShort("fuse"));
        if (tag.contains("block_state", 10)) {
            this.setBlockState(
                    NbtUtils.readBlockState(
                            this.level().holderLookup(Registries.BLOCK),
                            tag.getCompound("block_state")
                    )
            );
        }
        if (tag.contains("played_explosion_start_sound")) {
            this.playedExplosionStartSound = tag.getBoolean("played_explosion_start_sound");
        }
        if (tag.contains("explosion_index")) {
            this.explosionIndex = tag.getInt("explosion_index");
        }
        if (tag.contains("current_tick")) {
            this.currentTick = tag.getInt("current_tick");
        }
        if (tag.contains("total_ticks")) {
            this.totalTicks = tag.getInt("total_ticks");
        }
    }

    public void setFuse(int fuse) {
        this.entityData.set(DATA_FUSE_ID, fuse);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE_ID);
    }

    public void setBlockState(BlockState state) {
        this.entityData.set(DATA_BLOCK_STATE_ID, state);
    }

    public BlockState getBlockState() {
        return this.entityData.get(DATA_BLOCK_STATE_ID);
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    public void restoreFrom(Entity entity) {
        super.restoreFrom(entity);
        if (entity instanceof InfinityTntEntity tnt) {
            this.owner = tnt.owner;
            this.playedExplosionStartSound = tnt.playedExplosionStartSound;
            this.explosionIndex = tnt.explosionIndex;
            this.currentTick = tnt.currentTick;
            this.totalTicks = tnt.totalTicks;
        }
    }

    @Nullable
    @Override
    public Entity changeDimension(DimensionTransition transition) {
        return super.changeDimension(transition);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.BLOCKS;
    }
}
