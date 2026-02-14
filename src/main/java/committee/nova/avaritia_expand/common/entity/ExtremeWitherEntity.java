package committee.nova.avaritia_expand.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ExtremeWitherEntity extends WitherBoss implements PowerableMob, RangedAttackMob {

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

}
