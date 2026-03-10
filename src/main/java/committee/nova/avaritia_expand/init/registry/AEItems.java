package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.item.armor.blaze.*;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalBootsItem;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalChestplateItem;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalHelmetItem;
import committee.nova.avaritia_expand.common.item.armor.crystal.CrystalLeggingsItem;
import committee.nova.avaritia_expand.common.item.misc.WitherStarItem;
import committee.nova.avaritia_expand.common.item.misc.WitherTotemItem;
import committee.nova.avaritia_expand.common.item.tool.blaze.BlazeTotemItem;
import committee.nova.avaritia_expand.common.item.tool.blaze.BlazeWindChargeItem;
import committee.nova.avaritia_expand.common.item.tool.crystal.CrystalWindChargeItem;
import committee.nova.avaritia_expand.common.item.tool.infinity.InfinityBookItem;
import committee.nova.avaritia_expand.common.item.tool.infinity.InfinityBottleItem;
import committee.nova.avaritia_expand.common.item.tool.blaze.BlazePortableBurnerItem;
import committee.nova.avaritia_expand.common.item.tool.infinity.InfinityShearsItem;
import committee.nova.avaritia_expand.common.item.tool.neutron.*;
import committee.nova.mods.avaritia.init.registry.ModDataComponents;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia.init.registry.ModToolTiers;
import committee.nova.mods.avaritia.init.registry.modes.ToolMode;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.minecraft.world.item.DiggerItem.createAttributes;


public class AEItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AvaritiaExpand.MOD_ID);

        public static DeferredItem<Item> neutron_sword = ITEMS.register("neutron_sword",
                ()-> new NeutronSwordItem(ModToolTiers.CRYSTAL,new Item.Properties()
                        .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(ModToolTiers.CRYSTAL, 0, -2)).component(ModDataComponents.TOOL_MODE, ToolMode.DEFAULT)));
        public static DeferredItem<Item> neutron_axe = ITEMS.register("neutron_axe",
            ()-> new NeutronAxeItem(ModToolTiers.CRYSTAL,new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(ModToolTiers.CRYSTAL, 0, -3)).component(ModDataComponents.TOOL_MODE, ToolMode.DEFAULT)));
        public static DeferredItem<Item> neutron_pickaxe = ITEMS.register("neutron_pickaxe",
            ()-> new NeutronPickaxeItem(ModToolTiers.CRYSTAL,new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(ModToolTiers.CRYSTAL, 0, -3)).component(ModDataComponents.TOOL_MODE, ToolMode.DEFAULT)));
        public static DeferredItem<Item> neutron_shovel = ITEMS.register("neutron_shovel",
            ()-> new NeutronShovelItem(ModToolTiers.CRYSTAL,new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(ModToolTiers.CRYSTAL, 0, -3)).component(ModDataComponents.TOOL_MODE, ToolMode.DEFAULT)));
        public static DeferredItem<Item> neutron_hoe = ITEMS.register("neutron_hoe",
            ()-> new NeutronHoeItem(ModToolTiers.CRYSTAL,new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(ModToolTiers.CRYSTAL, 0, -3)).component(ModDataComponents.TOOL_MODE, ToolMode.DEFAULT)));
        public static DeferredItem<Item> neutron_mobile = ITEMS.register("neutron_mobile",
            ()-> new NeutronMobileItem(new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1)));
        public static DeferredItem<Item> neutron_mace = ITEMS.register("neutron_mace",
            ()-> new NeutronMaceItem(new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(NeutronMaceItem.createAttributes())));
    public static DeferredItem<Item> neutron_wind_charge = ITEMS.register("neutron_wind_charge",
            ()-> new NeutronWindChargeItem(new Item.Properties().fireResistant().rarity(ModRarities.EPIC).stacksTo(1).durability(256)));




    public static  DeferredItem<ArmorItem> blaze_helmet = ITEMS.register("blaze_helmet",
            () -> new BlazeHelmetItem(AEArmorMaterials.BLAZE, ArmorItem.Type.HELMET,
                    new Item.Properties().rarity(Rarity.EPIC).durability(7777).fireResistant()));
        public static  DeferredItem<ArmorItem> blaze_chestplate = ITEMS.register("blaze_chestplate",
            () -> new BlazeChestplateItem(AEArmorMaterials.BLAZE, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.EPIC).durability(7777).fireResistant()));
        public static  DeferredItem<ArmorItem> blaze_leggings = ITEMS.register("blaze_leggings",
            () -> new BlazeLeggingsItem(AEArmorMaterials.BLAZE, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.EPIC).durability(7777).fireResistant()));
        public static  DeferredItem<ArmorItem> blaze_boots = ITEMS.register("blaze_boots",
            () -> new BlazeBootsItem(AEArmorMaterials.BLAZE, ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.EPIC).durability(7777).fireResistant()));

    public static  DeferredItem<ArmorItem> crystal_helmet = ITEMS.register("crystal_helmet",
            () -> new CrystalHelmetItem(AEArmorMaterials.CRYSTAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(8888).rarity(Rarity.EPIC)));
    public static  DeferredItem<ArmorItem> crystal_chestplate = ITEMS.register("crystal_chestplate",
            () -> new CrystalChestplateItem(AEArmorMaterials.CRYSTAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(8888).rarity(Rarity.EPIC)));
    public static  DeferredItem<ArmorItem> crystal_leggings = ITEMS.register("crystal_leggings",
            () -> new CrystalLeggingsItem(AEArmorMaterials.CRYSTAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(8888).rarity(Rarity.EPIC)));
    public static  DeferredItem<ArmorItem> crystal_boots = ITEMS.register("crystal_boots",
            () -> new CrystalBootsItem(AEArmorMaterials.CRYSTAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(8888).rarity(Rarity.EPIC)));


    public static DeferredItem<Item> star_dessert = ITEMS.register("star_dessert",
            ()-> new Item(new Item.Properties().fireResistant().rarity(ModRarities.EPIC).stacksTo(16).food(AEFoods.star_dessert)));
    public static DeferredItem<Item> singularity_stew = ITEMS.register("singularity_stew",
            ()-> new Item(new Item.Properties().fireResistant().rarity(ModRarities.EPIC).stacksTo(16).food(AEFoods.singularity_stew)));

    public static DeferredItem<Item> blaze_portable_burner = ITEMS.register("blaze_portable_burner",
            ()-> new BlazePortableBurnerItem(new Item.Properties().fireResistant().rarity(ModRarities.UNCOMMON).stacksTo(1)));
    public static DeferredItem<Item> blaze_wind_charge = ITEMS.register("blaze_wind_charge",
            ()-> new BlazeWindChargeItem(new Item.Properties().fireResistant().rarity(ModRarities.UNCOMMON).stacksTo(1).durability(64)));
    public static DeferredItem<Item> blaze_totem = ITEMS.register("blaze_totem",
            ()-> new BlazeTotemItem(new Item.Properties().fireResistant().rarity(ModRarities.UNCOMMON).stacksTo(1)));

    public static DeferredItem<Item> crystal_wind_charge = ITEMS.register("crystal_wind_charge",
            ()-> new CrystalWindChargeItem(new Item.Properties().fireResistant().rarity(ModRarities.EPIC).stacksTo(1).durability(64)));

    public static DeferredItem<Item> infinity_bottle = ITEMS.register("infinity_bottle",
            ()-> new InfinityBottleItem(new Item.Properties().fireResistant().rarity(ModRarities.COSMIC.getValue()).stacksTo(1)));
    public static DeferredItem<Item> infinity_shears = ITEMS.register("infinity_shears",
            ()-> new InfinityShearsItem(new Item.Properties().fireResistant().rarity(ModRarities.COSMIC.getValue()).stacksTo(1)));
    public static DeferredItem<Item> infinity_book = ITEMS.register("infinity_book",
            ()-> new InfinityBookItem(new Item.Properties().fireResistant().rarity(ModRarities.COSMIC.getValue()).stacksTo(1)));


    public static DeferredItem<Item> wither_star = ITEMS.register("wither_star",
            ()-> new WitherStarItem(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static DeferredItem<Item> wither_totem = ITEMS.register("wither_totem",
            ()-> new WitherTotemItem(new Item.Properties().fireResistant().rarity(ModRarities.LEGEND.getValue()).stacksTo(1)));


    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
