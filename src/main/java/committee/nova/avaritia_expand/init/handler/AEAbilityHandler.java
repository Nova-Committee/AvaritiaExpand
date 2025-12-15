package committee.nova.avaritia_expand.init.handler;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.item.armor.blaze.*;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalBootsItem;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalChestplateItem;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalHelmetItem;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalLeggingsItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;

@EventBusSubscriber(modid = AvaritiaExpand.MOD_ID)
public class AEAbilityHandler {
    //Blaze Armor
    @SubscribeEvent
    public static void onBlazeArmorTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().level().isClientSide()) {
            Player player = event.getEntity();
            if (isWearingFullBlazeArmor(player)) {

                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0, false, false, false));

                if (player.isInLava()) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0, false, false, false));
                }
            }
        }
    }

    private static boolean isWearingFullBlazeArmor(Player player) {
        for (ItemStack armor : player.getArmorSlots()) {
            if (armor.isEmpty() || !(armor.getItem() instanceof BlazeArmorItem)) {
                return false;
            }
        }
        return true;
    }

    //Blaze Boots
    // 最低触发推力的摔落高度
    private static final float MIN_PUSH_HEIGHT = 3.0F;
    // 触发粒子效果的最低高度
    private static final float MIN_PARTICLE_HEIGHT = 5.0F;
    // 最大推力强度
    private static final double MAX_PUSH_STRENGTH = 3.0;

    @SubscribeEvent
    public static void onBlazeBootsTFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof Player player && isWearingBlazeBoots(player)) {

            float originalDamage = event.getDamageMultiplier() * event.getDistance();
            float reducedDamage = originalDamage * 0.7F;
            event.setDamageMultiplier(reducedDamage / event.getDistance());


            float fallHeight = event.getDistance();


            if (fallHeight >= MIN_PUSH_HEIGHT) {

                double pushStrength = calculatePushStrength(fallHeight);
                pushAwayNearbyEntities(player, player.level(), 2.5, pushStrength);
            }


            if (fallHeight >= MIN_PARTICLE_HEIGHT) {
                spawnParticles(player, player.level(), fallHeight);
            }
        }
    }

    private static double calculatePushStrength(float fallHeight) {

        double strength = 0.5 + (fallHeight - MIN_PUSH_HEIGHT) * 0.1;
        return Math.min(strength, MAX_PUSH_STRENGTH);
    }



    private static void pushAwayNearbyEntities(Player player, Level level, double radius, double strength) {
        AABB aabb = new AABB(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb,
                entity -> entity != player && entity.isAlive() && entity.isPushable());

        for (LivingEntity entity : entities) {
            double dx = entity.getX() - player.getX();
            double dy = entity.getY() - player.getY();
            double dz = entity.getZ() - player.getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (distance > 0) {
                entity.push(dx / distance * strength, dy / distance * strength, dz / distance * strength);
                entity.hurtMarked = true;
            }
        }
    }


    private static void spawnParticles(Player player, Level level, float fallHeight) {

        if (level.isClientSide) {

            int particleCount = (int) Math.min(10 + (fallHeight - MIN_PARTICLE_HEIGHT) * 2, 30);
            Level world = player.level();
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();


            for (int i = 0; i < particleCount; i++) {

                double offsetX = (world.random.nextDouble() - 0.5) * 2.0;
                double offsetY = world.random.nextDouble() * 1.5;
                double offsetZ = (world.random.nextDouble() - 0.5) * 2.0;


                world.addParticle(
                        ParticleTypes.FLAME,
                        x + offsetX,
                        y + 0.5 + offsetY,
                        z + offsetZ,
                        0,
                        0.1,
                        0
                );


                world.addParticle(
                        ParticleTypes.LAVA,
                        x + offsetX,
                        y + 0.5 + offsetY,
                        z + offsetZ,
                        0,
                        0.05,
                        0
                );
            }

            world.playSound(
                    player,
                    player.blockPosition(),
                    SoundEvents.ROOTED_DIRT_BREAK,
                    SoundSource.PLAYERS,
                    2F,
                    1.2F
            );
        }
    }

    private static boolean isWearingBlazeBoots(Player player) {
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        return !boots.isEmpty() && boots.getItem() instanceof BlazeBootsItem;
    }

    //Blaze Leggings
    @SubscribeEvent
    public static void onBlazeLeggingsTick(PlayerTickEvent.Post event){
        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if (isWearingBlazeLeggings(player)) {
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED,40,1,false,false,false));
            }
        }
    }

    private static boolean isWearingBlazeLeggings(Player player) {
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        return !leggings.isEmpty() && leggings.getItem() instanceof BlazeLeggingsItem;
    }
    //Blaze Chestplate

    @SubscribeEvent
    public static void onPlayerAttack(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (isWearingBlazeChestplate(player)) {
            DamageSource source = event.getSource();

            if (source.getEntity() instanceof LivingEntity attacker) {

                float originalDamage = event.getNewDamage();
                float reflectDamage = originalDamage * 0.2f;

                attacker.hurt(player.damageSources().thorns(player), reflectDamage);
            }
        }
    }

    private static boolean isWearingBlazeChestplate(Player player) {
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        return !chestplate.isEmpty() && chestplate.getItem() instanceof BlazeChestplateItem;
    }
    //Blaze Helmet

    @SubscribeEvent
    public static void onBlazeHelmetTick(PlayerTickEvent.Post event){
        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if (isWearingBlazeHelmet(player)) {
                player.removeEffect(MobEffects.BLINDNESS);
                player.removeEffect(MobEffects.DARKNESS);
            }
        }
    }

    private static boolean isWearingBlazeHelmet(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        return !helmet.isEmpty() && helmet.getItem() instanceof BlazeHelmetItem;
    }
    //Crystal Armor
    //Crystal Boots
    @SubscribeEvent
    public static void onCrystalBootsFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof Player player && isWearingCrystalBoots(player)) {
            event.setCanceled(true);
        }
    }

    private static boolean isWearingCrystalBoots(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.FEET);
        return !helmet.isEmpty() && helmet.getItem() instanceof CrystalBootsItem;
    }
    //Crystal Leggings
    @SubscribeEvent
    public static void onCrystalLeggingsTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof Player player) {
            if (isWearingCrystalLeggings(player)) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 1, false, false, false));
            }
        }
    }

    private static boolean isWearingCrystalLeggings(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.LEGS);
        return !helmet.isEmpty() && helmet.getItem() instanceof CrystalLeggingsItem;
    }
    //Crystal Chestplate
    @SubscribeEvent
    public static void onCrystalChestplateTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof Player player) {
            if (isWearingCrystalChestplate(player)) {

                player.getActiveEffects().removeIf(effect -> {
                    return !effect.getEffect().value().isBeneficial();
                });

                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 40, 0, false, false, false));
            }
        }
    }
    private static boolean isWearingCrystalChestplate(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.CHEST);
        return !helmet.isEmpty() && helmet.getItem() instanceof CrystalChestplateItem;
    }
    //Crystal Helmet

    @SubscribeEvent
    public static void onCrystalHelmetProjectileHit(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (isWearingCrystalHelmet(player)) {

            if (event.getSource().getDirectEntity() instanceof Projectile projectile) {

                Vec3 motion = projectile.getDeltaMovement();
                projectile.setDeltaMovement(motion.scale(-1.0));
                projectile.setOwner(player);

                event.setCanceled(true);
            }
        }
    }
    private static boolean isWearingCrystalHelmet(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        return !helmet.isEmpty() && helmet.getItem() instanceof CrystalHelmetItem;
    }
}
