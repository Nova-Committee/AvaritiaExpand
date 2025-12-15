package committee.nova.avaritia_expand.common.item.armor.blaze;


import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;


public class BlazeArmorItem extends ArmorItem {

    public BlazeArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    public static boolean isBlazeArmor(ItemStack stack) {
        return stack.getItem() instanceof BlazeArmorItem;
    }
}
