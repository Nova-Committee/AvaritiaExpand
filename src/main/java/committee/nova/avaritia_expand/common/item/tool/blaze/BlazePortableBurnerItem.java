package committee.nova.avaritia_expand.common.item.tool.blaze;

import committee.nova.avaritia_expand.common.menu.BurnerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BlazePortableBurnerItem extends Item {
    public BlazePortableBurnerItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level worldIn, @NotNull Player playerIn, @NotNull InteractionHand handIn) {
        if (!worldIn.isClientSide && !playerIn.isCrouching()) {
            int slot = handIn == InteractionHand.MAIN_HAND ? playerIn.getInventory().selected : 40;
            playerIn.openMenu(
                    new SimpleMenuProvider((id, playerInventory, player) -> new BurnerMenu(id, playerInventory, slot), Component.translatable("item.avaritia_expand.blaze_portable_burner")),
                    buf -> buf.writeInt(slot));
        }
        return super.use(worldIn, playerIn, handIn);
    }
}
