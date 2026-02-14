package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.client.render.entity.ExtremeWitherRender;
import committee.nova.avaritia_expand.client.render.entity.InfinityTntRender;
import committee.nova.avaritia_expand.common.entity.ExtremeWitherEntity;
import committee.nova.avaritia_expand.common.entity.InfinityTntEntity;
import committee.nova.avaritia_expand.common.entity.ThrownInfinityBottle;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AEEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE,AvaritiaExpand.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<InfinityTntEntity>> INFINITY_TNT_ENTITY = ENTITIES.register("infinity_tnt_entity",
            () -> EntityType.Builder.<InfinityTntEntity>of(InfinityTntEntity::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0.98F, 0.98F)
                    .eyeHeight(0.15F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
                    .build(AvaritiaExpand.rl("infinity_tnt_entity").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<ThrownInfinityBottle>> INFINITY_BOTTLE = ENTITIES.register("infinity_bottle",
            () -> EntityType.Builder.<ThrownInfinityBottle>of(ThrownInfinityBottle::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(AvaritiaExpand.rl("infinity_bottle").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<ExtremeWitherEntity>> EXTREME_WITHER = ENTITIES.register("extreme_wither",
            () -> EntityType.Builder.<ExtremeWitherEntity>of(ExtremeWitherEntity::new, MobCategory.MONSTER)
                    .sized(0.9F, 3.5F)
                    .clientTrackingRange(10)
                    .fireImmune()
                    .immuneTo(Blocks.WITHER_ROSE)
                    .build(AvaritiaExpand.rl("extreme_wither").toString()));

    public static void register(IEventBus bus){
        ENTITIES.register(bus);
    }

    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup() {
        EntityRenderers.register(AEEntities.INFINITY_TNT_ENTITY.get(), InfinityTntRender::new);
        EntityRenderers.register(AEEntities.EXTREME_WITHER.get(),ExtremeWitherRender::new);
    }

}
