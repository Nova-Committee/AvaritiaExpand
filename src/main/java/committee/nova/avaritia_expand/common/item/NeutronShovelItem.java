package committee.nova.avaritia_expand.common.item;

import committee.nova.mods.avaritia.api.common.enchant.InitEnchantment;
import committee.nova.mods.avaritia.api.iface.item.InitEnchantItem;
import committee.nova.mods.avaritia.api.iface.transform.IToolTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NeutronShovelItem extends ShovelItem implements IToolTransform , InitEnchantItem {

    private final InitEnchantment initEnchantment;

    public NeutronShovelItem(Tier p_43114_, Properties p_43117_) {
        super(p_43114_, p_43117_);
        this.initEnchantment = new InitEnchantment(Enchantments.EFFICIENCY, 5);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (miningEntity instanceof Player player) {

            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 1));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 40, 0));
        }
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {

        player.heal(0.2f);
        return super.onLeftClickEntity(stack, player, entity);
    }

    public int getInitEnchantLevel(ItemStack stack, Holder<Enchantment> enchantment) {
        return this.initEnchantment.getLevel(enchantment);
    }


}
