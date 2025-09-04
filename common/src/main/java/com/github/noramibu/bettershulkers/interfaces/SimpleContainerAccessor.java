package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

/**
 * Allows modification of a {@link net.minecraft.world.SimpleContainer}
 */
public interface SimpleContainerAccessor {
    /**
     * Sets the items variable
     * @param inventory Inventory of a Shulker Box
     */
    void setItems(NonNullList<ItemStack> inventory);

    /**
     * Sets the size variable
     * @param size Size of the Shulker Box (27)
     */
    void setSize(int size);
}
