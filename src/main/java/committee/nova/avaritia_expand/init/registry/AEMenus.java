package committee.nova.avaritia_expand.init.registry;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.client.screen.BurnerScreen;
import committee.nova.avaritia_expand.common.menu.BurnerMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AEMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, AvaritiaExpand.MOD_ID);

    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup(RegisterMenuScreensEvent event) {
        event.register(burner_menu.get(), BurnerScreen::new);

    }

    public static DeferredHolder<MenuType<?>, MenuType<BurnerMenu>> burner_menu = menu("burner_menu",
            () -> new MenuType<>((IContainerFactory<BurnerMenu>)BurnerMenu::new, FeatureFlagSet.of()));

    public static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> menu(String name, Supplier<? extends MenuType<T>> container) {
        return MENUS.register(name, container);
    }
}
