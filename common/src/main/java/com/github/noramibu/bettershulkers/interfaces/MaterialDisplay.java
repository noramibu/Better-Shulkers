package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.world.item.ItemStack;

/**
 * A visual representation of a shulker box's material using displays
 */
public interface MaterialDisplay {
    /**
     * Creates a material display for the shulker box
     * @param shulkerStack The shulker box item
     */
    void createDisplay(ItemStack shulkerStack);
}
