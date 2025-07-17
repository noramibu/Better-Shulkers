package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * Allows opening the shulker box's inventory without a block entity being present
 */
public interface RemoteInventory {
    /**
     * Opens the inventory of a shulker box
     * @param player The player viewing the inventory
     * @param stack The shulker box item
     */
    void openInventory(ServerPlayer player, ItemStack stack);
}
