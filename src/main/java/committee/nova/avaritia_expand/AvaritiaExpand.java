package committee.nova.avaritia_expand;


import committee.nova.avaritia_expand.init.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;


@Mod(AvaritiaExpand.MOD_ID)
public class AvaritiaExpand {

    public static final String MOD_ID = "avaritia_expand";
    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
    public AvaritiaExpand(IEventBus bus, ModContainer container) {
        AEConfig.register(container);
        AEItems.register(bus);
        AECreativeModeTabs.register(bus);
        AEEntities.register(bus);
        AEBlocks.register(bus);
        AERecipeSerializers.SERIALIZERS.register(bus);
    }


}
