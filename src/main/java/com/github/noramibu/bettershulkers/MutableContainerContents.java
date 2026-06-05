/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers;

import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public interface MutableContainerContents {
    // Returns if there was any overflow
    int add(ItemStack type, int amount);
    // Returns the number of elements NOT removed
    int remove(ItemStack type, int amount);
    void set(int i, ItemStack stack);
    List<Optional<ItemStackTemplate>> getItems();
}
