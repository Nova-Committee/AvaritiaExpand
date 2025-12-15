package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.item.armor.blaze.*;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalBootsItem;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalChestplateItem;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalHelmetItem;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalLeggingsItem;
import committee.nova.avaritia_expand.common.item.tool.neutron.*;
import committee.nova.avaritia_expand.init.registry.enums.AEToolTiers;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.minecraft.world.item.DiggerItem.createAttributes;


public class AEItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(    AvaritiaExpand.MOD_ID);

        public static DeferredItem<Item> neutron_sword = ITEMS.register("neutron_sword",
                ()-> new NeutronSwordItem(AEToolTiers.NEUTRON,new Item.Properties()
                        .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(AEToolTiers.NEUTRON, 0, AEToolTiers.NEUTRON.getSpeed())).durability(8888)));
        public static DeferredItem<Item> neutron_axe = ITEMS.register("neutron_axe",
            ()-> new NeutronAxeItem(AEToolTiers.NEUTRON,new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(AEToolTiers.NEUTRON, 0, AEToolTiers.NEUTRON.getSpeed())).durability(8888)));
        public static DeferredItem<Item> neutron_pickaxe = ITEMS.register("neutron_pickaxe",
            ()-> new NeutronPickaxeItem(AEToolTiers.NEUTRON,new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(AEToolTiers.NEUTRON, 0, AEToolTiers.NEUTRON.getSpeed())).durability(8888)));
        public static DeferredItem<Item> neutron_shovel = ITEMS.register("neutron_shovel",
            ()-> new NeutronShovelItem(AEToolTiers.NEUTRON,new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(AEToolTiers.NEUTRON, 0, AEToolTiers.NEUTRON.getSpeed())).durability(8888)));
        public static DeferredItem<Item> neutron_hoe = ITEMS.register("neutron_hoe",
            ()-> new NeutronHoeItem(AEToolTiers.NEUTRON,new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(AEToolTiers.NEUTRON, 0, AEToolTiers.NEUTRON.getSpeed())).durability(8888)));

        public static  DeferredItem<ArmorItem> blaze_helmet = ITEMS.register("blaze_helmet",
            () -> new BlazeHelmetItem(AEArmorMaterials.BLAZE, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(7777).fireResistant()));
        public static  DeferredItem<ArmorItem> blaze_chestplate = ITEMS.register("blaze_chestplate",
            () -> new BlazeChestplateItem(AEArmorMaterials.BLAZE, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(7777).fireResistant()));
        public static  DeferredItem<ArmorItem> blaze_leggings = ITEMS.register("blaze_leggings",
            () -> new BlazeLeggingsItem(AEArmorMaterials.BLAZE, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(7777).fireResistant()));
        public static  DeferredItem<ArmorItem> blaze_boots = ITEMS.register("blaze_boots",
            () -> new BlazeBootsItem(AEArmorMaterials.BLAZE, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(7777).fireResistant()));

    public static  DeferredItem<ArmorItem> crystal_helmet = ITEMS.register("crystal_helmet",
            () -> new CrystalHelmetItem(AEArmorMaterials.CRYSTAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(8888)));
    public static  DeferredItem<ArmorItem> crystal_chestplate = ITEMS.register("crystal_chestplate",
            () -> new CrystalChestplateItem(AEArmorMaterials.CRYSTAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(8888)));
    public static  DeferredItem<ArmorItem> crystal_leggings = ITEMS.register("crystal_leggings",
            () -> new CrystalLeggingsItem(AEArmorMaterials.CRYSTAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(8888)));
    public static  DeferredItem<ArmorItem> crystal_boots = ITEMS.register("crystal_boots",
            () -> new CrystalBootsItem(AEArmorMaterials.CRYSTAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(8888)));


    public static DeferredItem<Item> star = ITEMS.register("star",
            ()-> new Item(new Item.Properties().fireResistant().rarity(ModRarities.COSMIC.getValue()).stacksTo(16)));
    public static DeferredItem<Item> star_dessert = ITEMS.register("star_dessert",
            ()-> new Item(new Item.Properties().fireResistant().rarity(ModRarities.EPIC).stacksTo(16).food(AEFoods.star_dessert)));
    public static DeferredItem<Item> singularity_stew = ITEMS.register("singularity_stew",
            ()-> new Item(new Item.Properties().fireResistant().rarity(ModRarities.EPIC).stacksTo(16).food(AEFoods.star_dessert)));




    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
