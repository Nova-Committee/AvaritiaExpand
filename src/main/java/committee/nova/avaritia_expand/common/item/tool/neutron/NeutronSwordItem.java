package committee.nova.avaritia_expand.common.item.tool.neutron;

import committee.nova.mods.avaritia.api.iface.transform.IToolTransform;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NeutronSwordItem extends SwordItem implements IToolTransform {


    public NeutronSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && !player.getCooldowns().isOnCooldown(this)) {

            Vec3 targetPos = player.pick(64.0D, 0.0F, false).getLocation();

            player.teleportTo(targetPos.x, targetPos.y, targetPos.z);

            player.getCooldowns().addCooldown(this, 60);
            level.playSound(null, player.getOnPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.25F);
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {

        player.heal(0.5f);
        return super.onLeftClickEntity(stack, player, entity);
    }
}
