package committee.nova.avaritia_expand.common.menu;

import committee.nova.avaritia_expand.init.registry.AEMenus;
import committee.nova.mods.avaritia.init.registry.ModMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class BurnerMenu extends AbstractContainerMenu {

    private final Container container;
    private final Level level;
    private final Player player;

    private final CachedRecipe[] cache = new CachedRecipe[3];

    private final int[][] SLOT_POSITIONS = {
            {65, 67},   // 第一个槽位 (SMOKING)
            {82, 36},  // 第二个槽位 (BLASTING)
            {99, 67}   // 第三个槽位 (SMELTING)
    };

    public BurnerMenu(int windowId, Inventory playerInventory) {
        super(AEMenus.burner_menu.get(), windowId);

        this.container = new SimpleContainer(3);
        this.level = playerInventory.player.level();
        this.player = playerInventory.player;

        //输入槽
        for (int i = 0; i < 3; i++) {
            int x = SLOT_POSITIONS[i][0];
            int y = SLOT_POSITIONS[i][1];
            addSlot(new Slot(container, i, x, y));
            cache[i] = new CachedRecipe();
        }

        // 玩家背包
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 124 + row * 18));

        // 快捷栏
        for (int i = 0; i < 9; i++)
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 182));
    }

    public BurnerMenu(int id, Inventory inventory, RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(id, inventory);
    }

    public BurnerMenu(int id, Inventory playerInventory, int slot) {
        this(slot, playerInventory);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (level.isClientSide) return;

        for (int i = 0; i < 3; i++) {
            ItemStack input = container.getItem(i);
            if (input.isEmpty()) {
                cache[i].clear();
                continue;
            }

            CachedRecipe c = cache[i];

            if (!ItemStack.isSameItemSameComponents(input, c.lastInput)) {
                findRecipe(i, input, c);
            }

            if (c.recipe == null) continue;

            int count = Math.min(input.getCount(), c.recipe.getResultItem(level.registryAccess()).getMaxStackSize());

            ItemStack result = c.recipe.assemble(
                    new SingleRecipeInput(input),
                    level.registryAccess()
            );

            result.setCount(result.getCount() * count);

            input.shrink(count);

            giveOrDrop(result);
        }
    }

    private void findRecipe(int slot, ItemStack stack, CachedRecipe cache) {
        cache.clear();

        RecipeType<? extends AbstractCookingRecipe> type = getRecipeTypeForSlot(slot);

        Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> recipe =
                level.getRecipeManager().getRecipeFor(
                        type,
                        new SingleRecipeInput(stack),
                        level
                );

        recipe.ifPresent(r -> {
            cache.recipe = r.value();
            cache.lastInput = stack.copy();
        });
    }

    private void giveOrDrop(ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    private RecipeType<? extends AbstractCookingRecipe> getRecipeTypeForSlot(int slotIndex) {
        return switch (slotIndex) {
            case 0 -> RecipeType.SMOKING;
            case 1 -> RecipeType.BLASTING;
            case 2 -> RecipeType.SMELTING;
            default -> throw new IllegalStateException();
        };
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);

        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack originalStack = stack.copy();

        if (index < 3) {
            if (!moveItemStackTo(stack, 3, slots.size(), true))
                slot.set(originalStack);
                return ItemStack.EMPTY;
        } else {
            if (!moveItemStackTo(stack, 0, 3, false))
                slot.set(originalStack);
                return ItemStack.EMPTY;
        }

    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
//返还物品
    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!level.isClientSide) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack item = container.removeItemNoUpdate(i);
                if (!item.isEmpty()) {
                    player.getInventory().placeItemBackInInventory(item);
                }
            }
        }
    }

    private static class CachedRecipe {
        AbstractCookingRecipe recipe;
        ItemStack lastInput = ItemStack.EMPTY;

        void clear() {
            recipe = null;
            lastInput = ItemStack.EMPTY;
        }
    }
}

