package committee.nova.avaritia_expand.init.handler;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.item.tool.neutron.NeutronSwordItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.Random;

@EventBusSubscriber(modid = AvaritiaExpand.MOD_ID)

public class ExpandHandler {
    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack mainHandItem = player.getMainHandItem();
        if (mainHandItem.getItem() instanceof NeutronSwordItem) {
            Level world = player.level();
            Random random = new Random();

            double offsetX = (random.nextBoolean() ? 1 : -1) * 1.0;
            BlockPos originalPos = player.blockPosition();
            BlockPos offsetPos = originalPos.offset((int) offsetX, 0, 0);

            if (world.isEmptyBlock(offsetPos)) {
                player.teleportTo(offsetPos.getX() + 0.5, offsetPos.getY(), offsetPos.getZ() + 0.5);

            }
        }
    }
}
