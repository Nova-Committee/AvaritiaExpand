package committee.nova.avaritia_expand.common.item;

import committee.nova.avaritia_expand.util.AEToolUtils;
import committee.nova.mods.avaritia.api.common.enchant.InitEnchantment;
import committee.nova.mods.avaritia.api.iface.item.ISwitchable;
import committee.nova.mods.avaritia.api.iface.item.InitEnchantItem;
import committee.nova.mods.avaritia.api.iface.transform.IToolTransform;
import committee.nova.mods.avaritia.init.config.ModConfig;
import committee.nova.mods.avaritia.util.ToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class NeutronPickaxeItem extends PickaxeItem implements IToolTransform, ISwitchable, InitEnchantItem {

    private final InitEnchantment initEnchantment;

    public NeutronPickaxeItem(Tier p_42961_, Properties p_42964_) {
        super(p_42961_, p_42964_);
        this.initEnchantment = new InitEnchantment(Enchantments.EFFICIENCY, 5);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching()) {
            this.switchMode(world, player, hand, "range");
            return InteractionResultHolder.success(stack);
        }
            return super.use(world, player, hand);
    }
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity miningEntity) {
        if (miningEntity instanceof ServerPlayer player) {
            if (this.isActive(stack, "range")) {
                AEToolUtils.rangeMineBlock(player,pos,3, ToolUtils.materialsPick);
            }
        }

        return false;
    }

    public int getInitEnchantLevel(ItemStack stack, Holder<Enchantment> enchantment) {
        return this.initEnchantment.getLevel(enchantment);
    }
}
