/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class ShulkerBoxUtils {
    public static void openExternalShulker(ItemStack shulkerbox, Player player) {
        if (isServerSide(player)) {
            VirtualShulkerBoxContainer container = new VirtualShulkerBoxContainer(shulkerbox, player);
            ((VirtualContainerHolder) player).setVirtualContainer(container);
            player.openMenu(container);
        }
    }

    public static void openInternalShulker(ItemStack shulkerbox, Player player, Slot slot) {
        // Check if item is in the inventory
        if (isServerSide(player) && isSlotInInventory(slot, player)) {
            // Check if menu is already open
            if (player.containerMenu instanceof VirtualShulkerBoxMenu menu) {
                System.out.println("Opening Shulker at " + slot.index);
                menu.container.reload(shulkerbox, player, slot.index);
            } else {
                // Open new menu
                System.out.println("Opening Shulker at " + getShulkerIndex(slot));
                VirtualShulkerBoxContainer container = new VirtualShulkerBoxContainer(shulkerbox, player, getShulkerIndex(slot));
                ((VirtualContainerHolder) player).setVirtualContainer(container);
                player.openMenu(container);
            }
        }
    }

    private static boolean isSlotInInventory(Slot slot, Player player) {
        return slot.container == player.getInventory();
    }

    private static int getShulkerIndex(Slot slot) {
        int index = slot.getContainerSlot();
        int column = index % 9;
        int row = index / 9;

        System.out.println("Row: " + row + ", Col: " + column);
        return 27 + ((3 - row) * 9) + column;
    }

    public static boolean isServerSide(Player player) {
        return !player.level().isClientSide();
    }
}
