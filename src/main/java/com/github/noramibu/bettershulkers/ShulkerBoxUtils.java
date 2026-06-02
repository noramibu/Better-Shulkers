/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ShulkerBoxUtils {
    public static void openInventory(ItemStack shulkerbox, Player player) {
        VirtualShulkerBoxContainer container = new VirtualShulkerBoxContainer(shulkerbox, player);
        ((VirtualContainerHolder) player).setVirtualContainer(container);
        player.openMenu(container);
    }
}
