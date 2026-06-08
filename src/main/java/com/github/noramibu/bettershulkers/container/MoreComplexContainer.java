/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

/**
 * Allows interfacing directly with the items in a Container
 */
public interface MoreComplexContainer {
    /**
     * Sets the items inside a Container
     * @param items List of items
     */
    void setItems(NonNullList<ItemStack> items);
}
