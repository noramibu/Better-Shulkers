package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.world.item.ItemStack;

/**
 * Detects if a shulker box screen is being viewed by a player
 */
public interface ShulkerViewer {
    /**
     * If the player is viewing a shulker inventory
     * @return True if the player is viewing a shulker inventory
     */
    boolean isViewingShulker();

    /**
     * Sets the player to view the shulker box
     * @param shulker The shulker box item
     */
    void setViewing(ItemStack shulker);

    /**
     * Gets the currently viewed shulker box item
     * @return The currently viewed shulker box item, or null if not viewing one
     */
    ItemStack getViewedStack();
}
