/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.container;

import net.minecraft.world.item.ItemStack;

public interface MutableContainerContents {
    // Returns if there was any overflow
    int add(ItemStack type, int amount);
    void set(int i, ItemStack stack);
}
