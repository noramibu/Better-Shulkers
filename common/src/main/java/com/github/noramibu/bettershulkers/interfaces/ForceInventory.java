package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

/**
 * Forces an inventory to remain open even if there is no shulker box block entity available
 */
public interface ForceInventory {
    /**
     * Sets the inventory of a shulker box
     * @param inventory The inventory to override the existing with
     */
    void setInventory(NonNullList<ItemStack> inventory);

    /**
     * Sets the inventory to not be forcefully closed by the game
     */
    void setForced();

    /**
     * If the inventory is being forced open
     * @return If the inventory is being forced open
     */
    boolean forced();
}
