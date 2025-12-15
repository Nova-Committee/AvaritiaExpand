package committee.nova.avaritia_expand.common.item.armor.crystal;

import committee.nova.avaritia_expand.common.item.armor.blaze.BlazeArmorItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class CrystalArmorItem extends ArmorItem {

    public CrystalArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    public static boolean isCrystalArmor(ItemStack stack) {
        return stack.getItem() instanceof CrystalArmorItem;
    }
}
