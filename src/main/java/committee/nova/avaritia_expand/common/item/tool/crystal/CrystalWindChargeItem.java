package committee.nova.avaritia_expand.common.item.tool.crystal;

import committee.nova.avaritia_expand.common.entity.CrystalWindCharge;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CrystalWindChargeItem extends Item implements ProjectileItem {
    private static final int COOLDOWN = 10;

    public CrystalWindChargeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            CrystalWindCharge crystalWindCharge = new CrystalWindCharge(player, level, player.position().x(), player.getEyePosition().y(), player.position().z());
            crystalWindCharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(crystalWindCharge);
        }

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.WIND_CHARGE_THROW,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );

        player.getCooldowns().addCooldown(this, COOLDOWN);
        player.awardStat(Stats.ITEM_USED.get(this));
        itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        RandomSource randomsource = level.getRandom();
        double d0 = randomsource.triangle((double)direction.getStepX(), 0.11485000000000001);
        double d1 = randomsource.triangle((double)direction.getStepY(), 0.11485000000000001);
        double d2 = randomsource.triangle((double)direction.getStepZ(), 0.11485000000000001);
        Vec3 vec3 = new Vec3(d0, d1, d2);
        CrystalWindCharge crystalWindCharge = new CrystalWindCharge(level, pos.x(), pos.y(), pos.z(), vec3);
        crystalWindCharge.setDeltaMovement(vec3);
        return crystalWindCharge;
    }

    @Override
    public void shoot(Projectile projectile, double x, double y, double z, float velocity, float inaccuracy) {

    }

    @Override
    public DispenseConfig createDispenseConfig() {
        return DispenseConfig.builder()
                .positionFunction((source, stack) -> source.center())
                .uncertainty(6.6666665F)
                .power(1.0F)
                .overrideDispenseEvent(1051)
                .build();
    }
}
