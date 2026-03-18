package committee.nova.avaritia_expand.common.entity;

import committee.nova.avaritia_expand.init.registry.AEEntities;
import committee.nova.avaritia_expand.init.registry.AEItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NeutronMinecart extends Minecart {

    public NeutronMinecart(EntityType<?> type, Level level) {
        super(type, level);
    }

    public NeutronMinecart(Level level, double x, double y, double z) {
        this(AEEntities.NEUTRON_MINECART.get(), level);
        this.setPos(x, y, z);
    }
    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;

        Vec3 motion = this.getDeltaMovement();
        double speed = motion.horizontalDistance();

        if (speed > 0.4) {
            for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(0.3))) {

                if (!(entity instanceof LivingEntity living)) continue;
                if (entity == this.getFirstPassenger()) continue;

                Vec3 pushDir = motion.normalize();

                double strength = speed * 1.5;

                living.push(
                        pushDir.x * strength,
                        0.4 + speed * 0.5,
                        pushDir.z * strength
                );

                living.hurt(this.damageSources().fallingBlock( this), (float)(speed * 5));
            }
        }
    }

    @Override
    public void push(Entity entity) {

    }

    @Override
    protected Item getDropItem() {
        return AEItems.neutron_minecart.get();
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(AEItems.neutron_minecart.get());
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }
}