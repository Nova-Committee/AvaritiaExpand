package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.item.*;
import committee.nova.avaritia_expand.init.registry.enums.AEToolTiers;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.minecraft.world.item.DiggerItem.createAttributes;


public class AEItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(    AvaritiaExpand.MOD_ID);

        public static DeferredItem<Item> neutron_sword = ITEMS.register("neutron_sword",
                ()-> new NeutronSwordItem(AEToolTiers.NEUTRON,new Item.Properties()
                        .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(AEToolTiers.NEUTRON, 0, AEToolTiers.NEUTRON.getSpeed()))));
        public static DeferredItem<Item> neutron_axe = ITEMS.register("neutron_axe",
            ()-> new NeutronAxeItem(AEToolTiers.NEUTRON,new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(AEToolTiers.NEUTRON, 0, AEToolTiers.NEUTRON.getSpeed()))));
        public static DeferredItem<Item> neutron_pickaxe = ITEMS.register("neutron_pickaxe",
            ()-> new NeutronPickaxeItem(AEToolTiers.NEUTRON,new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(AEToolTiers.NEUTRON, 0, AEToolTiers.NEUTRON.getSpeed()))));
        public static DeferredItem<Item> neutron_shovel = ITEMS.register("neutron_shovel",
            ()-> new NeutronShovelItem(AEToolTiers.NEUTRON,new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(AEToolTiers.NEUTRON, 0, AEToolTiers.NEUTRON.getSpeed()))));
        public static DeferredItem<Item> neutron_hoe = ITEMS.register("neutron_hoe",
            ()-> new NeutronHoeItem(AEToolTiers.NEUTRON,new Item.Properties()
                    .fireResistant().rarity(ModRarities.EPIC).stacksTo(1).attributes(createAttributes(AEToolTiers.NEUTRON, 0, AEToolTiers.NEUTRON.getSpeed()))));


    public static DeferredItem<Item> star = ITEMS.register("star",
            ()-> new Item(new Item.Properties().fireResistant().rarity(ModRarities.COSMIC.getValue()).stacksTo(16)));
    public static DeferredItem<Item> star_dessert = ITEMS.register("star_dessert",
            ()-> new Item(new Item.Properties().fireResistant().rarity(ModRarities.EPIC).stacksTo(16).food(AEFoods.star_dessert)));




    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
