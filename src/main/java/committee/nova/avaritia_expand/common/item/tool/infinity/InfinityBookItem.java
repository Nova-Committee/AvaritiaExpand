package committee.nova.avaritia_expand.common.item.tool.infinity;

import committee.nova.avaritia_expand.client.render.InfinityBookRender;
import committee.nova.avaritia_expand.util.ProjectileRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InfinityBookItem extends Item implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    public InfinityBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            spawnRandomProjectile(level, player);
        }


        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    public static void spawnRandomProjectile(Level level, Player player) {

        if (ProjectileRegistry.PROJECTILES.isEmpty()) return;

        int index = level.random.nextInt(ProjectileRegistry.PROJECTILES.size());

        EntityType<? extends Projectile> type = ProjectileRegistry.PROJECTILES.get(index);

        Projectile projectile = type.create(level);

        if (projectile == null) return;

        projectile.setOwner(player);

        projectile.setPos(
                player.getX(),
                player.getEyeY(),
                player.getZ()
        );

        projectile.shootFromRotation(
                player,
                player.getXRot(),
                player.getYRot(),
                0,
                1.5f,
                0f
        );

        level.addFreshEntity(projectile);

    }

    //GeckoLib
    @Override
    public double getTick(Object itemStack) {
        return RenderUtil.getCurrentTick();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private PlayState predicate(AnimationState animationState){
        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @SuppressWarnings("removal")
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions(){
            private InfinityBookRender renderer;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null){
                    renderer = new InfinityBookRender();
                }
                return this.renderer;
            }
        });
    }
}
