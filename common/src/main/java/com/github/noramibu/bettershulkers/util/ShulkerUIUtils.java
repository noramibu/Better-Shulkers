package com.github.noramibu.bettershulkers.util;

import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class ShulkerUIUtils {
    private static MenuProvider createMenu(ItemStack shulkerItem) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return shulkerItem.getDisplayName();
            }

            @Override
            public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                NonNullList<ItemStack> shulkerInventory = ShulkerUtil.getInventoryFromShulker(shulkerItem);
                SimpleContainer container = new FilledSimpleContainer(shulkerInventory);
                return new ShulkerBoxMenu(i, inventory, container);
            }
        };
    }

    /**
     * Opens a Shulker Box menu
     * @param shulkerItem Shulker box item to get the inventory from
     * @param player The player opening the menu
     */
    public static void openMenu(ItemStack shulkerItem, Player player) {
        MenuProvider provider = createMenu(shulkerItem);
        player.openMenu(provider);
        ((ShulkerViewer) player.containerMenu).addViewing(shulkerItem);

        //: >=1.21.6
        player.level().playSound(null, player.blockPosition(), SoundEvents.SHULKER_BOX_OPEN, player.getSoundSource(), 1.0F, 1.0F);
        //: END

        /*\ <=1.21.5
        player.serverLevel().playSound(null, player.blockPosition(), SoundEvents.SHULKER_BOX_OPEN, player.getSoundSource(), 1.0F, 1.0F);
        \END */
    }
}
