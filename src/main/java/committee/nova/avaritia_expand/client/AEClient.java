package committee.nova.avaritia_expand.client;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.client.model.loader.GlowEdgeModelLoader;
import committee.nova.avaritia_expand.client.shader.AEShaders;
import committee.nova.avaritia_expand.init.registry.AEEntities;
import committee.nova.mods.avaritia.client.screen.AvaritiaConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = AvaritiaExpand.MOD_ID, value = Dist.CLIENT)
public class AEClient {
    @SubscribeEvent
    public static void registerLoaders(ModelEvent.RegisterGeometryLoaders event) {

        event.register(AvaritiaExpand.rl("glow_edge"), GlowEdgeModelLoader.INSTANCE);
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegisterShaders(RegisterShadersEvent event) {
        AEShaders.onRegisterShaders(event);//注册着色器
    }

    @SubscribeEvent
    public static void clientSetUp(FMLClientSetupEvent event) {
        ModList.get().getModContainerById(AvaritiaExpand.MOD_ID).orElseThrow().registerExtensionPoint(IConfigScreenFactory.class,
                (container, last) -> new AvaritiaConfigScreen(last));
        AEEntities.onClientSetup();
    }
}