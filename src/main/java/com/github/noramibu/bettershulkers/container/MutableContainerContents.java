/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.container;

import net.minecraft.world.item.ItemStack;

/**
 * Allows modifying Container contents without reassignment
 */
public interface MutableContainerContents {
    /**
     * Adds an item to the Container
     * @param type The type of item to add
     * @param amount The amount of that item to add
     * @return If there was overflow
     */
    int add(ItemStack type, int amount);

    /**
     * Sets the item in a Slot
     * @param i Slot index
     * @param stack The stack to replace that Slot with
     */
    void set(int i, ItemStack stack);
}
