/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface MoreComplexContainer {
    void setItems(NonNullList<ItemStack> items);
}
