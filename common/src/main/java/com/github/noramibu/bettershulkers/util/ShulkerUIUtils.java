package com.github.noramibu.bettershulkers.util;

import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Utilities for managing Shulker Box UIs
 */
public final class ShulkerUIUtils {
    private static MenuProvider createMenu(ItemStack shulkerItem) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return shulkerItem.getDisplayName();
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
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
    public static void openMenu(ItemStack shulkerItem, ServerPlayer player) {
        MenuProvider provider = createMenu(shulkerItem);
        player.openMenu(provider);
        ((ShulkerViewer) player.containerMenu).addViewing(shulkerItem, player);

        ShulkerUtil.playLocalSound(player, SoundEvents.SHULKER_BOX_OPEN);
    }
}
