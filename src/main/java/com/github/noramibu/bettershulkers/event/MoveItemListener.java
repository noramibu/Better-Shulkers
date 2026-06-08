/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.event;

/**
 * Called when an ItemStack moves between Slots
 */
public interface MoveItemListener {
    /**
     * Called when an ItemStack(s) move Slots
     * @param moves The ItemMoves that occurred
     */
    void itemMoved(ItemMove... moves);
}
